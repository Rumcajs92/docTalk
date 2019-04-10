package com.subtilitas.doctalk.test.factory;

import java.util.function.Supplier;

public interface AbstractFactory<E>{

    default E create(FactoryItem<? extends Enum, E> factoryItem) {
        Supplier<E> supplier = factoryItem.getSupplier();
        return supplier.get();
    }

}
