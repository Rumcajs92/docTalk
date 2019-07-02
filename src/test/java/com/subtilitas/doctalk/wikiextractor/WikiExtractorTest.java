package com.subtilitas.doctalk.wikiextractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.google.common.collect.ImmutableList;
import io.vavr.Tuple2;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Log4j2
public class WikiExtractorTest {

    private final String hostWiki = "pl.wikipedia.org";

    private final WikiExtractor wikiExtractor = new WikiExtractor(hostWiki, new ObjectMapper());

    @Test
    public void testExtracting() throws IOException, URISyntaxException, DiffException {

        //given
        List<Tuple2<String, Integer>> wikiPages = ImmutableList.of(
                new Tuple2<>("Planowanie_ciągłości_działania", 1)
        );

        //when
        List<String> extractedText = wikiExtractor.extract(wikiPages);

        //then
        List<String> expectedOutput = ImmutableList.of(
                Files.readString(Paths.get(getClass().getResource("expected-wiki-text-Planowanie_ciągłości_działania-1").toURI())));
        Assertions.assertEquals(expectedOutput, extractedText);

        Patch<String> patch = DiffUtils.diff(expectedOutput, extractedText);
        List<AbstractDelta<String>> deltas = patch.getDeltas();
        if(!deltas.isEmpty()) {
            deltas.forEach(log::error);
        }
        Assertions.assertEquals(expectedOutput,extractedText);
    }

}
