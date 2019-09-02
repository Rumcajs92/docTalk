package com.subtilitas.doctalk.adapter.service;

import com.subtilitas.doctalk.adapter.mapper.AdaptationMapper;
import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.model.Transcription;
import com.subtilitas.doctalk.adapter.model.dto.AdaptationDTO;
import com.subtilitas.doctalk.adapter.repository.AdaptationRepository;
import com.subtilitas.doctalk.cmutoolkit.CMUToolkit;
import com.subtilitas.doctalk.cmutoolkit.NormalizedText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class AdaptationService {



    private final CMUToolkit cmuToolkit = new CMUToolkit(Paths.get("C:\\Users\\tobia\\sphninx\\cmuclmtk"));

    private final AdaptationRepository adaptationRepository;

    private final AdaptationMapper mapper;



    public AdaptationDTO startAdaptation(Long modelId, String text) {
        NormalizedText sentencesToTranscript = cmuToolkit.normalizeText(text);

        //generate transcriptions
        Set<Transcription> transcriptions = sentencesToTranscript.getFormattedLines().stream()
                .map(Transcription::new)
                .collect(Collectors.toSet());

        //set adaptation
        Adaptation adaptation = new Adaptation();
        adaptation.setName("adaptation");
        adaptation.setTranscriptions(transcriptions);
        adaptation.setAdaptedSpeechModelId(modelId);

        Adaptation savedAdaptation = adaptationRepository.save(adaptation);
        Adaptation gotAdaptation = adaptationRepository.getOne(savedAdaptation.getId());
        return mapper.toDTO(gotAdaptation);

    }

    public AdaptationDTO getAdaptation(Long id) {
        Adaptation gotAdaptation = adaptationRepository.getOne(id);
        return mapper.toDTO(gotAdaptation);
    }

    public AdaptationDTO processAdaptationModel(Long adaptationId) {
        Adaptation gotAdaptation = adaptationRepository.getOne(adaptationId);
        if(allTranscriptionsHasRecordedFiles(gotAdaptation)) {
            //TODO add exception handler
            throw new RuntimeException("all transcriptions must have voice recorded files");
        }

        return null;
    }

    public boolean allTranscriptionsHasRecordedFiles(Adaptation gotAdaptation) {
        return gotAdaptation.getTranscriptions().stream().anyMatch(transcription -> transcription.getVoiceRecordingFiles().isEmpty());
    }
}
