package com.subtilitas.doctalk.adapter.service;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TranscriptionStatisticsCalculator {

    @SneakyThrows
    public static TranscriptionStatistics calculateTranscriptionStatistics(List<String> outputLines, List<String> expectedResultLines)  {
        if(expectedResultLines.isEmpty()) {
            throw new IllegalArgumentException("Expected result should not be empty");
        }
        Patch<String> patch = DiffUtils.diff(expectedResultLines, outputLines);
        List<AbstractDelta<String>> deltas = patch.getDeltas();
        final int wordCount = expectedResultLines.size();
        return deltas.stream()
                .map(TranscriptionStatisticsCalculator::calculateDeltaStatistics)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.reducing(TranscriptionStatisticsCalculator::reduceTranscriptionStatistics),
                                (statistics) -> new TranscriptionStatistics(statistics.orElse(TranscriptionStatistics.emptyDiff()), wordCount)));
    }

    private static TranscriptionStatistics.TranscriptionDiffStatistics calculateDeltaStatistics(AbstractDelta<String> del) {
        int sourceSize = del.getSource().size();
        int targetSize = del.getTarget().size();
        int diff = Math.abs( sourceSize - targetSize);
        int deletions = targetSize < sourceSize ? diff : 0;
        int insertions = targetSize > sourceSize ? diff : 0;
        int substitutions =Math.min(sourceSize, targetSize);
        return new TranscriptionStatistics.TranscriptionDiffStatistics(insertions, deletions, substitutions);
    }

    private static TranscriptionStatistics.TranscriptionDiffStatistics reduceTranscriptionStatistics(TranscriptionStatistics.TranscriptionDiffStatistics stat1, TranscriptionStatistics.TranscriptionDiffStatistics stat2) {
        return new TranscriptionStatistics.TranscriptionDiffStatistics(stat1.getInsertions() + stat2.getInsertions(), stat1.getDeletions() + stat2.getDeletions(), stat1.getSubstitutions() + stat2.getSubstitutions());
    }

}
