package com.afkl.tecc.lopi.airline;

import com.afkl.tecc.lopi.csv.CsvFileParser;
import com.afkl.tecc.lopi.csv.CsvLineParser;
import com.afkl.tecc.lopi.csv.CsvLineReader;
import com.afkl.tecc.lopi.csv.DataRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AirlineConfiguration {

    /*
     * AIRLINE Parsing notes:
     *
     * Airline ID: Unique OpenFlights identifier for this airline.
     * Name: Name of the airline.
     * Alias: Alias of the airline. For example, All Nippon Airways is commonly known as "ANA".
     * IATA: 2-letter IATA code, if available.
     * ICAO: 3-letter ICAO code, if available.
     * Callsign: Airline callsign.
     * Country: Country or territory where airline is incorporated.
     * Active: "Y" if the airline is or has until recently been operational, "N" if it is defunct. This field is not
     *  reliable: in particular, major airlines that stopped flying long ago, but have not had their IATA code reassigned
     *  (eg. Ansett/AN), will incorrectly show as "Y".
     *  The data is ISO 8859-1 (Latin-1) encoded. The special value \N is used for "NULL" to indicate that no value is
     *  available, and is understood automatically by MySQL if imported.
     *
     * Notes: Airlines with null codes/callsigns/countries generally represent user-added airlines. Since the data is
     * intended primarily for current flights, defunct IATA codes are generally not included. For example, "Sabena"
     * is not listed with a SN IATA code, since "SN" is presently used by its successor Brussels Airlines.
     */
    @Bean
    public CsvLineParser<Airline> airlineCsvLineParser() {
        return (line) -> CsvLineReader.read(line, 9, (data) -> new Airline(
                Integer.parseInt(data[0]), // Airline ID
                data[1], // Name
                data[2], // Alias
                data[3], // IATA code
                data[4], // ICAO code
                data[5], // Callsign
                data[6], // Country
                data[7].equals("Y") // Active
        ));
    }

    @Bean
    public CsvFileParser<Airline> airlineCsvFileParser() {
        // @formatter:off
        return new CsvFileParser<>() {};
        // @formatter:on
    }

    @Bean
    public RouterFunction<ServerResponse> airlineRoutes(AirlineHandler handler) {
        return route(GET("/airlines").and(queryParam("q", (q) -> true)), handler::find)
                .andRoute(GET("/airlines"), handler::list);
    }

    @Bean
    public DataRepository<Airline> airlineRepository(CsvFileParser<Airline> fileParser, CsvLineParser<Airline> lineParser) {
        return new DataRepository<>(new ClassPathResource("/data/airlines.dat"), fileParser, lineParser);
    }

}
