package com.afkl.tecc.lopi.offer;

import com.afkl.tecc.lopi.airline.Airline;
import com.afkl.tecc.lopi.airport.Airport;
import com.afkl.tecc.lopi.plane.Plane;

/**
 * Data holder for flight offers.
 * Unfortunately lombok does not work with JDK10, see link below.
 *
 * @see <a href="https://github.com/rzwitserloot/lombok/issues/1572">Lombok fails with JDK 10</a>
 */
public class FlightOffer {

    private final Airport destination;
    private final Plane plane;
    private final Airline airline;
    private final boolean codeshare;
    private final int stops;
    private final int fare;

    public FlightOffer(Airport destination,
                       Plane plane,
                       Airline airline,
                       boolean codeshare,
                       int stops,
                       int fare) {
        this.destination = destination;
        this.plane = plane;
        this.airline = airline;
        this.codeshare = codeshare;
        this.stops = stops;
        this.fare = fare;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Airport getDestination() {
        return destination;
    }

    public Plane getPlane() {
        return plane;
    }

    public Airline getAirline() {
        return airline;
    }

    public boolean isCodeshare() {
        return codeshare;
    }

    public int getStops() {
        return stops;
    }

    public int getFare() {
        return fare;
    }

    public static final class Builder {

        private String originId;
        private Airport destination;
        private String destinationId;
        private Plane plane;
        private String planeId;
        private Airline airline;
        private String airlineId;
        private boolean codeshare;
        private int stops;
        private int fare;

        // @formatter:off
        private Builder(){}
        // @formatter:on

        public Builder setFare(int fare) {
            this.fare = fare;
            return this;
        }

        public Builder setDestination(Airport destination) {
            this.destination = destination;
            return this;
        }

        public Builder setPlane(Plane plane) {
            this.plane = plane;
            return this;
        }

        public Builder setAirline(Airline airline) {
            this.airline = airline;
            return this;
        }

        public Builder setCodeshare(boolean codeshare) {
            this.codeshare = codeshare;
            return this;
        }

        public Builder setStops(int stops) {
            this.stops = stops;
            return this;
        }

        public Builder setOriginId(String originId) {
            this.originId = originId;
            return this;
        }

        public Builder setDestinationId(String destinationId) {
            this.destinationId = destinationId;
            return this;
        }

        public Builder setPlaneId(String planeId) {
            this.planeId = planeId;
            return this;
        }

        public Builder setAirlineId(String airlineId) {
            this.airlineId = airlineId;
            return this;
        }

        public String getOriginId() {
            return originId;
        }

        public String getDestinationId() {
            return destinationId;
        }

        public String getPlaneId() {
            return planeId;
        }

        public String getAirlineId() {
            return airlineId;
        }

        public FlightOffer build() {
            return new FlightOffer(destination, plane, airline, codeshare, stops, fare);
        }
    }
}
