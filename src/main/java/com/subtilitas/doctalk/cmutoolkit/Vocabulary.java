package com.subtilitas.doctalk.cmutoolkit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public class Vocabulary {

    private final String vocabularyFileText;

    private Set<String> words;

    Set<String> getWords() {
        if(words== null) {
            words = extractWords();
        }
        return null;
    }

    private Set<String> extractWords() {
        String [] lines = vocabularyFileText.split(System.lineSeparator());
        return Stream.of(lines)
                .filter(line -> !line.startsWith("#"))
                .collect(Collectors.toSet());
    }

}
