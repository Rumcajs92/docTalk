package com.subtilitas.doctalk.adapter.service;

import com.subtilitas.doctalk.adapter.model.Transcription;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
@ToString
public class TranscriptionStatistics {

    @NonNull
    private final TranscriptionDiffStatistics diffStatistics;
    private final int wordCount;

    @Getter(lazy = true)
    private final float wordErrorRate = calculateErrorRate();

    private float calculateErrorRate() {
        return ((float)((diffStatistics.getInsertions() + diffStatistics.getDeletions() + diffStatistics.getSubstitutions()) * 100))/((float)wordCount);
    }

    @Getter(lazy = true)
    private final float accuracy = calculateAccuracy();

    private float calculateAccuracy() {
        return ((float)((wordCount - diffStatistics.getDeletions() - diffStatistics.getSubstitutions()) * 100))/((float)wordCount);
    }

    public static TranscriptionDiffStatistics emptyDiff() {
        return new TranscriptionDiffStatistics(0,0,0);
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    static class TranscriptionDiffStatistics {


        private final int insertions;
        private final int deletions;
        private final int substitutions;
    }
}
