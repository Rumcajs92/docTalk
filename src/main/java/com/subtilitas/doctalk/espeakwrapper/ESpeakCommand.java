package com.subtilitas.doctalk.espeakwrapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.subtilitas.doctalk.ProcessRunner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ESpeakCommand implements ProcessRunner
{

    private final Path eSpeakPath;

    private final ESpeakParameters parameters;

    public static ESpeakCommand build(Path eSpeakPath, ESpeakParameters eSpeakParameters) {
        return new ESpeakCommand(eSpeakPath, eSpeakParameters);
    }

    @Override
    public Process run(ProcessBuilder processBuilder) throws IOException
    {
        List<String> commands = getCommands();
        return processBuilder
            .command(commands)
            .start();
    }

    private List<String> getCommands()
    {
        return Stream.concat(
            Stream.of(eSpeakPath.toString()), parameters.commandStream())
            .collect(Collectors.toList());
    }

    @Override
    public String toString()
    {
        return String.join(" ", getCommands());
    }
}
