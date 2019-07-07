package com.subtilitas.doctalk.cmutoolkit;

import com.subtilitas.doctalk.CommandBuilder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class Text2WordFrequencyParameters implements CommandBuilder {

    @CommandValue("-")
    private final Integer verbosity;

    @CommandValue("-")
    private final Integer hash;



}
