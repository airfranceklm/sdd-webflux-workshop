package com.afkl.tecc.lopi.route;

import com.afkl.tecc.lopi.csv.CsvFileParser;
import com.afkl.tecc.lopi.csv.CsvLineParser;
import com.afkl.tecc.lopi.csv.CsvLineReader;
import com.afkl.tecc.lopi.csv.DataRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class NetworkRouteConfiguration {

    /*
     * ROUTE parsing notes:
     *
     * Airline: 2-letter (IATA) or 3-letter (ICAO) code of the airline.
     * Airline ID: Unique OpenFlights identifier for airline (see Airline).
     * Source airport: 3-letter (IATA) or 4-letter (ICAO) code of the source airport.
     * Source airport ID: Unique OpenFlights identifier for source airport (see Airport)
     * Destination airport: 3-letter (IATA) or 4-letter (ICAO) code of the destination airport.
     * Destination airport ID: Unique OpenFlights identifier for destination airport (see Airport)
     * Codeshare: "Y" if this flight is a codeshare (that is, not operated by Airline, but another carrier), empty otherwise.
     * Stops: Number of stops on this flight ("0" for direct)
     * Equipment: 3-letter codes for plane type(s) generally used on this flight, separated by spaces
     *
     * The data is ISO 8859-1 (Latin-1) encoded. The special value \N is used for "NULL" to indicate that no value is available, and is understood automatically by MySQL if imported.
     *
     * Notes:
     * Routes are directional: if an airline operates services from A to B and from B to A, both A-B and B-A are listed separately.
     * Routes where one carrier operates both its own and codeshare flights are listed only once.
     */
    @Bean
    public CsvLineParser<NetworkRoute> routeCsvLineParser() {
        return (line) -> CsvLineReader.read(line, 10, (data) -> new NetworkRoute(
                data[0], // Airline code
                data[1], // Airline ID
                data[2], // Source Airport
                data[3], // Source Airport ID
                data[4], // Destination Airport
                data[5], // Destination Airport ID
                data[6] != null && data[6].equals("Y"), // Codeshare
                Integer.parseInt(data[7]), // Number of Stops
                data[8] != null ? data[8].split(" ")[0].trim() : null // Equipment
        ));
    }

    @Bean
    public CsvFileParser<NetworkRoute> routeCsvFileParser() {
        // @formatter:off
        return new CsvFileParser<>() {};
        // @formatter:on
    }

    @Bean
    public DataRepository<NetworkRoute> routeRepository(CsvFileParser<NetworkRoute> fileParser, CsvLineParser<NetworkRoute> lineParser) {
        return new DataRepository<>(new ClassPathResource("/data/routes.dat"), fileParser, lineParser);
    }

}
