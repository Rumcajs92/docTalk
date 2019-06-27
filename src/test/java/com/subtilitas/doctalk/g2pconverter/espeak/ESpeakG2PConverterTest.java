package com.subtilitas.doctalk.g2pconverter.espeak;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.subtilitas.doctalk.FileComparer;
import com.subtilitas.doctalk.espeakwrapper.ESpeak;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Log4j2
class ESpeakG2PConverterTest  {

    private final ESpeak eSpeak = ESpeak.of();

    private ESpeakG2PConverter eSpeakG2PConverter = new ESpeakG2PConverter(eSpeak, new PhonemeContext());

    @Test
    public void toPhonemesPolish() throws URISyntaxException, IOException, DiffException {

        //given
        Path inputFile = Paths.get(getClass().getResource("input-graphonemes").toURI());
        List<String> input = Files.readString(inputFile, Charset.forName("ISO-8859-2")).lines().collect(Collectors.toList());

        //when
        List<String> output = eSpeakG2PConverter.toPhonemes(input, Locale.forLanguageTag("pl-PL"));

        //then
        Path expectedResultFile = Paths.get(getClass().getResource("expected-phonemes").toURI());
        List<String> expectedOutput = Files.lines(expectedResultFile).collect(Collectors.toList());

        Patch<String> patch = DiffUtils.diff(expectedOutput, output);
        List<AbstractDelta<String>> deltas = patch.getDeltas();
        deltas.forEach(log::error);
        Assertions.assertTrue(deltas.isEmpty());

        Assertions.assertEquals(expectedOutput,output);

    }


}