package com.subtilitas.doctalk.g2pconverter.espeak;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.base.Joiner;
import com.google.common.io.CharStreams;
import com.subtilitas.doctalk.espeakwrapper.ESpeak;
import com.subtilitas.doctalk.espeakwrapper.ESpeakCommand;
import com.subtilitas.doctalk.espeakwrapper.ESpeakParameters;
import com.subtilitas.doctalk.g2pconverter.G2PConverter;
import io.vavr.Tuple3;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class ESpeakG2PConverter implements G2PConverter
{

    private final ESpeak eSpeak;

    private final PhonemeContext phonemeContext;


    @Override
    public Map<String, String> toPhonemes(List<String> wordsToConvert, Locale locale)  {
        String voiceName = eSpeak.getVoiceName(locale);
        LanguagePhonemeMapper mapper =  phonemeContext.getMapper(voiceName);
        Charset mapperCharset = mapper.getCharset();
        Path tempoFileToTranslate = getFileToTranslate(wordsToConvert, mapperCharset);
        List<String> convertedWords = convertWords(tempoFileToTranslate, voiceName);
        return IntStream.range(0, Math.min(wordsToConvert.size(), convertedWords.size()))
                .boxed()
                .map(i -> new Tuple3<>(wordsToConvert.get(i), convertedWords.get(i), mapper.map(convertedWords.get(i))))
                .peek(log::debug)
                .collect(Collectors.toMap(Tuple3::_1, Tuple3::_3, (x, y) -> y, LinkedHashMap::new));
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
