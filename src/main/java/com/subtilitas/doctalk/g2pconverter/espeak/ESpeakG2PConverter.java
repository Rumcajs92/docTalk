package com.subtilitas.doctalk.g2pconverter.espeak;

import java.util.Locale;

import com.subtilitas.doctalk.espeakwrapper.ESpeak;
import com.subtilitas.doctalk.espeakwrapper.ESpeakParameters;
import com.subtilitas.doctalk.g2pconverter.G2PConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ESpeakG2PConverter implements G2PConverter
{

    private final ESpeak eSpeak;


    @Override
    public String toPhonemes(String wordsToConvert, Locale locale) {

        ESpeakParameters eSpeakParameters = ESpeakParameters.builder()
                .writePhonemes()
                .voiceName(eSpeak.getVoiceName(locale))
                .quiet()
                .words(wordsToConvert)
                .build();

        return null;
    }

}
