package com.subtilitas.doctalk.adapter.service;

import com.subtilitas.doctalk.adapter.mapper.AdaptationMapper;
import com.subtilitas.doctalk.adapter.model.VoiceRecordingFile;
import com.subtilitas.doctalk.adapter.model.dto.VoiceRecordingFileDTO;
import com.subtilitas.doctalk.adapter.repository.VoiceRecordingFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VoiceRecordingFileService {

    private final VoiceRecordingFileRepository recordingFileRepository;

    private final AdaptationMapper mapper;

    @SneakyThrows
    public VoiceRecordingFileDTO storeRecording(Long adaptationId, String transcriptionId, MultipartFile file){
        VoiceRecordingFile voiceRecordingFile = new VoiceRecordingFile();
        voiceRecordingFile.setAdaptationId(adaptationId);
        voiceRecordingFile.setTranscriptionId(UUID.fromString(transcriptionId));
        voiceRecordingFile.setData(file.getBytes());
        voiceRecordingFile.setExtension(file.getContentType());
        voiceRecordingFile.setPath("/path");
        VoiceRecordingFile savedFile = recordingFileRepository.save(voiceRecordingFile);
        VoiceRecordingFile gotFile = recordingFileRepository.getOne(savedFile.getId());
        return mapper.toDTO(gotFile);
    }

    @SneakyThrows
    public VoiceRecordingFileDTO getVoiceRecording(Long id){
       VoiceRecordingFile voiceRecordingFile = recordingFileRepository.getOne(id);




        return mapper.toDTO(voiceRecordingFile);
    }

}
