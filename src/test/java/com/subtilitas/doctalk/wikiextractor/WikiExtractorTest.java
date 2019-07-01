package com.subtilitas.doctalk.wikiextractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.vavr.Tuple2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class WikiExtractorTest {

    private final String hostWiki = "pl.wikipedia.org";

    private final WikiExtractor wikiExtractor = new WikiExtractor(hostWiki, new ObjectMapper());

    @Test
    public void testExtracting() throws IOException, URISyntaxException {

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
    }

}
