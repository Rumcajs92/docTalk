package com.subtilitas.doctalk.audiodownlader;

import java.nio.file.Path;

public class FileDownloader
{

    private final Path pathToWrite;

    private final FileUrlProvider fileUrlProvider;

    public FileDownloader(Path pathToWrite, FileUrlProvider fileurlProvider)
    {
        this.pathToWrite = pathToWrite;
        this.fileUrlProvider = fileurlProvider;
    }

    public Path download(String fileName)
    {
        return null;
    }
}
