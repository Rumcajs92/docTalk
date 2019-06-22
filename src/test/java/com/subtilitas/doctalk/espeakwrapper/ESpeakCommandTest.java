package com.subtilitas.doctalk.espeakwrapper;

import com.google.common.io.CharStreams;
import io.vavr.Tuple2;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;


@Log4j2
public class ESpeakCommandTest {

    private static final ESpeak eSpeak = ESpeak.of();

    private static final String RESULT_FILE_NAME = "testToFile";

    private Path resultFile;

    private Path errorFile;

    private ProcessBuilder processBuilder;

    @BeforeEach
    public void setUp() throws IOException {
        resultFile = Files.createTempFile("result", "");
        errorFile = Files.createTempFile("error", "");

        processBuilder = new ProcessBuilder();
        processBuilder.redirectError(errorFile.toFile());
    }

    @Test
    public void testRunningWithoutFile() throws IOException, InterruptedException {
        //given
        String hello = "Hello world";
        ESpeakParameters eSpeakParameter = ESpeakParameters.builder()
                .quiet()
                .writePhonemes()
                .words(hello)
                .build();
        ESpeakCommand command = eSpeak.command(eSpeakParameter);

        //when
        Process eSpeakProcess = command.run(processBuilder);
        eSpeakProcess.waitFor();

        //then
        InputStream eSpeakInputStream = eSpeakProcess.getInputStream();
        String text = CharStreams.toString(new InputStreamReader(eSpeakInputStream));
        Assertions.assertEquals(" h@l'oU w'3:ld\r\n", text);
    }


    @Test
    public void testRunningESpeak() throws IOException, InterruptedException {

        //given
        String hello_world = "Hello World";
        Path fileToSpeak = getFileToTranslate(hello_world, "UTF-8");
        ESpeakParameters eSpeakParameters = ESpeakParameters.builder()
                .textFileToSpeak(fileToSpeak)
                .writePhonemes()
                .quiet()
                .phonoutFile(resultFile)
                .build();
        ESpeakCommand command = eSpeak.command(eSpeakParameters);

       //when
        runProcessAndLogTime(command);

        //then
        Tuple2<String, String> results = getResults();
        Assertions.assertEquals(results._2, "");
        Assertions.assertEquals("h@l'oU w'3:ld", results._1.trim());
    }

    @Test
    public void testPolishRun() throws IOException, InterruptedException {

        //given
        String hello_world = "Dzień dobry";
        Path fileToSpeak = getFileToTranslate(hello_world, "ISO-8859-2");
        ESpeakParameters eSpeakParameters = ESpeakParameters.builder()
                .textFileToSpeak(fileToSpeak)
                .writePhonemes()
                .voiceName("pl")
                .phonoutFile(resultFile)
                .build();
        ESpeakCommand command = eSpeak.command(eSpeakParameters);

        //when
        runProcessAndLogTime(command);

        //then
        Tuple2<String, String> results = getResults();
        Assertions.assertEquals(results._2, "");
        Assertions.assertEquals("dz;'En^ d'ObRy", results._1.trim());
    }




    private Tuple2<String ,String> getResults() throws IOException {
        String result = Files.readString(resultFile);
        String error = Files.readString(errorFile);
        return new Tuple2<>(result, error);
    }

    private void runProcessAndLogTime(ESpeakCommand command) throws IOException, InterruptedException {
        log.info("Starting eSpeak process");
        log.info("{}", command);
        LocalDateTime start = LocalDateTime.now();
        Process process = command.run(processBuilder);
        process.waitFor();
        long processDuration = Duration.between(start, LocalDateTime.now()).toMillis();
        log.info("espeak process is finished. Duration in millis = {}", processDuration);
    }


    private Path getFileToTranslate(String input, String charsetName) throws IOException {
        Path fileToSpeak = Files.createTempFile(RESULT_FILE_NAME, "");
        BufferedWriter bufferedWriter = Files.newBufferedWriter(fileToSpeak, Charset.forName(charsetName));
        bufferedWriter.write(input);
        bufferedWriter.flush();
        bufferedWriter.close();
        return fileToSpeak;
    }
}
