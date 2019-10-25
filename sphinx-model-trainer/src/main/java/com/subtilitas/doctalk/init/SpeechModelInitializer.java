package com.subtilitas.doctalk.init;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.wav.WavDirectory;
import com.subtilitas.doctalk.adapter.model.SpeechModel;
import com.subtilitas.doctalk.adapter.model.dto.AdaptationDTO;
import com.subtilitas.doctalk.adapter.model.dto.SpeechModelDTO;
import com.subtilitas.doctalk.adapter.model.dto.TranscriptionDTO;
import com.subtilitas.doctalk.adapter.repository.SpeechModelRepository;
import com.subtilitas.doctalk.adapter.service.AdaptationService;
import com.subtilitas.doctalk.adapter.service.SpeechModelService;
import com.subtilitas.doctalk.adapter.service.VoiceRecordingFileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
public class SpeechModelInitializer implements ApplicationRunner {

    private final SpeechModelRepository speechModelRepository;

    private final AdaptationService adaptationService;
    private final VoiceRecordingFileService voiceRecordingFileService;

    @Override
    @SneakyThrows
    public void run(ApplicationArguments args) {
        SpeechModel enUsModel= new SpeechModel();
        enUsModel.setName("en-us speech model");
        enUsModel.setDesc("basic english model available on cmusphinx");
        enUsModel.setPath("en-us");
        speechModelRepository.save(enUsModel);
        SpeechModel deModel= new SpeechModel();
        deModel.setName("de speech model");
        deModel.setDesc("basic german model available on cmusphinx");
        deModel.setPath("de");
        speechModelRepository.save(deModel);

        long modelId = enUsModel.getId();
        String textToAdapt = Files.readString(Paths.get(getClass().getResource("process-adaptation-model-test-text").toURI()));
        AdaptationDTO adaptationDTO = adaptationService.startAdaptation(modelId,textToAdapt);
        long adaptationId = adaptationDTO.getId();
        final Map<String, Path> titlePathMap = getTitlePathMap();
        Map<UUID, MultipartFile> idFileMap = getIdMultipartFileMap(adaptationDTO, titlePathMap);
        idFileMap.forEach((id, file) -> voiceRecordingFileService.storeRecording(adaptationId, id.toString(), file));
        adaptationService.processAdaptationModel(adaptationId);
    }


    private Map<UUID, MultipartFile> getIdMultipartFileMap(AdaptationDTO adaptationDTO, Map<String, Path> titlePathMap) {
        return adaptationDTO.getTranscriptions()
                .stream()
                .collect(Collectors.toMap(TranscriptionDTO::getId, transcriptionDTO -> {
                    byte [] content = getContent(titlePathMap, transcriptionDTO);
                    return new MockMultipartFile("file", content);
                }));
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
}
