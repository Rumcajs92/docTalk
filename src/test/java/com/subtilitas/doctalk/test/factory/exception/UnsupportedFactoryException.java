package com.subtilitas.doctalk.test.factory.exception;

import com.subtilitas.doctalk.test.factory.Factory;

public class UnsupportedFactoryException extends RuntimeException {

    private Factory factory;

    public UnsupportedFactoryException(Factory factory) {
        this.factory = factory;
    }


}
