package com.subtilitas.doctalk.cmutoolkit;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.google.common.io.CharStreams;
import com.subtilitas.doctalk.CommandBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CMUToolkit {


    private final Path pathToToolkit;

    private final Set<String> splitters = ImmutableSet.of(",", ".", "\n", System.lineSeparator());

    public NormalizedText normalizeText(String textToNormalize) {
        List<String> sentences = Stream.of(textToNormalize.split(getSplittersRegex()))
                .map(str -> str.replaceAll("[^\\p{IsAlphabetic}|(\\p{Space})]", ""))
                .filter(str -> str.split(" ").length > 3)
                .map(str-> str.replaceAll("\\p{Space}{2,}+", " "))
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        return new NormalizedText(sentences);
    }

    private String getSplittersRegex() {
        return splitters
                .stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));
    }


    @SneakyThrows
    public WordFrequency text2wordFrequency(String text, Text2WordFrequencyParameters parameters) {
        List<String> commands = Lists.newArrayList();
        commands.add(getProgramPath(TOOLKIT_PROGRAMS.TEXT_2_WORD_FREQUENCY));
        commands.addAll(parameters.getCommands());
        Process process = startProcessWithRedirectInput(text, commands);
        processError(process);
        String readStream = readInputStream(process);

        return new WordFrequency(readStream);
    }

    private String getProgramPath(TOOLKIT_PROGRAMS toolkit_programs) {
        return pathToToolkit.resolve(toolkit_programs.getName()).toAbsolutePath().toString();
    }

    private Process startProcessWithRedirectInput(String text, List<String> commands) throws IOException {
        Path temporaryTextFile = Files.createTempFile(Thread.currentThread().getName() + "." ,  "." + LocalDate.now().toString());
        Files.writeString(temporaryTextFile, text);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commands);
        processBuilder.redirectInput(temporaryTextFile.toFile());
        return processBuilder.start();
    }

    private String readInputStream(Process process) throws IOException {
        InputStream outputStream = process.getInputStream();
        return CharStreams.toString(new InputStreamReader(outputStream));
    }

    private void processError(Process process) throws IOException {
        InputStream errorStream = process.getErrorStream();
        String errorText = CharStreams.toString(new InputStreamReader(errorStream));
        if(StringUtils.containsNone(errorText, "Done")) {
            throw new RuntimeException(errorText);
        }
    }

    @Getter
    @RequiredArgsConstructor
    private enum TOOLKIT_PROGRAMS {

        TEXT_2_WORD_FREQUENCY("text2wfreq.exe");

        private final String name;

    }
}
