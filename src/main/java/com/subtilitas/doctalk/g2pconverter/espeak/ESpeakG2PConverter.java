package com.subtilitas.doctalk.g2pconverter.espeak;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Joiner;
import com.google.common.io.CharStreams;
import com.subtilitas.doctalk.espeakwrapper.ESpeak;
import com.subtilitas.doctalk.espeakwrapper.ESpeakCommand;
import com.subtilitas.doctalk.espeakwrapper.ESpeakParameters;
import com.subtilitas.doctalk.g2pconverter.G2PConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.io.Streams;
import org.eclipse.jgit.util.IO;

@Log4j2
@RequiredArgsConstructor
public class ESpeakG2PConverter implements G2PConverter
{

    private final ESpeak eSpeak;

    private final PhonemeContext phonemeContext;


    @Override
    public List<String> toPhonemes(List<String> wordsToConvert, Locale locale)  {
        String voiceName = eSpeak.getVoiceName(locale);
        LanguagePhonemeMapper mapper =  phonemeContext.getMapper(voiceName);


        Charset mapperCharset = mapper.getCharset();
        Path tempoFileToTranslate = getFileToTranslate(wordsToConvert, mapperCharset);
        List<String> convertedWords= convertWords(tempoFileToTranslate, voiceName);
        return convertedWords.stream()
                .map(mapper::map)
                .peek(log::debug)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private List<String> convertWords(Path temporaryFile, String voiceName)  {
        ESpeakParameters eSpeakParameters = ESpeakParameters.builder()
                .writePhonemes()
                .voiceName(voiceName)
                .quiet()
                .textFileToSpeak(temporaryFile)
                .build();
        ESpeakCommand eSpeakCommand = eSpeak.command(eSpeakParameters);
        log.debug(eSpeakCommand);
        Process process = eSpeakCommand.run();
        InputStream eSpeakInputStream = process.getInputStream();
        String readStream = CharStreams.toString(new InputStreamReader(eSpeakInputStream));
        return Stream.of(readStream.split(" "))
                .filter(str -> !str.isBlank())
                .map(str -> str.replaceAll(System.lineSeparator(), ""))
                .collect(Collectors.toList());

    }

    @SneakyThrows
    private Path getFileToTranslate(List<String> input, Charset charset) {
        Path fileToSpeak = Files.createTempFile(Thread.currentThread().getName(), "");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(fileToSpeak, charset)) {
            bufferedWriter.write(Joiner.on(" ").join(input));
            bufferedWriter.flush();
        }
        return fileToSpeak;
    }

}
