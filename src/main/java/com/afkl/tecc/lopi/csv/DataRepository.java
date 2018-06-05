package com.afkl.tecc.lopi.csv;

import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;

import java.nio.file.Paths;

public class DataRepository<T> {

    private final Resource resource;
    private final CsvFileParser<T> csvFileParser;
    private final CsvLineParser<T> csvLineParser;

    public DataRepository(Resource resource, CsvFileParser<T> csvFileParser, CsvLineParser<T> csvLineParser) {
        this.resource = resource;
        this.csvFileParser = csvFileParser;
        this.csvLineParser = csvLineParser;
    }

    public Flux<T> stream() {
        try {
            return csvFileParser.parse(Paths.get(resource.getURI()), csvLineParser).cache();
        } catch (Throwable t) {
            return Flux.error(t);
        }
    }

}
