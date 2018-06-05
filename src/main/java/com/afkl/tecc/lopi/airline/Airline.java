package com.afkl.tecc.lopi.airline;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Data holder for airline data.
 * Unfortunately lombok does not work with JDK10, see link below.
 *
 * @see <a href="https://github.com/rzwitserloot/lombok/issues/1572">Lombok fails with JDK 10</a>
 */
public class Airline implements Comparable<Airline> {

    private final int id;
    private final String name;
    private final String alias;
    private final String iataId;
    private final String icaoId;
    private final String callSign;
    private final String country;
    private final boolean active;

    @JsonCreator
    public Airline(@JsonProperty("id") int id,
                   @JsonProperty("name") String name,
                   @JsonProperty("alias") String alias,
                   @JsonProperty("iataId") String iataId,
                   @JsonProperty("icaoId") String icaoId,
                   @JsonProperty("callSign") String callSign,
                   @JsonProperty("country") String country,
                   @JsonProperty("active") boolean active) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.iataId = iataId;
        this.icaoId = icaoId;
        this.callSign = callSign;
        this.country = country;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getIataId() {
        return iataId;
    }

    public String getIcaoId() {
        return icaoId;
    }

    public String getCallSign() {
        return callSign;
    }

    public String getCountry() {
        return country;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var airline = (Airline) o;
        return isActive() == airline.isActive() &&
                Objects.equals(getId(), airline.getId()) &&
                Objects.equals(getName(), airline.getName()) &&
                Objects.equals(getAlias(), airline.getAlias()) &&
                Objects.equals(getIataId(), airline.getIataId()) &&
                Objects.equals(getIcaoId(), airline.getIcaoId()) &&
                Objects.equals(getCallSign(), airline.getCallSign()) &&
                Objects.equals(getCountry(), airline.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAlias(), getIataId(), getIcaoId(), getCallSign(), getCountry(), isActive());
    }

    @Override
    public int compareTo(Airline other) {
        return getId() - other.getId();
    }
}
