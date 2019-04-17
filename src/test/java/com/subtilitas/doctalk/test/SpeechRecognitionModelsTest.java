package com.subtilitas.doctalk.test;

import com.subtilitas.doctalk.test.factory.exception.NoConfigurationFileError;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ExtendWith(YamlConfigurationParameterResolver.class)
public class SpeechRecognitionModelsTest {


    private final Configuration configuration;

//    private final Map<String, String> yamlMap;

    public SpeechRecognitionModelsTest(@YamlConfiguration Configuration configuration) {
        this.configuration = configuration;
    }


    @Test
    public void testSpeechRecognitionModels(@YamlConfiguration String testFilesPath) throws IOException {
        Path pathToTestFilesDirectory = Paths.get(testFilesPath);
        if(!Files.exists(pathToTestFilesDirectory)) {
            Files.createDirectory(pathToTestFilesDirectory);
            throw new NoConfigurationFileError("The directory with tests do not exists");
        }

//        Stream<Path> testDataDirectories = Files.list(pathToTestFilesDirectory);

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);

        Path audioFile = pathToTestFilesDirectory.resolve("test.wav");
        Path transcript = pathToTestFilesDirectory.resolve("test-transcript.txt");

        recognizer.startRecognition(Files.newInputStream(audioFile));
        SpeechResult result = recognizer.getResult();
        recognizer.stopRecognition();



    }
}
