package com.subtilitas.doctalk.factory;

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
