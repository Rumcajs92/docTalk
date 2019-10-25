package com.subtilitas.doctalk.adapter.service;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import io.vavr.Tuple2;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Log4j2
@Component
public class AdaptationModelContainer {

    private Map<Long, Tuple2<Path, StreamSpeechRecognizer>> adaptationIdModelPathMap = new ConcurrentHashMap<>();

    @SneakyThrows
    public void add(Long id, Path model) {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(toURLString(model.resolve("acoustic")));
        configuration.setDictionaryPath(toURLString(model.resolve("cmudict-en-us.dict")));
        configuration.setLanguageModelPath(toURLString(model.resolve("en-us.lm.bin")));
        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
        adaptationIdModelPathMap.put(id, new Tuple2<>(model, recognizer));
    }

    @SneakyThrows
    private String toURLString(Path path) {
        return path.toUri().toURL().toString();
    }

    public Path getModelPath(Long id) {
        return adaptationIdModelPathMap.get(id)._1();
    }

    public StreamSpeechRecognizer getModelRecognizer(Long id) {
        return adaptationIdModelPathMap.get(id)._2();
    }

    @PreDestroy
    public void destroy() {
        adaptationIdModelPathMap.values().forEach(tuplePathRecognizer -> walk(tuplePathRecognizer._1)
                .sorted(Comparator.reverseOrder())
                .peek(path -> log.info("Deleting file: {}", path.toAbsolutePath().toString()))
                .forEach(this::delete)
        );
    }

    @SneakyThrows
    public void delete(Path path){
        Files.deleteIfExists(path);
    }

    @SneakyThrows
    public Stream<Path> walk(Path path) {
        return Files.walk(path);
    }

}
