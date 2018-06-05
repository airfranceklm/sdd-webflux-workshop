package com.afkl.tecc.lopi.csv;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Files;
import java.nio.file.Path;

public interface CsvFileParser<T> {

    default Flux<T> parse(Path path, CsvLineParser<T> lineParser) {
        try {
            return Flux.fromStream(Files.lines(path))
                    .parallel()
                    .runOn(Schedulers.parallel())
                    .flatMap(lineParser::parse)
                    .sequential();
        } catch (Throwable t) {
            return Flux.error(t);
        }
    }

}
