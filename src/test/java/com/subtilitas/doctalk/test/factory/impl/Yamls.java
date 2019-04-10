package com.subtilitas.doctalk.test.factory.impl;

import com.subtilitas.doctalk.test.factory.FactoryItem;
import org.yaml.snakeyaml.Yaml;

import java.util.function.Supplier;

public enum Yamls implements FactoryItem<Yamls, Yaml> {

    BASIC_YAML;

    @Override
    public Yamls getEnumInstance() {
        return this;
    }

    @Override
    public Supplier<Yaml> getSupplier() {
        return Yaml::new;
    }
}
