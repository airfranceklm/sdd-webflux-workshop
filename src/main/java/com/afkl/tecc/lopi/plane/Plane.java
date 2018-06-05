package com.afkl.tecc.lopi.plane;

/**
 * Data holder for plane data.
 * Unfortunately lombok does not work with JDK10, see link below.
 *
 * @see <a href="https://github.com/rzwitserloot/lombok/issues/1572">Lombok fails with JDK 10</a>
 */
public class Plane {

    private final String name;
    private final String iataId;
    private final String icaoId;

    public Plane(String name, String iataId, String icaoId) {
        this.name = name;
        this.iataId = iataId;
        this.icaoId = icaoId;
    }

    public String getName() {
        return name;
    }

    public String getIataId() {
        return iataId;
    }

    public String getIcaoId() {
        return icaoId;
    }

}
