package com.subtilitas.doctalk.audiodownlader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.collect.Lists;
import com.subtilitas.doctalk.audiodownlader.FileDownloader;
import com.subtilitas.doctalk.audiodownlader.FileUrlProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

public class FileDownloaderTest
{

    private static final String PATH_TO_FILE = "path-to-file";
    private ClientAndServer mockServer;

    @Mock
    private FileUrlProvider fileurlProvider;


    @BeforeEach
    public void setUp() throws IOException
    {

        Path testFile = getTestFilePath();
        mockServer = ClientAndServer.startClientAndServer(8080);
        HttpRequest requestToGetFile = HttpRequest
            .request(PATH_TO_FILE)
            .withMethod("GET");
        HttpResponse responseWithFile = HttpResponse.response().withBody(Files.readAllBytes(testFile));
        mockServer
            .when(requestToGetFile)
            .respond(responseWithFile);

        final String urlToFile = "http://localhost:8080/" + PATH_TO_FILE;
        Mockito.when(fileurlProvider.getFileUrls())
            .thenReturn(Lists.newArrayList(urlToFile));
    }

    private Path getTestFilePath()
    {
        return Paths.get("./test-audio.mp3");
    }

    @Test
    public void test() throws IOException
    {


        String temporaryFolderName = "./temp/";
        Path pathToWrite = Paths.get(temporaryFolderName);
        String fileName = "downloadedTestAudioFile";
        FileDownloader fileDownloader = new FileDownloader(pathToWrite, fileurlProvider);

        Path downloadedFilePath =  fileDownloader.download(fileName);

        Assertions.assertTrue(Files.exists(downloadedFilePath));
        Assertions.assertTrue(Files.exists(pathToWrite));
        Assertions.assertTrue(Files.isDirectory(pathToWrite));
        Assertions.assertTrue(Files.exists(Paths.get(temporaryFolderName)));
        Assertions.assertEquals(Files.readAllBytes(downloadedFilePath),Files.readAllBytes(getTestFilePath()));

        Files.delete(downloadedFilePath);
        Files.delete(pathToWrite);
    }

    @AfterEach
    public void tearDown() {
        mockServer.stop();
    }

}
