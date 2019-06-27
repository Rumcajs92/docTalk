package com.subtilitas.doctalk.espeakwrapper;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ESpeakTest {

    private static final ESpeak eSpeak = ESpeak.of();

    @Test
    void getVoiceName() {
        //given
        Locale polishLocale = Locale.forLanguageTag("pl-PL");

        //when
        String returnedVoice = eSpeak.getVoiceName(polishLocale);

        //then
        Assertions.assertEquals(returnedVoice, "pl");
    }


    @Test
    void getVoices() {
        //given

        //when
        Set<String> voices = eSpeak.getVoices();

        //then
        Set<String> expectedVoices = ImmutableSet.of(
                "af", "am", "an","as","az","bg","bn","bs","ca","cs","cy","da","de","el","en","en-gb","en-sc","en-uk-north",
                "en-uk-rp", "en-uk-wmids","en-us","en-wi","eo","es","es-la","et","eu","fa","fa-pin","fi","fr-be","fr-fr",
                "ga","gd", "grc","gu","hi","hr","hu","hy","hy-west","id","is","it","jbo","ka","kl","kn","ko","ku","la",
                "lfn","lt","lv","mk", "ml","ms","nci","ne","nl","no","or","pa","pap","pl","pt-br","pt-pt","ro","ru","si",
                "sk","sl","sq","sr", "sv","sw", "ta", "te","tr","ur","vi","vi-hue","vi-sgn","zh","zh-yue"
        );
        Assertions.assertEquals(expectedVoices, voices);
    }

}