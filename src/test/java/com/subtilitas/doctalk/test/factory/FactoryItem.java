package com.subtilitas.doctalk.test.factory;

import java.util.function.Supplier;

public interface FactoryItem<E extends Enum, T> {

    E getEnumInstance();

    Supplier<T> getSupplier();
}
