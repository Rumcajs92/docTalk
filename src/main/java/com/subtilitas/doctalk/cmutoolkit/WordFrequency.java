package com.subtilitas.doctalk.cmutoolkit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class WordFrequency {

    private final String wordFrequencyText;

    private Map<String, Integer> wordFrequencyMap;

    private Map<String, Integer> getWordFrequencyMap() {
        if(wordFrequencyMap == null) {
            String [] splitWordFrequencyText = wordFrequencyText.split(System.lineSeparator());
            wordFrequencyMap = ImmutableMap.copyOf(Stream.of(splitWordFrequencyText)
                    .collect(
                            Collectors.toMap(
                                    str -> str.split("\\s")[0],
                                    str -> Integer.valueOf(str.split(" ")[1]))));
        }
        return wordFrequencyMap;

    }


}
