package com.subtilitas.doctalk.adapter.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class TranscriptionDTO {

    private UUID id;

    private String transcriptionText;

    private List<VoiceRecordingFileDTO> voiceRecordingFiles;
}
