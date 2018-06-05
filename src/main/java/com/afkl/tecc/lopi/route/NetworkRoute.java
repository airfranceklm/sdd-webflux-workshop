package com.afkl.tecc.lopi.route;

/**
 * Data holder for network routes.
 * Unfortunately lombok does not work with JDK10, see link below.
 *
 * @see <a href="https://github.com/rzwitserloot/lombok/issues/1572">Lombok fails with JDK 10</a>
 */
public class NetworkRoute {

    private final String airline;
    private final String airlineId;
    private final String sourceAirport;
    private final String sourceAirportId;
    private final String destinationAirport;
    private final String destinationAirportId;
    private final boolean codeshare;
    private final int stops;
    private final String equipment;

    public NetworkRoute(String airline,
                        String airlineId,
                        String sourceAirport,
                        String sourceAirportId,
                        String destinationAirport,
                        String destinationAirportId,
                        boolean codeshare,
                        int stops,
                        String equipment) {
        this.airline = airline;
        this.airlineId = airlineId;
        this.sourceAirport = sourceAirport;
        this.sourceAirportId = sourceAirportId;
        this.destinationAirport = destinationAirport;
        this.destinationAirportId = destinationAirportId;
        this.codeshare = codeshare;
        this.stops = stops;
        this.equipment = equipment;
    }

    public String getAirline() {
        return airline;
    }

    public String getAirlineId() {
        return airlineId;
    }

    public String getSourceAirport() {
        return sourceAirport;
    }

    public String getSourceAirportId() {
        return sourceAirportId;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public String getDestinationAirportId() {
        return destinationAirportId;
    }

    public boolean isCodeshare() {
        return codeshare;
    }

    public int getStops() {
        return stops;
    }

    public String getEquipment() {
        return equipment;
    }

}
