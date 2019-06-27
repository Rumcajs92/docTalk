package com.subtilitas.doctalk.wordextractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UniqueWordExtractor
{
    public Path extractUniqueWords(Path path) throws IOException
    {
        return Files.write(path.resolveSibling(path.getFileName().toString() + "-unique-words"),
                extract(Files.lines(path))
            );
    }

    public List<String> extractUniqueWords(List<String> words) {
        return extract(words.stream());
    }


    private List<String> extract(Stream<String> stringStream) {
        return stringStream.flatMap( s -> Stream.of(s.split(" ")))
                .map( s -> s.replaceAll("[^a-zA-Z_0-9ąęłżźćńóś]" , ""))
                .filter(s -> !s.isBlank())
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

}
