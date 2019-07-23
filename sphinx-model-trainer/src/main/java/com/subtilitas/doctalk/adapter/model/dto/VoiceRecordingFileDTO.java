package com.subtilitas.doctalk.adapter.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class VoiceRecordingFileDTO {

    private Long id;

    private UUID transcriptionId;

    private Long adaptationId;

    private String path;

    private String extension;

}
