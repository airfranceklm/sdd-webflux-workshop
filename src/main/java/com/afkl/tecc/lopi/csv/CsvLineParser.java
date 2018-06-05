package com.afkl.tecc.lopi.csv;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CsvLineParser<T> {

    Mono<T> parse(String line);

}
