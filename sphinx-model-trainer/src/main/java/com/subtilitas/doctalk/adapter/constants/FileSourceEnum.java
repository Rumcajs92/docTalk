package com.subtilitas.doctalk.adapter.constants;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum FileSourceEnum {

    VOICE_RECORDING("voice-recordings");

    @NonNull
    private final String name;

    public static FileSourceEnum of(String s) {
        return Stream.of(values())
                .filter(en -> en.getName().equalsIgnoreCase(s))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("such file source not exists"));
    }

}
