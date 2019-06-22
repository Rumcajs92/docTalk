package com.subtilitas.doctalk.espeakwrapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ESpeak {

    private static final String E_SPEAK_DIR = "ESPEAK_DIR";
    private final Path eSpeakPath;

    private Set<String> voices;

    public ESpeakCommand command(ESpeakParameters eSpeakParameters) {
        return ESpeakCommand.build(eSpeakPath, eSpeakParameters);
    }

    public String getVoiceName(Locale locale) {

        return null;
    }

    public static ESpeak of() {

        String envVariable = System.getenv(E_SPEAK_DIR);
        Path eSpeakPath = Path.of(envVariable,"command_line","espeak.exe");
        if(Files.exists(eSpeakPath)) {
            return new ESpeak(eSpeakPath);
        }
        throw new IllegalStateException(String.format("There is no eSpeak directory with eSpeak executable under '%s' environment variable", E_SPEAK_DIR));
    }


    public Set<String> getVoices() {
        if(voices == null || voices.isEmpty()) {
            voices = readVoicesFromESpeak();
        }
        return voices;
    }

    @SneakyThrows
    private Set<String> readVoicesFromESpeak() {
        ESpeakParameters voicesParameter = ESpeakParameters.builder()
                .voices()
                .build();
        ESpeakCommand eSpeakCommand = command(voicesParameter);
        Process process = eSpeakCommand.run();
        process.waitFor();




        return null;
    }


}
