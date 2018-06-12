package com.afkl.tecc.lopi;

import ch.qos.logback.classic.Level;
import com.afkl.tecc.lopi.offer.FlightOffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class LopiWebClientExample {

    private static final Logger LOG = LoggerFactory.getLogger("WebClient example");

    static {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("root")).setLevel(Level.INFO);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl("http://localhost:8080")
                .filter(ExchangeFilterFunction.ofRequestProcessor((req) -> {
                    LOG.info("Starting request.");
                    return Mono.just(req);
                }))
                .filter(ExchangeFilterFunction.ofResponseProcessor((res) -> {
                    LOG.info("Finalized request.");
                    return Mono.just(res);
                })).build();
    }

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(LopiWebClientExample.class);
        var client = ctx.getBean(WebClient.class);
        var thread = Thread.currentThread();
        try {
            LOG.info("Listing all available fares.");
            client.get().uri("/offers/ams")
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .retrieve()
                    .bodyToFlux(FlightOffer.class)
                    .map((offer) -> String.format("%s for the amazing lowest price of €%s", offer.getDestination().getCity(), offer.getFare()))
                    .doOnComplete(() -> {
                        ctx.close();
                        thread.interrupt();
                    })
                    .doOnError((t) -> LOG.error("Unable to process request.", t))
                    .subscribe((value) -> LOG.info("Retrieved flight offer for Amsterdam to {}", value));
            thread.join();
        } catch (InterruptedException e) {
            LOG.info("Presented all available fares, have a nice day.");
        }
    }

}
