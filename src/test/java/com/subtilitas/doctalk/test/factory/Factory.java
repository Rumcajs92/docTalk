package com.subtilitas.doctalk.test.factory;

import com.subtilitas.doctalk.test.factory.impl.YamlFactory;
import org.yaml.snakeyaml.Yaml;

public enum Factory {

    YAMLS(Yaml.class);

    private Class<?> createdObject;

    Factory(Class<?> createdObject) {
        this.createdObject = createdObject;
    }

    public Class<?> getCreatedClass() {
        return createdObject;
    }
}
