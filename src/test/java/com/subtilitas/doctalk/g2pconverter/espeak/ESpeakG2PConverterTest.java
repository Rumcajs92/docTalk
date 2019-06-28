package com.subtilitas.doctalk.g2pconverter.espeak;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.google.common.collect.Lists;
import com.subtilitas.doctalk.FileComparer;
import com.subtilitas.doctalk.espeakwrapper.ESpeak;
import io.vavr.Tuple2;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
class ESpeakG2PConverterTest  {

    private final ESpeak eSpeak = ESpeak.of();

    private ESpeakG2PConverter eSpeakG2PConverter = new ESpeakG2PConverter(eSpeak, new PhonemeContext());

    @Test
    public void toPhonemesPolish() throws URISyntaxException, IOException, DiffException {

        //given
        Path inputFile = Paths.get(getClass().getResource("input-graphonemes").toURI());
        List<String> input = Files.readString(inputFile).lines().collect(Collectors.toList());

        //when
        Map<String, String> output = eSpeakG2PConverter.toPhonemes(input, Locale.forLanguageTag("pl-PL"));

        //then
        Path expectedResultFile = Paths.get(getClass().getResource("expected-phonemes").toURI());
        List<String> expectedConvertedOutput = Files.lines(expectedResultFile).collect(Collectors.toList());

        Map<String, String> expectedOutput = IntStream.range(0, Math.max(input.size(), expectedConvertedOutput.size()))
                .boxed()
                .collect(Collectors.toMap(input::get, expectedConvertedOutput::get, (x, y) -> y, LinkedHashMap::new));

        List<Map.Entry> expectedOutputEntryList = new ArrayList<>(expectedOutput.entrySet());
        List<Map.Entry> expectedInputEntryList = new ArrayList<>(output.entrySet());
        Patch<Map.Entry> patch = DiffUtils.diff(expectedOutputEntryList, expectedInputEntryList);
        List<AbstractDelta<Map.Entry>> deltas = patch.getDeltas();
        deltas.forEach(log::error);
        Assertions.assertTrue(deltas.isEmpty());
        Assertions.assertEquals(expectedOutput,output);

    }


}