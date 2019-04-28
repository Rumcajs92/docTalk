package com.subtilitas.doctalk.factory.exception;

import com.subtilitas.doctalk.factory.Factory;

public class UnsupportedFactoryException extends RuntimeException {

    private Factory factory;

    public UnsupportedFactoryException(Factory factory) {
        this.factory = factory;
    }


}
