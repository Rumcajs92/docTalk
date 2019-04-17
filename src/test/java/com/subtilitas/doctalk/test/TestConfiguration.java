package com.subtilitas.doctalk.test;

import edu.cmu.sphinx.api.Configuration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class TestConfiguration {

    private final Configuration configuration;

    private final String testFilesPath;

    TestConfiguration(MutableTestConfiguration mutableTestConfiguration) {
        this.configuration = mutableTestConfiguration.getConfiguration();
        this.testFilesPath = mutableTestConfiguration.getTestFilesPath();
    }
}
