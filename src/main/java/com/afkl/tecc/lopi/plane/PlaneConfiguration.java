package com.afkl.tecc.lopi.plane;

import com.afkl.tecc.lopi.csv.CsvFileParser;
import com.afkl.tecc.lopi.csv.CsvLineParser;
import com.afkl.tecc.lopi.csv.CsvLineReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlaneConfiguration {

    /*
     * PLANE parsing notes:
     *
     * Name	Full name of the aircraft.
     * IATA code: Unique three-letter IATA identifier for the aircraft.
     * ICAO code: Unique four-letter ICAO identifier for the aircraft.
     *  The data is UTF-8 encoded. The special value \N is used for "NULL" to indicate that no value is available, and is
     *  understood automatically by MySQL if imported.
     *
     * Notes:
     * Aircraft with IATA but without ICAO codes are generally aircraft classes: for example, IATA "747" can be any type
     * of Boeing 747, whereas IATA "744"/ICAO "B744" is specifically a Boeing 747-400.
     */
    @Bean
    public CsvLineParser<Plane> planeCsvLineParser() {
        return (line) -> CsvLineReader.read(line, 3, (data) -> new Plane(
                data[0], // Name
                data[1], // IATA code
                data[2]) // ICAO code
        );
    }

    @Bean
    public CsvFileParser<Plane> planeCsvFileParser() {
        // @formatter:off
        return new CsvFileParser<>() {};
        // @formatter:on
    }

}
