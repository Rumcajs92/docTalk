package com.subtilitas.doctalk.adapter.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.wav.WavDirectory;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.subtilitas.doctalk.adapter.model.dto.AdaptationDTO;
import com.subtilitas.doctalk.adapter.model.dto.SpeechModelDTO;
import com.subtilitas.doctalk.adapter.model.dto.TranscriptionDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.hibernate.mapping.Join;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Priority;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.subtilitas.doctalk.adapter.service.AdaptationModelPathMatcher.hasPath;
import static com.subtilitas.doctalk.adapter.service.TranscriptionStatisticsCalculator.calculateTranscriptionStatistics;
import static com.subtilitas.doctalk.processRunner.ProcessRunner.runProcess;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.function.Predicate.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProcessAdaptationModelTest  {

    @LocalServerPort
    private int port;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdaptationService adaptationService;
    @Autowired
    private SpeechModelService speechModelService;
    @Autowired
    private VoiceRecordingFileService voiceRecordingFileService;
    @Value("${sphinx.directory}")
    private String sphinxDirectory;

    private List<Path> pathsToDelete = new ArrayList<>();

    private long adaptationId;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        //prepare adaptation model
        List<SpeechModelDTO> models = speechModelService.findAllModels();
        long modelId = getEnUsModelId(models);
        String textToAdapt = Files.readString(Paths.get(getClass().getResource("process-adaptation-model-test-text").toURI()));
        AdaptationDTO adaptationDTO = adaptationService.startAdaptation(modelId,textToAdapt);
        adaptationId = adaptationDTO.getId();
        final Map<String, Path> titlePathMap = getTitlePathMap();
        Map<UUID, MultipartFile> idFileMap = getIdMultipartFileMap(adaptationDTO, titlePathMap);
        idFileMap.forEach((id, file) -> voiceRecordingFileService.storeRecording(adaptationId, id.toString(), file));
    }

    private Map<UUID, MultipartFile> getIdMultipartFileMap(AdaptationDTO adaptationDTO, Map<String, Path> titlePathMap) {
        return adaptationDTO.getTranscriptions()
                .stream()
                .collect(Collectors.toMap(TranscriptionDTO::getId, transcriptionDTO -> {
                    byte [] content = getContent(titlePathMap, transcriptionDTO);
                    return new MockMultipartFile("file", content);
                }));
    }

    private Long getEnUsModelId(List<SpeechModelDTO> models) {
        return models
                .stream()
                .filter(model -> "en-us speech model".equals(model.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("no en-us model- download from cmushpinx and add to resources"))
                .getId();
    }

    private Map<String, Path> getTitlePathMap() throws IOException, URISyntaxException {
        return Files.walk(Path.of(getClass().getResource("verification").toURI()))
                .filter(not(Files::isDirectory))
                .collect(Collectors.toMap(
                        this::getTitleFromMeta,
                        path -> path
                ));
    }

    @SneakyThrows
    private byte[] getContent(Map<String, Path> titlePathMap, TranscriptionDTO transcriptionDTO) {
        String key = transcriptionDTO.getTranscriptionText().replace("<s>", "").replace("</s>", "").strip();
        return Files.readAllBytes(titlePathMap.get(key));
    }

    @SneakyThrows
    private String getTitleFromMeta(Path path) {
        return ImageMetadataReader.readMetadata(path.toFile())
                .getFirstDirectoryOfType(WavDirectory.class)
                .getString(WavDirectory.TAG_TITLE)
                .strip()
                .trim();
    }


    @Test
    void loadContextTest() {

    }

    @Test
    void returnModelPath_afterProcessAdaptationModel_forStandardInput() throws IOException, URISyntaxException {
        AdaptationDTO adaptationDTO =
                given()
                        .mockMvc(mockMvc)
                .when()
                        .put("/adaptations/{adaptationId}/processed-model", adaptationId)
                        .as(AdaptationDTO.class);

        String path = String.format("/file/adapted-models/%s", adaptationId);
        assertThat(adaptationDTO, hasPath(path));

        byte [] zippedModelBytes = given().mockMvc(mockMvc)
                .get(path)
                .asByteArray();

        assertNotNull(zippedModelBytes);
        assertTrue(zippedModelBytes.length > 0);
        log.info("got bytes, extracting");

        Path unzippedModel = getUnzippedPath(zippedModelBytes);
        Path verificationPath = Paths.get(getClass().getResource("verification/verification-01.wav").toURI());
        List<String> adaptedModelResult = transcribeAdaptedModel(unzippedModel, verificationPath);
        List<String> baseModelResult = transcribeBaseModel(verificationPath);

        log.info("got result, checking");
        List<String> transcriptLines = getTranscriptLines(ImmutableList.of(verificationPath));

        List<String> wordsAdapted = lines2Words(adaptedModelResult);
        List<String> wordsBase = lines2Words(baseModelResult);
        List<String> transcriptWords = lines2Words(transcriptLines);
        TranscriptionStatistics adaptedModelStatistics = calculateTranscriptionStatistics(wordsAdapted, transcriptWords);
        TranscriptionStatistics baseModelStatistics = calculateTranscriptionStatistics(wordsBase, transcriptWords);
        log.info("Transcript    output: {}", Joiner.on(" ").join(transcriptLines));
        log.info("Adapted Model output: {}", Joiner.on(" ").join(adaptedModelResult));
        log.info("Base Model    output: {}", Joiner.on(" ").join(baseModelResult));
        log.info("Adapted model statistics: {}", adaptedModelStatistics);
        log.info("Base model statistics: {}", baseModelStatistics);
        assertTrue(adaptedModelStatistics.getWordErrorRate() < baseModelStatistics.getWordErrorRate(), "check if adapted model error rate is better than the base one");
        assertTrue(adaptedModelStatistics.getWordErrorRate() + 10.f < baseModelStatistics.getWordErrorRate(), "check if adapted model error rate is better than the base one by at lest 10 percent");
    }

    private List<String> getTranscriptLines(ImmutableList<Path> verificationPaths) {
        return verificationPaths.stream()
                .map(this::getTitleFromMeta)
                .collect(Collectors.toList());
    }

    private List<String> transcribeAdaptedModel(Path unzippedModel, Path verificationPath) throws IOException {
        Path pocketsphionx_continoues = Paths.get(sphinxDirectory, "sphinxtrain", "bin", "Release", "x64", "pocketsphinx_continuous").toAbsolutePath();
        Path acousticDir = unzippedModel.resolve("acoustic").toAbsolutePath();
        Path mdefBin = acousticDir.resolve("mdef.bin").toAbsolutePath();
        Path mllr_matrix = acousticDir.resolve("mllr_matrix").toAbsolutePath();
        Path senddump = acousticDir.resolve("senddump").toAbsolutePath();
        Path dictionaryPath = unzippedModel.resolve("cmudict-en-us.dict").toAbsolutePath();
        Path languageModelPath = unzippedModel.resolve("en-us.lm.bin").toAbsolutePath();
        List<String> commands = ImmutableList.<String>builder()
                .add(pocketsphionx_continoues.toString())
                .add("-hmm", acousticDir.toString())
//                .add("-sendump", senddump.toString())
//                .add("-mdef",  mdefBin.toString())
                .add("-mllr", mllr_matrix.toString())
                .add("-lm",languageModelPath.toString())
                .add("-dict",  dictionaryPath.toString())
                .add("-infile", verificationPath.toString())
                .build();
        return runProcess(commands);
    }

    private Path getUnzippedPath(byte[] zippedModelBytes) throws IOException {
        Path workingDir = Files.createTempDirectory("working-model-dir");
        pathsToDelete.add(workingDir);
        Path zippedModel = Files.write(workingDir.resolve("zipped-model"), zippedModelBytes, StandardOpenOption.CREATE);
        ZipFile zipFile = new ZipFile(zippedModel.toFile());
        Path unzippedModel = workingDir.resolve("model");
        zipFile.extractAll(unzippedModel.toAbsolutePath().toString());
        return unzippedModel;
    }

    private List<String> lines2Words(List<String> processResult) {
        return processResult.stream().map(str -> str.split(" ")).flatMap(Stream::of).map(String::strip).map(String::toLowerCase).collect(Collectors.toList());
    }

    private List<String> transcribeBaseModel(Path verificationPath) throws URISyntaxException, IOException {
        Path unzippedModel = Path.of(getClass().getResource("en-us").toURI());
        Path pocketsphionx_continoues = Paths.get(sphinxDirectory, "sphinxtrain", "bin", "Release", "x64", "pocketsphinx_continuous").toAbsolutePath();
        Path acousticDir = unzippedModel.resolve("en-us").toAbsolutePath();
        Path mdef = acousticDir.resolve("mdef").toAbsolutePath();
        Path dictionaryPath = unzippedModel.resolve("cmudict-en-us.dict").toAbsolutePath();
        Path languageModelPath = unzippedModel.resolve("en-us.lm.bin").toAbsolutePath();
        List<String> commands = ImmutableList.<String>builder()
                .add(pocketsphionx_continoues.toString())
                .add("-hmm", acousticDir.toString())
                .add("-mdef",  mdef.toString())
                .add("-lm",languageModelPath.toString())
                .add("-dict",  dictionaryPath.toString())
                .add("-infile", verificationPath.toString())
                .build();
        return runProcess(commands);
    }

    @AfterEach
    void tearDown() {
        log.info("Deleting files: ");
        pathsToDelete.forEach(this::delete);
    }

    @SneakyThrows
    private void delete(Path path){
        if(Files.isDirectory(path)){
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .forEach(this::deleteOne);
        }
        else {
            deleteOne(path);
        }
    }

    @SneakyThrows
    private void deleteOne(Path path)  {
        log.info("Deleting file {}", path.toString());
        Files.deleteIfExists(path);
    }


}