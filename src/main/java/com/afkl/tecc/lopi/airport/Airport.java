package com.afkl.tecc.lopi.airport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data holder for airport data.
 * Unfortunately lombok does not work with JDK10, see link below.
 *
 * @see <a href="https://github.com/rzwitserloot/lombok/issues/1572">Lombok fails with JDK 10</a>
 */
public class Airport {

    private final String id;
    private final String name;
    private final String city;
    private final String country;
    private final String iataId;
    private final String icaoId;
    private final double latitude;
    private final double longitude;
    private final double timezone;
    private final String dst;
    private final String tzTimezone;
    private final String type;
    private final String source;

    @JsonCreator
    public Airport(@JsonProperty("id") String id,
                   @JsonProperty("name") String name,
                   @JsonProperty("city") String city,
                   @JsonProperty("country") String country,
                   @JsonProperty("iataId") String iataId,
                   @JsonProperty("icaoId") String icaoId,
                   @JsonProperty("latitude") double latitude,
                   @JsonProperty("longitude") double longitude,
                   @JsonProperty("timezone") double timezone,
                   @JsonProperty("dst") String dst,
                   @JsonProperty("tzTimezone") String tzTimezone,
                   @JsonProperty("type") String type,
                   @JsonProperty("source") String source) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.iataId = iataId;
        this.icaoId = icaoId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.dst = dst;
        this.tzTimezone = tzTimezone;
        this.type = type;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getIataId() {
        return iataId;
    }

    public String getIcaoId() {
        return icaoId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getTimezone() {
        return timezone;
    }

    public String getDst() {
        return dst;
    }

    public String getTzTimezone() {
        return tzTimezone;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

}
