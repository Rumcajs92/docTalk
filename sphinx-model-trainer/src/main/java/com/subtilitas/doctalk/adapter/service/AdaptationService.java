package com.subtilitas.doctalk.adapter.service;

import com.subtilitas.doctalk.adapter.mapper.AdaptationMapper;
import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.model.Transcription;
import com.subtilitas.doctalk.adapter.model.dto.AdaptationDTO;
import com.subtilitas.doctalk.adapter.repository.AdaptationRepository;
import com.subtilitas.doctalk.cmutoolkit.CMUToolkit;
import com.subtilitas.doctalk.cmutoolkit.NormalizedText;
import io.vavr.Tuple3;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
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

    @SneakyThrows
    public AdaptationDTO processAdaptationModel(Long adaptationId) {
        Adaptation gotAdaptation = adaptationRepository.getOne(adaptationId);
        if(allTranscriptionsHasRecordedFiles(gotAdaptation)) {
            //TODO add exception handler
            throw new RuntimeException("all transcriptions must have voice recorded files");
        }
        //TODO

        //generate temporary working directory
        String adaptationIdentifier = gotAdaptation.getName() + gotAdaptation.getId();
        Path temporaryAdaptationDirectory = Files.createTempDirectory(adaptationIdentifier);

        //generate temp fileids
        List<String> fileIdsLines = gotAdaptation.getTranscriptions().stream()
                .map(Transcription::getId)
                .map(UUID::toString)
                .collect(Collectors.toList());
        Path fileIds = temporaryAdaptationDirectory.resolve(String.format("%s.fileids", adaptationIdentifier));
        Files.write(fileIds,  fileIdsLines);

        //generate temp transcription
        List<String> transcriptionLines = gotAdaptation.getTranscriptions().stream()
                .map(this::getTranscriptionLine)
                .collect(Collectors.toList());
        Path transcriptionFile = temporaryAdaptationDirectory.resolve(String.format("%s.transcription", adaptationIdentifier));
        Files.write(transcriptionFile, transcriptionLines);

//        generate temp wav from database
        List<Path> audioFilePaths =  gotAdaptation.getTranscriptions().stream().map(transcription -> {
            //TODO remove get(0)
            byte [] data = transcription.getVoiceRecordingFiles().get(0).getData();
            InputStream byteInputStream = new ByteArrayInputStream(data);
            AudioFormat audioFormat = new AudioFormat(16000.0f, 16, 1, true, false);
            AudioInputStream audioInputStream = new AudioInputStream(byteInputStream,audioFormat,  2);
            Path audioPath = temporaryAdaptationDirectory.resolve(String.format("%s.wav", transcription.getId()));
            writeAudioFile(audioInputStream, audioPath);
            return audioPath;
        }).collect(Collectors.toList());

        //copy acoustinc model from path

        //TODO copy language model and dictionary
        //they will be the default ones

        //copy files to temporary folder

        //generate acoustic feature files
        //execute sphinx_fe -argfile en-us/feat.params \
        //        -samprate 16000 -c filename.fileids \
        //       -di . -do . -ei wav -eo mfc -mswav yes

        //accumulating observation counts
        //copy bw tool to working directory?
        //execute it with accepted parameters from feat params
        //./bw \
        // -hmmdir en-us \
        // -moddeffn en-us/mdef.txt \
        // -ts2cbfn .ptm. \
        // -feat 1s_c_d_dd \
        // -svspec 0-12/13-25/26-38 \
        // -cmn current \
        // -agc none \
        // -dictfn cmudict-en-us.dict \
        // -ctlfn arctic20.fileids \
        // -lsnfn arctic20.transcription \
        // -accumdir .


        return null;
    }

    @SneakyThrows
    public void writeAudioFile(AudioInputStream audioInputStream, Path audioPath) {
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioPath.toFile());
    }

    public String getTranscriptionLine(Transcription transcription) {
        return String.format("%s (%s)", transcription.getTranscriptionText().stripTrailing(), transcription.getId().toString());
    }

    public boolean allTranscriptionsHasRecordedFiles(Adaptation gotAdaptation) {
        return gotAdaptation.getTranscriptions().stream().anyMatch(transcription -> transcription.getVoiceRecordingFiles().isEmpty());
    }
}
