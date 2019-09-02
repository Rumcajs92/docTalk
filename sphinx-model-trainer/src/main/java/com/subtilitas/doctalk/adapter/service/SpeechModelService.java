package com.subtilitas.doctalk.adapter.service;

import com.subtilitas.doctalk.adapter.mapper.SpeechModelMapper;
import com.subtilitas.doctalk.adapter.model.SpeechModel;
import com.subtilitas.doctalk.adapter.model.dto.SpeechModelDTO;
import com.subtilitas.doctalk.adapter.repository.SpeechModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
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
