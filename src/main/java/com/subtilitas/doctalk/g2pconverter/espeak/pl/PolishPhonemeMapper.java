package com.subtilitas.doctalk.g2pconverter.espeak.pl;

import com.subtilitas.doctalk.g2pconverter.espeak.LanguagePhonemeMapper;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
public class PolishPhonemeMapper implements LanguagePhonemeMapper {

    @Override
    public String map(final String word) {
        if (word.contains(" ")) {
            throw new IllegalArgumentException(String.format("The string: '%s' is not a single word", word));
        }
        List<PolishESpeak2SphinxPhonemeMapping> mappings = getPhonemeMappings(word);
        return joinPhonemes(mappings);
    }

    @Override
    public Charset getCharset() {
        return Charset.forName("ISO-8859-2");
    }

    private List<PolishESpeak2SphinxPhonemeMapping> getPhonemeMappings(String operatingWord) {
        List<PolishESpeak2SphinxPhonemeMapping> mappings = new ArrayList<>();
        while (!operatingWord.isEmpty()) {
            PolishESpeak2SphinxPhonemeMapping firstMapping = getFirstMapping(operatingWord);
            operatingWord = operatingWord.substring(firstMapping.getESpeakSignLength());
            mappings.add(firstMapping);
        }

        return mappings;
    }

    private String joinPhonemes(List<PolishESpeak2SphinxPhonemeMapping> mappings) {
        return mappings.stream()
                .filter(PolishESpeak2SphinxPhonemeMapping::isPhoneme)
                .map(PolishESpeak2SphinxPhonemeMapping::getSphinxPhoneme)
                .collect(Collectors.joining(" "));
    }

    private PolishESpeak2SphinxPhonemeMapping getFirstMapping(final String word) {
        int wordLength = word.length();
        int from = Math.min(wordLength, PolishESpeak2SphinxPhonemeMapping.MAX_MAPPING_KEY);
        return PolishESpeak2SphinxPhonemeMapping.LENGTH_SORTED_E_SPEAK_MAPPINGS.stream()
                .filter(mapping -> mapping.getESpeakSignLength() <= from)
                .filter(mapping -> word.substring(0, mapping.getESpeakSignLength()).equals(mapping.getESpeakSign()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("No value present for word '%s'", word)));
    }



}
