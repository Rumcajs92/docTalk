package com.subtilitas.doctalk.espeakwrapper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Period;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ESpeakCommandTest
{

    private final Path eSpeakFile = Path.of("C:\\Users\\tharasim\\eSpeak\\command_line\\espeak.exe");


    @Test
    public void testRunningESpeak() throws IOException, InterruptedException
    {
        Path fileToSpeak = Files.createTempFile("testToFile", "");
        BufferedWriter bufferedWriter = Files.newBufferedWriter(fileToSpeak , Charset.forName("UTF-8"));
        //Charset.forName("ISO-8859-2")
        bufferedWriter.write("Hello World");
        bufferedWriter.flush();
        bufferedWriter.close();

        Path resultFile = Files.createTempFile("result", "");
        Path errorFile = Files.createTempFile("error", "");


        ESpeakParameters eSpeakParameters = ESpeakParameters.builder()
            .textFileToSpeak(fileToSpeak)
            .writePhonemes()
            .phonoutFile(resultFile)
            .build();

        ProcessBuilder processBuilder = new ProcessBuilder();
//        processBuilder.redirectOutput(resultFile.toFile());
        processBuilder.redirectError(errorFile.toFile());

        ESpeakCommand command = ESpeakCommand.build(eSpeakFile, eSpeakParameters);
        System.out.println(command);
        Process process = command.run(processBuilder);
        long startMillis = System.currentTimeMillis();
        System.out.println("Starting espeak process");
        while (process.isAlive()) { }
        long endMillis = System.currentTimeMillis();
        System.out.format("espeak process is finished. Duration in millis = %d\n", endMillis -startMillis);


        String result = Files.readString(resultFile);
        String error = Files.readString(errorFile);
        Assertions.assertEquals( "h@l'oU w'3:ld", result.trim());

    }
}
