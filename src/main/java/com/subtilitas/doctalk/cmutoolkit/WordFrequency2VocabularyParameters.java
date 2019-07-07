package com.subtilitas.doctalk.cmutoolkit;

import com.subtilitas.doctalk.CommandBuilder;
import lombok.Builder;

@Builder
public class WordFrequency2VocabularyParameters implements CommandBuilder {

    @CommandValue("-")
    private Integer top;

    @CommandValue("-")
    private Integer gt;

    @CommandValue("-")
    private final Integer records;

    @CommandValue("-")
    private final Integer verbosity;

    public WordFrequency2VocabularyParameters(Integer top, Integer gt, Integer records, Integer verbosity) {
        if(gt != null && top != null) {
            throw new IllegalArgumentException("'gt' and 'top' cannot be set both");
        }
        this.top = top;
        this.gt = gt;
        this.records = records;
        this.verbosity = verbosity;
    }
}
