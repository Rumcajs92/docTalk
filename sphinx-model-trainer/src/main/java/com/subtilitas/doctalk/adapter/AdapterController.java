package com.subtilitas.doctalk.adapter;

import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.model.dto.AdaptationDTO;
import com.subtilitas.doctalk.adapter.model.dto.SpeechModelDTO;
import com.subtilitas.doctalk.adapter.model.dto.VoiceRecordingFileDTO;
import com.subtilitas.doctalk.adapter.service.AdaptationService;
import com.subtilitas.doctalk.adapter.service.SpeechModelService;
import com.subtilitas.doctalk.adapter.service.VoiceRecordingFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AdapterController {

    private final AdaptationService adaptationService;

    private final VoiceRecordingFileService voiceRecordingFileService;

    private final SpeechModelService speechModelService;

    @ResponseBody
    @GetMapping("/adaptation/{id}")
    public AdaptationDTO getAdaptation(@PathVariable Long id) {
        return adaptationService.getAdaptation(id);
    }

    @ResponseBody
    @PostMapping("/adaptation")
    public AdaptationDTO startNewAdaptation(@RequestBody String text) {
        return adaptationService.startAdaptation(text);
    }

    @ResponseBody
    @PostMapping("/adaptation/{adaptationId}/transcription/{transcriptionId}")
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



}
