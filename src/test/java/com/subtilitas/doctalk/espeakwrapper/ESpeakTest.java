package com.subtilitas.doctalk.espeakwrapper;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ESpeakTest {

    private static final ESpeak eSpeak = ESpeak.of();

    @Test
    void getVoiceName() {
        //given
        Locale polishLocale = Locale.forLanguageTag("pl");

        //when
        String returnedVoice = eSpeak.getVoiceName(polishLocale);

    }
}