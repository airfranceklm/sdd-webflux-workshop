package com.afkl.tecc.lopi.csv;

import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.Function;

public class CsvLineReader {

    public static <T> Mono<T> read(String line, int bufferSize, Function<String[], T> adapter) {
        return split(line.toCharArray(), bufferSize).map(adapter);
    }

    private static Mono<String[]> split(char[] chars, int bufferSize) {
        var itemStartIndex = 0;
        var bufferIndex = 0;
        var buffer = new String[bufferSize];
        var encapsulated = false;
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case ',': {
                    if (!encapsulated) {
                        if (itemStartIndex == i - 1) {
                            if (chars[i - 1] == ',') {
                                buffer[bufferIndex] = null;
                            } else {
                                var character = Character.toString(chars[i - 1]);
                                buffer[bufferIndex] = character.equals("\"") ? null : character;
                            }
                            bufferIndex++;
                        } else {
                            if (itemStartIndex < (i)) {
                                if (chars[i - 1] == '"') {
                                    var start = bufferIndex == 0 ? itemStartIndex + 1 : itemStartIndex;
                                    buffer[bufferIndex] = getValue(new String(Arrays.copyOfRange(chars, start, i - 1)).trim());
                                    bufferIndex++;
                                } else {
                                    buffer[bufferIndex] = getValue(new String(Arrays.copyOfRange(chars, itemStartIndex, i)).trim());
                                    bufferIndex++;
                                }
                            }
                        }
                        if (isInRange(i + 1, chars)) {
                            if (chars[i + 1] == '"') {
                                encapsulated = true;
                                itemStartIndex = i + 2;
                            } else {
                                if (chars[i + 1] != ',') {
                                    itemStartIndex = i + 1;
                                } else {
                                    itemStartIndex = i;
                                }
                            }
                        }
                    }
                    break;
                }
                case '"': {
                    if (isInRange(i + 1, chars) && chars[i + 1] == ',') {
                        encapsulated = false;
                    }
                    break;
                }
            }
        }
        return Mono.just(buffer);
    }

    private static String getValue(String v) {
        return v == null || v.equals("\\N") ? null : v;
    }

    private static boolean isInRange(int i, char[] chars) {
        return i < chars.length;
    }

}
