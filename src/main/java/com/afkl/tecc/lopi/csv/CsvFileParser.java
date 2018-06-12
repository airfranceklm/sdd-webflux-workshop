package com.afkl.tecc.lopi.csv;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public interface CsvFileParser<T> {

    default Flux<T> read(InputStream io, CsvLineParser<T> lineParser) {
        return Flux.<String>create((sink) -> {
            try {
                var reader = new BufferedReader(new InputStreamReader(io));
                String line;
                while ((line = reader.readLine()) != null) {
                    sink.next(line);
                }
                sink.complete();
            } catch (Throwable t) {
                sink.error(t);
            }
        }).parallel().runOn(Schedulers.parallel())
                .flatMap(lineParser::parse)
                .sequential();
    }

}
