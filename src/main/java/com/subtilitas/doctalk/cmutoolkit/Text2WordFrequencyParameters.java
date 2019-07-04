package com.subtilitas.doctalk.cmutoolkit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.subtilitas.doctalk.CommandBuilder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Builder
public class Text2WordFrequencyParameters implements CommandBuilder {

    @CommandValue("-")
    private final Integer verbosity;

    @CommandValue("-")
    private final Integer hash;



}
