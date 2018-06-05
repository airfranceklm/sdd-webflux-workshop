package com.afkl.tecc.lopi.airport;

import com.afkl.tecc.lopi.csv.CsvFileParser;
import com.afkl.tecc.lopi.csv.CsvLineParser;
import com.afkl.tecc.lopi.csv.CsvLineReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AirportConfiguration {

    /*
     * AIRPORT parsing notes:
     *
     * Airport ID: Unique OpenFlights identifier for this airport.
     * Name: Name of airport. May or may not contain the City name.
     * City: Main city served by airport. May be spelled differently from Name.
     * Country: Country or territory where airport is located. See countries.dat to cross-reference to ISO 3166-1 codes.
     * IATA: 3-letter IATA code. Null if not assigned/unknown.
     * ICAO: 4-letter ICAO code. Null if not assigned.
     * Latitude: Decimal degrees, usually to six significant digits. Negative is South, positive is North.
     * Longitude: Decimal degrees, usually to six significant digits. Negative is West, positive is East.
     *  Altitude: In feet.
     * Timezone: Hours offset from UTC. Fractional hours are expressed as decimals, eg. India is 5.5.
     * DST: Daylight savings time. One of E (Europe), A (US/Canada), S (South America), O (Australia), Z (New Zealand), N (None) or U (Unknown). See also: Help: Time
     * Tz database time zone: Timezone in "tz" (Olson) format, eg. "America/Los_Angeles".
     * Type: Type of the airport. Value "airport" for air terminals, "station" for train stations, "port" for ferry terminals and "unknown" if not known. In airports.csv, only type=airport is included.
     * Source: Source of this data. "OurAirports" for data sourced from OurAirports, "Legacy" for old data not matched to OurAirports (mostly DAFIF), "User" for unverified user contributions. In airports.csv, only source=OurAirports is included.
     */
    @Bean
    public CsvLineParser<Airport> airportCsvLineParser() {
        return (line) -> CsvLineReader.read(line, 13, (data) -> new Airport(
                data[0], // Airport ID
                data[1], // Name
                data[2], // City
                data[3], // Country
                data[4], // IATA code
                data[5], // ICAO code
                Double.valueOf(data[6]), // Latitude
                Double.valueOf(data[7]), // Longitude
                Double.valueOf(data[8]), // Timezone
                data[9], // DST
                data[10], // TZ Timezone
                data[11], // Type
                data[12])); // Source
    }

    @Bean
    public CsvFileParser<Airport> airportCsvFileParser() {
        // @formatter:off
        return new CsvFileParser<>() {};
        // @formatter:on
    }

}
