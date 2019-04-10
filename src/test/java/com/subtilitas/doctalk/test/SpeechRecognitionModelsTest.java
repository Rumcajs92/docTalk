package com.subtilitas.doctalk.test;

import edu.cmu.sphinx.api.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

@ExtendWith(YamlConfigurationParameterResolver.class)
public class SpeechRecognitionModelsTest {


    private static final Configuration CONFIGURATION = new Configuration();

    private final Map<String, String> yamlMap;

    public SpeechRecognitionModelsTest(@YamlConfiguration Map<String, String> yamlMap) {
        this.yamlMap = yamlMap;
    }

    @BeforeAll
    public static void setUp() {

    }

    @Test
    public void testSpeechRecognitionModels() {

        Assertions.assertEquals(1, 1);


    }
}
