package com.subtilitas.doctalk.adapter.mapper;

import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.model.Transcription;
import com.subtilitas.doctalk.adapter.model.VoiceRecordingFile;
import com.subtilitas.doctalk.adapter.model.dto.AdaptationDTO;
import com.subtilitas.doctalk.adapter.model.dto.TranscriptionDTO;
import com.subtilitas.doctalk.adapter.model.dto.VoiceRecordingFileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdaptationMapper {

    Adaptation fromDTO(AdaptationDTO adaptationDTO);
    AdaptationDTO toDTO(Adaptation adaptation);

    Transcription fromDTO(TranscriptionDTO transcriptionDTO);
    TranscriptionDTO toDTO(Transcription transcription);

    VoiceRecordingFile fromDTO(VoiceRecordingFileDTO voiceRecordingFileDTO);
    VoiceRecordingFileDTO toDTO(VoiceRecordingFile voiceRecordingFile);
}
