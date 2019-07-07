package com.subtilitas.doctalk.cmutoolkit;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.subtilitas.doctalk.ClassResourceReader;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

@Log4j2
class CMUToolkitTest implements ClassResourceReader {

    private CMUToolkit cmuToolkit = new CMUToolkit(Paths.get("C:\\Users\\tobia\\sphninx\\cmuclmtk"));

    @Test
    public void testNormalizingString() throws URISyntaxException, IOException, DiffException {
        //given
         String textToNormalize = readResource("input-text-to-normalize");

         //when
        NormalizedText normalizedText = cmuToolkit.normalizeText(textToNormalize);

        //then
        List<String> expectedOutput = readResourceLines("expected-normalized-text");

        Patch<String> patch = DiffUtils.diff(expectedOutput,  normalizedText.getFormattedLines());
        List<AbstractDelta<String>> deltas = patch.getDeltas();
        if(!deltas.isEmpty()) {
            deltas.forEach(log::error);
        }
        Assertions.assertEquals(expectedOutput, normalizedText.getFormattedLines());

    }

    @Test
    void text2wordFrequency() throws IOException, URISyntaxException {
        //given
        String normalizedInput = readResource("expected-normalized-text");
        Text2WordFrequencyParameters parameters = Text2WordFrequencyParameters.builder()
                .verbosity(4)
                .build();

        //when
        WordFrequency wordFrequency = cmuToolkit.text2wordFrequency(normalizedInput, parameters);

        //then
        String output = wordFrequency.getWordFrequencyText();
        String expectedOutput = readResource("expected-word-frequency");
        Assertions.assertEquals(expectedOutput, output);

    }

    @Test
    void wordFrequency2Vocabulary() throws IOException, URISyntaxException {

        //given
        String wordFrequencyText = readResource("expected-word-frequency");
        WordFrequency wordFrequency = new WordFrequency(wordFrequencyText);
        WordFrequency2VocabularyParameters parameters = WordFrequency2VocabularyParameters.builder()
                .verbosity(2)
                .build();

        //when
        Vocabulary vocabulary = cmuToolkit.wordFrequency2Vocabulary(wordFrequency, parameters);

        //then
        String expectedText = readResource("expected-vocabulary-file");
        Assertions.assertEquals(expectedText, vocabulary.getVocabularyFileText());
    }
}