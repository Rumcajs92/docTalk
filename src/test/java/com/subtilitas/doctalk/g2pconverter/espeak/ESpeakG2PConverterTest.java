package com.subtilitas.doctalk.g2pconverter.espeak;

import com.github.difflib.algorithm.DiffException;
import com.subtilitas.doctalk.FileComparer;
import com.subtilitas.doctalk.espeakwrapper.ESpeak;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
class ESpeakG2PConverterTest implements FileComparer {

    private static final ESpeak eSpeak = new ESpeak(Path.of("C:\\Program Files (x86)\\eSpeak\\command_line\\espeak.exe"));

    private ESpeakG2PConverter eSpeakG2PConverter = new ESpeakG2PConverter(eSpeak);

    @Test
    public void toPhonemes() throws URISyntaxException, IOException, DiffException {

        //given
        Path inputFile = Paths.get(getClass().getResource("input-graphonemes").toURI());

        //when
        Path outputFile = eSpeakG2PConverter.toPhonemes(inputFile, );

        //then
        Path expectedResultPath = Paths.get(getClass().getResource("expected-phonemes").toURI());

        compareFiles(outputFile, expectedResultPath);
    }


    @Override
    public Logger logger() {
        return log;
    }
}