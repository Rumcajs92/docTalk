package com.subtilitas.doctalk.test;

import com.subtilitas.doctalk.test.factory.YamlProvidable;
import com.subtilitas.doctalk.test.factory.exception.NoConfigurationFileError;
import com.subtilitas.doctalk.test.factory.impl.Yamls;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class YamlConfigurationParameterResolver implements ParameterResolver, YamlProvidable {

    private static final String YAML_CONFIGURATION_FILE_NAME = "./test-properties.yml";

    private final String document = loadDocument();

    public YamlConfigurationParameterResolver() throws IOException {
    }


    private String loadDocument() throws IOException {
        Path configurationPath = Paths.get(YAML_CONFIGURATION_FILE_NAME);
        if(Files.exists(configurationPath)){
            return Files.readString(configurationPath);
        }
        //TODO delete
        Files.createFile(configurationPath);
        throw new NoConfigurationFileError("The configuration file could not be found");
    }

    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(YamlConfiguration.class);
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return yaml(Yamls.BASIC_YAML).load(document);
    }
}
