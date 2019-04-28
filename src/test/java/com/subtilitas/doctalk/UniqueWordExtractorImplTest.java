package com.subtilitas.doctalk;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.subtilitas.doctalk.wordextractor.UniqueWordExtractorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(YamlConfigurationParameterResolver.class)
class UniqueWordExtractorImplTest
{

    private final UniqueWordExtractorImpl uniqueWordExtractor = new UniqueWordExtractorImpl();

    @Test
    void extractUniqueWords() throws IOException
    {
        Path pathToExtractUniqueWords = Paths.get("C:\\Users\\tharasim\\IdeaProjects\\docTalk\\src\\resources\\transcirpt-example");
        Path writtenFile = uniqueWordExtractor.extractUniqueWords(pathToExtractUniqueWords);
    }
}