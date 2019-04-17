package com.subtilitas.doctalk.test;

import com.subtilitas.doctalk.test.factory.YamlProvidable;
import com.subtilitas.doctalk.test.factory.exception.ConfigurationClassHasNoGetter;
import com.subtilitas.doctalk.test.factory.exception.NoConfigurationFileError;
import com.subtilitas.doctalk.test.factory.exception.UnknownConfigurationClass;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

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
        Parameter parameter = parameterContext.getParameter();
        Class<?> yamlClass = parameter.getType();
        Constructor constructor = new Constructor(MutableTestConfiguration.class);
        Yaml yaml = new Yaml(constructor);
        MutableTestConfiguration mutableTestConfiguration = yaml.load(document);
        TestConfiguration immutableTestConfiguration = mutableTestConfiguration.immutalize();
        return returnDesiredObject(yamlClass, immutableTestConfiguration);
    }

    private Object returnDesiredObject(Class<?> yamlClass, TestConfiguration testConfiguration) {
        Field fieldToGet = getDesiredField(yamlClass);
        String accessorMethodName = getAccessorMethodName(fieldToGet);
        return invokeAccessor(testConfiguration, accessorMethodName);
    }

    private Object invokeAccessor(TestConfiguration testConfiguration, String upperCasedFirstLetterFieldName) {
        try {
            Method method = TestConfiguration.class.getMethod( upperCasedFirstLetterFieldName);
            return  method.invoke(testConfiguration);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ConfigurationClassHasNoGetter();
        }
    }

    private String getAccessorMethodName(Field fieldToGet) {
        String fieldName = fieldToGet.getName();
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private Field getDesiredField(Class<?> yamlClass) {
        Field[] testConfigurationFields = TestConfiguration.class.getDeclaredFields();
        return Stream.of(testConfigurationFields)
                .filter(field -> field.getGenericType().equals(yamlClass))
                .findFirst()
                .orElseThrow(UnknownConfigurationClass::new);
    }
}
