package com.subtilitas.doctalk.cmutoolkit;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NormalizedText {

    private final List<String> sentences;

    private List<String> formattedSentences;


    public NormalizedText(List<String> sentences) {
        this.sentences = ImmutableList.copyOf(sentences);
    }

    public List<String> getFormattedLines() {
        if(formattedSentences ==  null){
            formattedSentences = formatSentences();
        }
        return formattedSentences;
    }

    private List<String> formatSentences() {
        return sentences.stream()
                .map(s -> String.format("<s> %s </s>", s))
                .collect(Collectors.toList());
    }

    public NormalizedText closeText(Vocabulary vocabulary) {
        Set<String> words = vocabulary.getWords();
        List<String> closedSentences = sentences.stream()
                .filter(sentence -> words.stream().anyMatch(sentence::contains))
                .collect(Collectors.toList());
        return new NormalizedText(closedSentences);
    }


}
