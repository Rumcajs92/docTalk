package com.subtilitas.doctalk.test;


import edu.cmu.sphinx.api.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MutableTestConfiguration {

    private Configuration configuration;

    private String testFilesPath;

    public TestConfiguration immutalize() {
        return new TestConfiguration(this);
    }
}
