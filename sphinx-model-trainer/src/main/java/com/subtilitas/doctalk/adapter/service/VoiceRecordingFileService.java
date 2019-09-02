package com.subtilitas.doctalk.adapter.service;

import com.subtilitas.doctalk.adapter.constants.FileSourceEnum;
import com.subtilitas.doctalk.adapter.mapper.AdaptationMapper;
import com.subtilitas.doctalk.adapter.model.VoiceRecordingFile;
import com.subtilitas.doctalk.adapter.model.dto.VoiceRecordingFileDTO;
import com.subtilitas.doctalk.adapter.repository.VoiceRecordingFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
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
        VoiceRecordingFile savedFile = recordingFileRepository.save(voiceRecordingFile);
        voiceRecordingFile.setPath(String.format("/file/%s/%d", FileSourceEnum.VOICE_RECORDING.getName() , savedFile.getId()));
        recordingFileRepository.save(voiceRecordingFile);
        VoiceRecordingFile gotFile = recordingFileRepository.getOne(savedFile.getId());
        return mapper.toDTO(gotFile);
    }

    @SneakyThrows
    public VoiceRecordingFileDTO getVoiceRecording(Long id){
       VoiceRecordingFile voiceRecordingFile = recordingFileRepository.getOne(id);
        return mapper.toDTO(voiceRecordingFile);
    }

}
