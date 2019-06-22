package com.subtilitas.doctalk.wordextractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UniqueWordExtractorImpl implements UniqueWordExtractor
{
    @Override
    public Path extractUniqueWords(Path path) throws IOException
    {
        return Files.write(path.resolveSibling(path.getFileName().toString() + "-unique-words"),
            Files.lines(path)
                    .flatMap( s -> Stream.of(s.split(" ")))
                    .map( s -> s.replaceAll("[^a-zA-Z_0-9ąęłżźćńóś]" , ""))
                    .filter(s -> !s.isBlank())
                    .map(String::toLowerCase)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList())
            );
    }
}
