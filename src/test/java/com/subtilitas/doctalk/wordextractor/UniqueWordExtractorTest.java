package com.subtilitas.doctalk.wordextractor;

import com.github.difflib.algorithm.DiffException;
import com.subtilitas.doctalk.TextComparer;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
class UniqueWordExtractorTest implements TextComparer {

    private final UniqueWordExtractor uniqueWordExtractor = new UniqueWordExtractor();

    private Path writtenFile;

    @Test
    void extractUniqueWords() throws IOException, URISyntaxException, DiffException {

        //given
        Path inputFile = Paths.get(getClass().getResource("input-transcript").toURI());

        //when
        writtenFile = uniqueWordExtractor.extractUniqueWords(inputFile);

        //then
        Path expectedResultPath = Paths.get(getClass().getResource("exptected-transcript").toURI());
        compareFiles(writtenFile, expectedResultPath);

    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.delete(writtenFile);
    }


    @Override
    public Logger logger() {
        return log;
    }
}