package com.subtilitas.doctalk;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public interface ClassResourceReader {

    default String readResource(String name) throws URISyntaxException, IOException {
        return Files.readString(Paths.get(getClass().getResource(name).toURI()));
    }

    default List<String> readResourceLines(String name) throws URISyntaxException, IOException {
        return Files.readAllLines(Paths.get(getClass().getResource(name).toURI()));
    }
}
