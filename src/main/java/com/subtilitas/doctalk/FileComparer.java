package com.subtilitas.doctalk;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public interface FileComparer {

    default void compareFiles(Path outputFile, Path expectedResultPath) throws IOException, DiffException {
        List<String> expectedResultStringList = Files.readAllLines(expectedResultPath);
        List<String> outputStringList = Files.readAllLines(outputFile);
        Patch<String> patch = DiffUtils.diff(expectedResultStringList, outputStringList);
        List<AbstractDelta<String>> deltas = patch.getDeltas();
        deltas.forEach(logger()::error);
        Assertions.assertTrue(deltas.isEmpty());
    }

    Logger logger();

}
