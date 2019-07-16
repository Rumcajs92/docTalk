package com.subtilitas.doctalk.adapter.service;

import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.model.Transcription;
import com.subtilitas.doctalk.adapter.model.VoiceRecordingFileInfo;
import com.subtilitas.doctalk.adapter.repository.AdaptationRepository;
import com.subtilitas.doctalk.cmutoolkit.CMUToolkit;
import com.subtilitas.doctalk.cmutoolkit.NormalizedText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdaptationService {



    private final CMUToolkit cmuToolkit = new CMUToolkit(Paths.get("C:\\Users\\tobia\\sphninx\\cmuclmtk"));

    private final AdaptationRepository adaptationRepository;

//    private final TranscriptionRepository  transcriptionRepository;


    public Adaptation startAdaptation(String text) {
        NormalizedText sentencesToTranscript = cmuToolkit.normalizeText(text);

        //generate transcriptions
        Set<Transcription> transcriptions = sentencesToTranscript.getFormattedLines().stream()
                .map(Transcription::new)
                .collect(Collectors.toSet());

        VoiceRecordingFileInfo voiceRecordingFileInfo = new VoiceRecordingFileInfo();
        voiceRecordingFileInfo.setSomeData("some data");


        transcriptions.forEach(transcription -> transcription.setVoiceRecordingFileInfos(Collections.singleton(voiceRecordingFileInfo)));

        //set adaptation
        Adaptation adaptation = new Adaptation();
        adaptation.setName("adaptation");
        adaptation.setTranscriptions(transcriptions);

        Adaptation savedAdaptation = adaptationRepository.save(adaptation);

        return adaptationRepository.getOne(savedAdaptation.getId()); //adaptationRepository.get(id)

    }

    private List<Transcription> insertTranscriptions(Long adaptationId, NormalizedText sentencesToTranscript) {


        return null;
    }
}
