package com.subtilitas.doctalk.cmutoolkit;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
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
                .map(this::deleteAllNonAlphabeticExceptSpace)
                .filter(this::filterShorterThan3)
                .map(this::keepWhitespaceToOneCharacter)
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        return new NormalizedText(sentences);
    }

    private String keepWhitespaceToOneCharacter(String str) {
        return str.replaceAll("\\p{Space}{2,}+", " ");
    }

    private boolean filterShorterThan3(String str) {
        return str.split(" ").length > 3;
    }

    private String deleteAllNonAlphabeticExceptSpace(String str) {
        return str.replaceAll("[^\\p{IsAlphabetic}|(\\p{Space})]", "");
    }

    private String getSplittersRegex() {
        return splitters
                .stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));
    }


    public WordFrequency text2wordFrequency(String text, Text2WordFrequencyParameters parameters) {
        String readStream = pipeTextToCommand(text, TOOLKIT_PROGRAMS.TEXT_2_WORD_FREQUENCY, parameters);
        return new WordFrequency(readStream);
    }

    @SneakyThrows
    private String pipeTextToCommand(String text, TOOLKIT_PROGRAMS program, CommandBuilder parameters) {
        List<String> commands = Lists.newArrayList();
        commands.add(getProgramPath(program));
        commands.addAll(parameters.getCommands());
        Process process = startProcessWithRedirectInput(text, commands);
        processError(process);
        return readInputStream(process);
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

    public Vocabulary wordFrequency2Vocabulary(WordFrequency wordFrequency, WordFrequency2VocabularyParameters parameters) {
        String vocabularyTextFile = pipeTextToCommand(wordFrequency.getWordFrequencyText(), TOOLKIT_PROGRAMS.WORD_FREQUENCY_2_VOCAB, parameters);
        return new Vocabulary(vocabularyTextFile);
    }



    @Getter
    @RequiredArgsConstructor
    private enum TOOLKIT_PROGRAMS {

        TEXT_2_WORD_FREQUENCY("text2wfreq.exe"),
        WORD_FREQUENCY_2_VOCAB("wfreq2vocab.exe"),
        TEXT_2_ID_NGRAM("text2idngram.exe");

        private final String name;

    }
}
