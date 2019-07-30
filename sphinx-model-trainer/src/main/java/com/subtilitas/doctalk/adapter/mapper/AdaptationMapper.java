package com.subtilitas.doctalk.adapter.mapper;

import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.model.SpeechModel;
import com.subtilitas.doctalk.adapter.model.Transcription;
import com.subtilitas.doctalk.adapter.model.VoiceRecordingFile;
import com.subtilitas.doctalk.adapter.model.dto.AdaptationDTO;
import com.subtilitas.doctalk.adapter.model.dto.SpeechModelDTO;
import com.subtilitas.doctalk.adapter.model.dto.TranscriptionDTO;
import com.subtilitas.doctalk.adapter.model.dto.VoiceRecordingFileDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdaptationMapper {

    Adaptation fromDTO(AdaptationDTO adaptationDTO);
    AdaptationDTO toDTO(Adaptation adaptation);

    Transcription fromDTO(TranscriptionDTO transcriptionDTO);
    TranscriptionDTO toDTO(Transcription transcription);

    VoiceRecordingFile fromDTO(VoiceRecordingFileDTO voiceRecordingFileDTO);
    VoiceRecordingFileDTO toDTO(VoiceRecordingFile voiceRecordingFile);

    SpeechModel formDTO(SpeechModelDTO speechModelDTO);
    SpeechModelDTO toDTO(SpeechModel speechModel);

}
