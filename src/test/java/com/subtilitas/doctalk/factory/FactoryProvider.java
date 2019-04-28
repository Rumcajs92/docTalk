package com.subtilitas.doctalk.factory;

import com.subtilitas.doctalk.factory.exception.UnsupportedFactoryException;
import com.subtilitas.doctalk.factory.impl.YamlFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class FactoryProvider {

    private FactoryProvider() {}

    private static final Map<Factory, Supplier<AbstractFactory<?>>> FACTORY_SUPPLIER_MAP = Collections.unmodifiableMap(getFactoryMap());

    private static Map<Factory, Supplier<AbstractFactory<?>>> getFactoryMap() {
        Map<Factory, Supplier<AbstractFactory<?>>> factorySupplierMap = new HashMap<>(1);
        factorySupplierMap.put(Factory.YAMLS, FactoryProvider::getYamlFactory);
        return factorySupplierMap;
    }

    private static AbstractFactory<?> getYamlFactory() {
        return new YamlFactory();
    }

    public  static AbstractFactory<?> getFactory(Factory factory) {
        if(FACTORY_SUPPLIER_MAP.containsKey(factory)) {
            Supplier<AbstractFactory<?>> factorySupplier = FACTORY_SUPPLIER_MAP.get(factory);
            return factorySupplier.get();
        }
        throw new UnsupportedFactoryException(factory);
    }

}
