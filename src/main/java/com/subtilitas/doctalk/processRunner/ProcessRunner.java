package com.subtilitas.doctalk.processRunner;

import com.google.common.base.Joiner;
import com.google.common.io.CharStreams;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessRunner {

    public static List<String> runProcess(List<String> commands) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
//        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        log.info(String.format("Running: %s", Joiner.on(" ").join(commands)));
        Process process = processBuilder.start();
        while (process.isAlive()) {}
        log.info(String.format("Finishing: %s", Joiner.on(" ").join(commands)));
        throwExceptionOnErrors(process);
        return getOutput(process);
    }

    private static List<String> getOutput(Process process) throws IOException {
        InputStream inputStream = process.getInputStream();
        String text = CharStreams.toString(new InputStreamReader(inputStream));
        return List.of(text.split("\n"));
    }

    private static void throwExceptionOnErrors(Process process) throws IOException {
        InputStream errorStream = process.getErrorStream();
        String text = CharStreams.toString(new InputStreamReader(errorStream));

        if(!text.isBlank()) {
            throw new RuntimeException(text);
        }
        if(process.exitValue() != 0) {
            throw new RuntimeException("Exit value: " + process.exitValue());
        }
    }
}
