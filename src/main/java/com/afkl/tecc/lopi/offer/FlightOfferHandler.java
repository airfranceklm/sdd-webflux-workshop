package com.afkl.tecc.lopi.offer;

import com.afkl.tecc.lopi.airline.Airline;
import com.afkl.tecc.lopi.airport.Airport;
import com.afkl.tecc.lopi.csv.DataRepository;
import com.afkl.tecc.lopi.plane.Plane;
import com.afkl.tecc.lopi.route.NetworkRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.SecureRandom;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Component
public class FlightOfferHandler {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final DataRepository<Plane> planeRepository;
    private final DataRepository<Airport> airportRepository;
    private final DataRepository<Airline> airlineRepository;
    private final DataRepository<NetworkRoute> routeRepository;

    @Autowired
    public FlightOfferHandler(DataRepository<Plane> planeRepository,
                              DataRepository<Airport> airportRepository,
                              DataRepository<Airline> airlineRepository,
                              DataRepository<NetworkRoute> routeRepository) {
        this.planeRepository = planeRepository;
        this.airportRepository = airportRepository;
        this.airlineRepository = airlineRepository;
        this.routeRepository = routeRepository;
    }

    public Mono<ServerResponse> streamOffers(ServerRequest req) {
        var airports = airportRepository.stream();
        var airlines = airlineRepository.stream();
        var planes = planeRepository.stream();
        var origin = req.pathVariable("origin");
        // Start with the routes, these contain all possible destinations (airports) from a specific origin (airport).
        var publisher = routeRepository.stream()
                .parallel()
                .runOn(Schedulers.parallel()) // This will be quite a big operation, so lets run it in parallel
                .filter((route) -> route.getSourceAirport().equalsIgnoreCase(origin) &&
                        route.getAirline() != null &&
                        route.getDestinationAirport() != null) // Make sure we only have entities we can resolve, and filter out all the incomplete ones
                .flatMap((route) -> Mono.subscriberContext().map((ctx) -> {
                    int bound = ctx.get("bound");
                    return FlightOffer.newBuilder() // Convert a NetworkRoute to a FlightOffer.Builder, we can use the builder to set the missing values in the next steps
                            .setAirlineId(route.getAirline())
                            .setOriginId(route.getSourceAirport())
                            .setDestinationId(route.getDestinationAirport())
                            .setPlaneId(route.getEquipment())
                            .setFare(RANDOM.nextInt(bound)); // Generate a price
                }))
                .flatMap((builder) -> airlines // Retrieve full airline details
                        .filter((airline) -> airline.getIataId() != null && airline.getIataId().equals(builder.getAirlineId()))
                        .map((airline) -> builder.setAirline(airline)))
                .flatMap((builder) -> airports // Retrieve full airport details of the destination airport
                        .filter((airport) -> airport.getIataId() != null && airport.getIataId().equals(builder.getDestinationId()))
                        .map((airport) -> builder.setDestination(airport)))
                .flatMap((builder) -> (builder.getPlaneId() != null) ? planes // Retrieve full aircraft details
                        .filter((plane) -> plane.getIataId() != null && plane.getIataId().equals(builder.getPlaneId()))
                        .map((plane) -> builder.setPlane(plane)) : Flux.just(builder))
                .map((builder) -> builder.build()) // Build the final immutable FlightOffer instance
                .filter((offer) -> offer.getDestination() != null) // some destinations might still be missing, filter them out to avoid client side issues
                .sequential().delayElements(Duration.of(250, MILLIS)) // Delay entities pushed to the client to simulate a slower backend system
                .onBackpressureDrop() // Should backpressure occur simply dump them as they are not of vital importance in this specific case, other scenarios might require other backpressure strategies
                .subscriberContext((ctx) -> ctx.put("bound", 1000)); // Set the bound in the context, to be used by the random number generator
        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(publisher, FlightOffer.class);
    }

}
