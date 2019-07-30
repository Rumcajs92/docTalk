package com.subtilitas.doctalk.adapter.mapper;

import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.model.SpeechModel;
import com.subtilitas.doctalk.adapter.model.dto.AdaptationDTO;
import com.subtilitas.doctalk.adapter.model.dto.SpeechModelDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpeechModelMapper {

    SpeechModel formDTO(SpeechModelDTO speechModelDTO);
    SpeechModelDTO toDTO(SpeechModel speechModel);

    List<SpeechModel> fromDTOs (List<SpeechModelDTO> speechModelDTOS);
    List<SpeechModelDTO> toDTOs (List<SpeechModel> speechModels);

    Adaptation fromDTO(AdaptationDTO adaptationDTO);
    AdaptationDTO toDTO(Adaptation adaptation);
}
