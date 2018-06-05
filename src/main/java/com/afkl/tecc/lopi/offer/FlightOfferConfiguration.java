package com.afkl.tecc.lopi.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FlightOfferConfiguration {

    @Bean
    public RouterFunction<ServerResponse> offerRoutes(FlightOfferHandler handler) {
        return route(GET("/offers/{origin}"), handler::streamOffers);
    }

}
