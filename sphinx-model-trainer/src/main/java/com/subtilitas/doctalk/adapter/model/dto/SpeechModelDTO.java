package com.subtilitas.doctalk.adapter.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
public class SpeechModelDTO {

    private Long id;

    private String name;

    private String desc;

    private String path;

    private Set<AdaptationDTO> adaptations;

}
