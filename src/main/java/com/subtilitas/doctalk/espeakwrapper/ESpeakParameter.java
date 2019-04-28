package com.subtilitas.doctalk.espeakwrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ESpeakParameter
{

    private final ESpeakArgument eSpeakArgument;

    List<String> write()
    {
        List<String> list = new LinkedList<>();
        list.add(eSpeakArgument.getArgumentCharacter());
        return list;
    }

    ESpeakArgument getESpeakArgument() {
        return eSpeakArgument;
    }
}
