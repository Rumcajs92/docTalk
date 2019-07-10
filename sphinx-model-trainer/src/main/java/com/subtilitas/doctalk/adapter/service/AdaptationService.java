package com.subtilitas.doctalk.adapter.service;

import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.model.Transcription;
import com.subtilitas.doctalk.adapter.repository.AdaptationRepository;
import com.subtilitas.doctalk.cmutoolkit.CMUToolkit;
import com.subtilitas.doctalk.cmutoolkit.NormalizedText;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdaptationService {

    private final CMUToolkit cmuToolkit = new CMUToolkit(Paths.get("C:\\Users\\tobia\\sphninx\\cmuclmtk"));

    private final AdaptationRepository adaptationRepository;

//    private final TranscriptionRepository  transcriptionRepository;


    public Adaptation startAdaptation(String text) {
        NormalizedText sentencesToTranscript = cmuToolkit.normalizeText(text);

        //set adaptation
        Adaptation adaptation = new Adaptation();
        adaptation.setName("adaptation");
        Adaptation savedAdaptation =  adaptationRepository.save(adaptation);
        //set transcriptions

//        List<Transcription> transcriptions = insertTranscriptions(id, sentencesToTranscript);

        return adaptationRepository.getOne(savedAdaptation.getId()); //adaptationRepository.get(id)

    }

    private List<Transcription> insertTranscriptions(Long adaptationId, NormalizedText sentencesToTranscript) {


        return null;
    }
}
