package com.subtilitas.doctalk.adapter.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class AdaptationDTO {

    private Long id;

    private String name;

    private Set<TranscriptionDTO> transcriptions;

    private String path;
}
