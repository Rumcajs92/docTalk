package com.subtilitas.doctalk.cmutoolkit;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CMUToolkit {


    private final Set<String> splitters = ImmutableSet.of(",", ".", "\n", System.lineSeparator());

    public NormalizedText normalizeText(String textToNormalize) {
        List<String> sentences = Stream.of(textToNormalize.split(getSplittersRegex()))
                .map(str -> str.replaceAll("[^\\p{IsAlphabetic}|(\\p{Space})]", ""))
                .filter(str -> str.split(" ").length > 3)
                .map(str-> str.replaceAll("\\p{Space}{2,}+", " "))
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        return new NormalizedText(sentences);
    }

    String getSplittersRegex() {
        return splitters
                .stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));
    }
}
