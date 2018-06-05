package com.afkl.tecc.lopi.airline;

import com.afkl.tecc.lopi.common.HttpError;
import com.afkl.tecc.lopi.common.OpenFlightHandler;
import com.afkl.tecc.lopi.common.PagedResponse;
import com.afkl.tecc.lopi.csv.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@OpenFlightHandler
public class AirlineHandler {

    private final DataRepository<Airline> repository;

    @Autowired
    public AirlineHandler(DataRepository<Airline> repository) {
        this.repository = repository;
    }

    protected Mono<ServerResponse> list(ServerRequest req) {
        if (req.queryParam("q").isPresent()) {
            return find(req);
        }
        var size = req.queryParam("size").map(Integer::parseInt)
                .filter((s) -> s > 0)
                .orElse(10);
        var cursor = req.queryParam("cursor").map(Integer::parseInt);
        var stream = repository.stream().sort();
        if (cursor.isPresent()) {
            stream = stream.skipUntil((airline) -> airline.getId() > cursor.get());
        }
        return stream.take(size)
                .collectList()
                .flatMap((airlines) -> {
                    var newCursor = airlines.stream().reduce((f, s) -> s).map(Airline::getId).orElse(0);
                    var response = new PagedResponse<>(airlines, size, newCursor);
                    return airlines.size() == 0 ? Mono.empty() : Mono.just(response);
                }).flatMap((response) -> ServerResponse.ok() // @formatter:off
                        .body(Mono.just(response), new ParameterizedTypeReference<PagedResponse<Airline>>() {})) // @formatter:on
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private Mono<ServerResponse> find(ServerRequest req) {
        return req.queryParam("q").map((query) -> repository.stream()
                .filter((a) -> a.getName().toLowerCase().contains(query.toLowerCase()))
                .collectList()
                .flatMap((airlines) -> airlines.size() == 0 ? Mono.empty() : Mono.just(airlines))
                .flatMap((airlines) -> ServerResponse.ok() // @formatter:off
                    .body(Mono.just(airlines), new ParameterizedTypeReference<List<Airline>>() {})) // @formatter:on
                .switchIfEmpty(ServerResponse.notFound().build()))
                .orElse(ServerResponse.badRequest()
                        .body(Mono.just(new HttpError(400, "Missing query parameter.")), HttpError.class));
    }

}

