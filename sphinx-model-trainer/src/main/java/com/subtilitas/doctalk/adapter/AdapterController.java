package com.subtilitas.doctalk.adapter;

import com.google.common.base.Joiner;
import com.subtilitas.doctalk.adapter.constants.FileSourceEnum;
import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.model.dto.*;
import com.subtilitas.doctalk.adapter.service.*;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AdapterController {

    private final AdaptationService adaptationService;
    private final VoiceRecordingFileService voiceRecordingFileService;
    private final SpeechModelService speechModelService;
    private final FileService fileService;
    private final AdaptationModelContainer adaptationModelContainer;

    @ResponseBody
    @GetMapping("/adaptations/{id}")
    public AdaptationDTO getAdaptation(@PathVariable Long id) {
        return adaptationService.getAdaptation(id);
    }

    @ResponseBody
    @PostMapping("/speech-models/{modelId}/adaptations")
    public AdaptationDTO startNewAdaptation(@RequestParam("text") String text, @PathVariable Long modelId, @RequestParam("files[]") MultipartFile []  files) {
        String fileString = Stream.of(files)
                .map(this::getString)
                .collect(Collectors.joining("."));
        String textToAdapt = Joiner.on(".").join(text, fileString);
        AdaptationDTO adaptationDTO = adaptationService.startAdaptation(modelId, textToAdapt);
        log.info(adaptationDTO.toString());
        return adaptationDTO;
    }

    @ResponseBody
    @PutMapping("/adaptations/{adaptationId}/processed-model")
    public AdaptationDTO processAdaptedModel(@PathVariable Long adaptationId) {
        return adaptationService.processAdaptationModel(adaptationId);
    }

    @SneakyThrows
    public String getString(MultipartFile file) {
        return IOUtils.toString(getInputStream(file), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public InputStream getInputStream(MultipartFile file) {
        return file.getInputStream();
    }


    @ResponseBody
    @PostMapping("/adaptations/{adaptationId}/transcriptions/{transcriptionId}")
    public VoiceRecordingFileDTO storeRecording(@PathVariable Long adaptationId, @PathVariable String transcriptionId, @RequestParam("file") MultipartFile file) {
        return voiceRecordingFileService.storeRecording(adaptationId, transcriptionId, file);
    }

    @ResponseBody
    @GetMapping("/voice-files/{id}")
    public VoiceRecordingFileDTO getVoiceRecording(@PathVariable Long id) {
        return voiceRecordingFileService.getVoiceRecording(id);
    }

    @ResponseBody
    @GetMapping("/speech-models")
    public List<SpeechModelDTO> getSpeechModels() {
        return speechModelService.findAllModels();
    }

    @ResponseBody
    @GetMapping("/file/{fileSource}/{id}")
    public byte [] getFileData(@PathVariable FileSourceEnum fileSource, @PathVariable Long id) {
        return fileService.getFile(fileSource, id);
    }

    @ResponseBody
    @GetMapping("/speech-model/{modelId}")
    public SpeechModelDTO getSpeechModel(@PathVariable Long modelId) {
        return speechModelService.getModel(modelId);
    }

    @ResponseBody
    @PutMapping("/adaptations/{adaptationId}/result")
    @SneakyThrows
    public String send(@PathVariable Long adaptationId, @RequestParam("file") MultipartFile file) {

        InputStream is = new ByteArrayInputStream(file.getBytes());

        StreamSpeechRecognizer recognizer = adaptationModelContainer.getModelRecognizer(adaptationId);
        recognizer.startRecognition(is);
        SpeechResult result;
        List<String> hypothesisList = new ArrayList<>();
        while ((result = recognizer.getResult()) != null) {
            String hypothesis = result.getHypothesis();
            hypothesisList.add(hypothesis);
        }
        recognizer.stopRecognition();
        String resultString = Joiner.on(" ").join(hypothesisList);
        log.info("Result: {}",resultString);
        return resultString;

    }

}
