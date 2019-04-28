package com.subtilitas.doctalk.factory.impl;

import com.subtilitas.doctalk.factory.FactoryItem;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.security.auth.login.Configuration;
import java.util.function.Supplier;

public enum Yamls implements FactoryItem<Yamls, Yaml> {

    BASIC(Yaml::new),
    CONFIGURATION(() -> new Yaml(new Constructor(Configuration.class)))
    ;

    private Supplier<Yaml> supplier;

    Yamls(Supplier<Yaml> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Yamls getEnumInstance() {
        return this;
    }

    @Override
    public Supplier<Yaml> getSupplier() {
        return supplier;
    }
}
