package com.subtilitas.doctalk.espeakwrapper;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FormattedValuedESpeakParameter<E> extends ValuedESpeakParameter<E>
{
    @Override
    List<String> write()
    {
        List<String> parameters = super.write();
        return Collections.singletonList(
            String.format(parameters.get(0), parameters.subList(1, parameters.size()).toArray()));
    }

    FormattedValuedESpeakParameter(ESpeakArgument argument, E parameter)
    {
        super(argument, parameter);
    }
}
