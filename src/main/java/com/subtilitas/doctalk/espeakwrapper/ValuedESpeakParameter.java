package com.subtilitas.doctalk.espeakwrapper;


import java.util.Arrays;
import java.util.List;

class ValuedESpeakParameter<E> extends ESpeakParameter
{

    private final E parameter;

    ValuedESpeakParameter(ESpeakArgument argument, E parameter)
    {
        super(argument);
        this.parameter = parameter;
    }

    @Override
    List<String> write() {
        List<String> argumentList = super.write();
        argumentList.add(parameter.toString());
        return argumentList;
    }

    E getParameter()
    {
        return parameter;
    }
}
