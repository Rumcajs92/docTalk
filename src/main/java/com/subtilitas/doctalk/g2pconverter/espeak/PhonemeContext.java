package com.subtilitas.doctalk.g2pconverter.espeak;

import com.google.common.collect.ImmutableMap;
import com.subtilitas.doctalk.g2pconverter.espeak.pl.PolishPhonemeMapper;

import java.util.Map;

public class PhonemeContext {

    private final Map<String, LanguagePhonemeMapper> MAPPERS = ImmutableMap.<String, LanguagePhonemeMapper>builder()
            .put("pl", new PolishPhonemeMapper())
            .build();

    public LanguagePhonemeMapper getMapper(String voice) {
        if(MAPPERS .containsKey(voice)) {
            return MAPPERS.get(voice);
        }
        throw new UnsupportedOperationException(String.format("Voice: '%s' is not supported.", voice));
    }

}
