package com.subtilitas.doctalk.adapter.service;

import com.subtilitas.doctalk.adapter.mapper.SpeechModelMapper;
import com.subtilitas.doctalk.adapter.model.SpeechModel;
import com.subtilitas.doctalk.adapter.model.dto.SpeechModelDTO;
import com.subtilitas.doctalk.adapter.repository.SpeechModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeechModelService {

    private final SpeechModelRepository speechModelRepository;

    private final SpeechModelMapper speechModelMapper;

    public List<SpeechModelDTO> findAllModels() {
        List<SpeechModel> speechModels = speechModelRepository.findAll();
        return speechModelMapper.toDTOs(speechModels);
    }

    public SpeechModelDTO getModel(Long modelId) {
        SpeechModel speechModel = speechModelRepository.getOne(modelId);
        return speechModelMapper.toDTO(speechModel);
    }
}
