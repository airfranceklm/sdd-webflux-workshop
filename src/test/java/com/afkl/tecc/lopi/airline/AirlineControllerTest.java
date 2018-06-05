package com.afkl.tecc.lopi.airline;

import com.afkl.tecc.lopi.common.PagedResponse;
import com.afkl.tecc.lopi.csv.DataRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AirlineConfiguration.class)
@WebFluxTest
@FixMethodOrder(NAME_ASCENDING)
@ComponentScan("com.afkl.tecc.lopi.airline")
public class AirlineControllerTest {

    @MockBean
    private DataRepository<Airline> repository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldListTenAirlineByDefault() {
        var data = IntStream.range(1, 20).mapToObj((i) -> new Airline(i, null, null, null, null, null, null, false));
        BDDMockito.given(repository.stream()).willReturn(Flux.fromStream(data));
        webTestClient.get().uri("/airlines").exchange()
                .expectStatus().isOk()
                .expectBody(PagedResponse.class)
                .consumeWith((result) -> {
                    assertThat(result.getResponseBody().getResults().size()).isEqualTo(10);
                    assertThat(result.getResponseBody().getCursor()).isEqualTo(10);
                });
    }

    @Test
    public void shouldSkipUntilCursor() {
        var data = IntStream.range(1, 20).mapToObj((i) -> new Airline(i, null, null, null, null, null, null, false));
        BDDMockito.given(repository.stream()).willReturn(Flux.fromStream(data));
        webTestClient.get().uri("/airlines?size=2&cursor=4").exchange()
                .expectStatus().isOk()
                .expectBody(PagedResponse.class)
                .consumeWith((result) -> {
                    assertThat(result.getResponseBody().getResults().size()).isEqualTo(2);
                    assertThat(result.getResponseBody().getCursor()).isEqualTo(6);
                });
    }

    @Test
    public void shouldFilterResults() {
        BDDMockito.given(repository.stream()).willReturn(Flux.just(
                new Airline(1, "KLM", null, null, null, null, null, false),
                new Airline(2, "AIR FRANCE", null, null, null, null, null, false)
        ));
        webTestClient.get().uri("/airlines?q=kl").exchange()
                .expectStatus().isOk()
                .expectBodyList(Airline.class)
                .consumeWith((result) -> {
                    assertThat(result.getResponseBody().size()).isEqualTo(1);
                    assertThat(result.getResponseBody().stream().findFirst()).isPresent();
                    assertThat(result.getResponseBody().stream().findFirst().get().getName()).isEqualToIgnoringCase("KLM");
                });
    }
}
