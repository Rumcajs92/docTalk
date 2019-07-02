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
import java.util.List;

@Log4j2
class CMUToolkitTest implements ClassResourceReader {

    @Test
    public void testNormalizingString() throws URISyntaxException, IOException, DiffException {
        //given
         String textToNormalize = readResource("input-text-to-normalize");

         //when
        CMUToolkit cmuToolkit = new CMUToolkit();
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
}