package com.subtilitas.doctalk.adapter.service;

import com.google.common.collect.ImmutableList;
import com.subtilitas.doctalk.adapter.constants.FileSourceEnum;
import com.subtilitas.doctalk.adapter.mapper.AdaptationMapper;
import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.model.Transcription;
import com.subtilitas.doctalk.adapter.model.dto.AdaptationDTO;
import com.subtilitas.doctalk.adapter.repository.AdaptationRepository;
import com.subtilitas.doctalk.cmutoolkit.CMUToolkit;
import com.subtilitas.doctalk.cmutoolkit.NormalizedText;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.lingala.zip4j.ZipFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.subtilitas.doctalk.processRunner.ProcessRunner.runProcess;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class AdaptationService {



    private final CMUToolkit cmuToolkit = new CMUToolkit(Paths.get("C:\\Users\\tobia\\sphninx\\cmuclmtk"));

    private final AdaptationRepository adaptationRepository;
    private final AdaptationModelContainer modelContainer;

    private final AdaptationMapper mapper;

    @Value("${sphinx.directory}")
    private String sphinxDirectory;



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
        Adaptation gotAdaptation = getAdaptationForProcess(adaptationId);
        Path temporaryAdaptationDirectory = generateTemporaryWorkingDirectory(gotAdaptation);
        Try.of(() -> adaptModel(temporaryAdaptationDirectory, gotAdaptation))
                .andThenTry((zippedPath) -> {
                    gotAdaptation.setData(Files.readAllBytes(zippedPath));
                })
                .andFinallyTry(() -> modelContainer.add(gotAdaptation.getId(), temporaryAdaptationDirectory));
        gotAdaptation.setPath(FileSourceEnum.ADAPTED_MODELS.parse(gotAdaptation.getId()));
        adaptationRepository.save(gotAdaptation);
        return getAdaptation(adaptationId);
    }

    @SneakyThrows
    public boolean deleteFile(Path path){
        return Files.deleteIfExists(path);
    }

    private Path adaptModel(Path temporaryAdaptationDirectory, Adaptation gotAdaptation) throws IOException, URISyntaxException {
        Path fileIds = generateTempFileids(gotAdaptation, temporaryAdaptationDirectory);
        Path transcription = generateTemporaryTranscription(gotAdaptation, temporaryAdaptationDirectory);
        List<Path> audioFilePaths = generateWavFiles(gotAdaptation, temporaryAdaptationDirectory);

        //copy acoustic model from path
        //copy files to temporary folder
        Path acousticModelDirectory = copyAcousticModelDirectory(temporaryAdaptationDirectory);
        Path dictionaryFile = Files.copy(Path.of(getClass().getResource("en-us/cmudict-en-us.dict").toURI()), temporaryAdaptationDirectory.resolve("cmudict-en-us.dict"));
        Path languageModelPath = Files.copy(Path.of(getClass().getResource("en-us/en-us.lm.bin").toURI()), temporaryAdaptationDirectory.resolve("en-us.lm.bin"));

        runSphinxFe(temporaryAdaptationDirectory, fileIds, acousticModelDirectory);
        runBw(fileIds, transcription, acousticModelDirectory, dictionaryFile,temporaryAdaptationDirectory);
        runMllrTransform(temporaryAdaptationDirectory, acousticModelDirectory);
        runMks2senddump(acousticModelDirectory);
        runPocketsphinxMdefConvert(acousticModelDirectory);


        ZipFile pocektSphinxZip = new ZipFile(temporaryAdaptationDirectory.resolve("model.zip").toFile());
        pocektSphinxZip.addFolder(acousticModelDirectory.toFile());
        pocektSphinxZip.addFile(dictionaryFile.toFile());
        pocektSphinxZip.addFile(languageModelPath.toFile());
        return pocektSphinxZip.getFile().toPath();
    }

    private void runPocketsphinxMdefConvert(Path acousticModelDirectory) throws IOException {
        Path pocketsphinx_mdef_convert = Paths.get(sphinxDirectory, "sphinxtrain", "bin", "Release", "x64", "pocketsphinx_mdef_convert").toAbsolutePath();
        Path mdef = acousticModelDirectory.resolve("mdef").toAbsolutePath();
        Path mdefbin = acousticModelDirectory.resolve("mdef.bin").toAbsolutePath();
        List<String> pocketsphinx_mdef_convertCommands = ImmutableList.<String>builder()
                .add(pocketsphinx_mdef_convert.toString())
                .add("-bin")
                .add(mdef.toString(), mdefbin.toString())
                .build();
        runProcess(pocketsphinx_mdef_convertCommands);
    }

    private Path copyAcousticModelDirectory(Path temporaryAdaptationDirectory) throws URISyntaxException, IOException {
        Path acousticModelDirectory = temporaryAdaptationDirectory.resolve("acoustic");
        Path src = Path.of(getClass().getResource("en-us/en-us").toURI());
        Files.walk(src).forEach(source -> copy(source, acousticModelDirectory.resolve(src.relativize(source))));
        return acousticModelDirectory;
    }

    @SneakyThrows
    private void copy(Path source, Path dest) {
        Files.copy(source, dest);
    }

    private void runMks2senddump(Path acousticModelDirectory) throws IOException {
        Path mk_s2sendumpconvert = Paths.get(sphinxDirectory, "sphinxtrain", "bin", "Release", "x64", "mk_s2sendump").toAbsolutePath();
        List<String> mk_s2sendumpconvertCommands = ImmutableList.<String>builder()
                .add(mk_s2sendumpconvert.toString())
                .add("-pocketsphinx", "yes")
                .add("-moddeffn", acousticModelDirectory.resolve("mdef").toAbsolutePath().toString())
                .add("-mixwfn", acousticModelDirectory.resolve("mixture_weights").toAbsolutePath().toString())
                .add("-sendumpfn", acousticModelDirectory.resolve("sendump").toAbsolutePath().toString())
                .build();
        runProcess(mk_s2sendumpconvertCommands);
    }

    private void runMllrTransform(Path temporaryAdaptationDirectory, Path acousticModelDirectory) throws IOException {
        Path mllrSolve = Paths.get(sphinxDirectory, "sphinxtrain", "bin", "Release", "x64", "mllr_solve").toAbsolutePath();
        List<String> mllrSolveCommands = ImmutableList.<String>builder()
                .add(mllrSolve.toString())
                .add("-meanfn", acousticModelDirectory.resolve("means").toAbsolutePath().toString())
                .add("-varfn",acousticModelDirectory.resolve("variances").toAbsolutePath().toString())
                .add("-outmllrfn", acousticModelDirectory.resolve("mllr_matrix").toAbsolutePath().toString())
                .add("-accumdir",temporaryAdaptationDirectory.toAbsolutePath().toString())
                .build();
        runProcess(mllrSolveCommands);
    }

    private void runBw(Path fileIds, Path transcription, Path acousticModelDirectory, Path dictionaryFile, Path temporaryAdaptationDirectory) throws IOException {
        Path bw = Paths.get(sphinxDirectory, "sphinxtrain", "bin", "Release", "x64", "bw").toAbsolutePath();
        List<String> bwCommands = ImmutableList.<String>builder()
                .add(bw.toString())
                .add("-hmmdir", acousticModelDirectory.toAbsolutePath().toString())
                .add("-moddeffn", acousticModelDirectory.resolve("mdef").toAbsolutePath().toString())
                .add("-ts2cbfn", ".cont.")
                .add("-feat", "1s_c_d_dd")
                .add("-cmn", "current")
                .add("-lda", acousticModelDirectory.resolve("feature_transform").toAbsolutePath().toString())
                .add("-agc", "none")
                .add("-dictfn", dictionaryFile.toAbsolutePath().toString())
                .add("-ctlfn", fileIds.toAbsolutePath().toString())
                .add("-lsnfn", transcription.toAbsolutePath().toString())
                .add("-accumdir", temporaryAdaptationDirectory.toAbsolutePath().toString())
                .add("-timing", "no")
                .add("-cepdir", temporaryAdaptationDirectory.toAbsolutePath().toString())
                .build();
        runProcess(bwCommands);
    }

    private void runSphinxFe(Path temporaryAdaptationDirectory, Path fileIds, Path acousticModelDirectory) throws IOException {
        //generate acoustic feature files
        //execute sphinx_fe -argfile en-us/feat.params \
        //        -samprate 16000 -c filename.fileids \
        //       -di . -do . -ei wav -eo mfc -mswav yes
        Path stringFe = Paths.get(sphinxDirectory, "sphinxbase", "bin", "Release", "x64", "sphinx_fe").toAbsolutePath();
        List<String> stringFeCommands = ImmutableList.<String>builder()
                .add(stringFe.toString())
                .add("-argfile", acousticModelDirectory.resolve("feat.params").toAbsolutePath().toString())
                .add("-samprate", "16000")
                .add("-c", fileIds.toAbsolutePath().toString())
                .add("-di", temporaryAdaptationDirectory.toAbsolutePath().toString())
                .add("-do", temporaryAdaptationDirectory.toAbsolutePath().toString())
                .add("-ei", "wav")
                .add("-eo", "mfc")
                .add("-mswav", "yes")
                .build();
        runProcess(stringFeCommands);
    }



    private List<Path> generateWavFiles(Adaptation gotAdaptation, Path temporaryAdaptationDirectory) {
        return gotAdaptation.getTranscriptions().stream().map(transcription -> {
            //TODO remove get(0)
            byte [] data = transcription.getVoiceRecordingFiles().get(0).getData();
            Path audioPath = temporaryAdaptationDirectory.resolve(String.format("%s.wav", transcription.getId()));
            try {
                Files.write(audioPath, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return audioPath;
        }).collect(Collectors.toList());
    }

    private Path generateTemporaryTranscription(Adaptation gotAdaptation, Path temporaryAdaptationDirectory) throws IOException {
        String transcriptionLines = gotAdaptation.getTranscriptions().stream()
                .map(this::getTranscriptionLine)
                .collect(Collectors.joining("\n"));
        Path transcriptionFile = temporaryAdaptationDirectory.resolve(String.format("%s.transcription", getAdaptationIdentifier(gotAdaptation)));
        return Files.writeString(transcriptionFile, transcriptionLines + "\n");
    }

    private Path  generateTempFileids(Adaptation gotAdaptation, Path temporaryAdaptationDirectory) throws IOException {
        String fileIdsLines = gotAdaptation.getTranscriptions().stream()
                .map(Transcription::getId)
                .map(UUID::toString)
                .collect(Collectors.joining("\n"));
        Path fileIds = temporaryAdaptationDirectory.resolve(String.format("%s.fileids", getAdaptationIdentifier(gotAdaptation)));
        return Files.writeString(fileIds,  fileIdsLines + "\n");
    }

    private Adaptation getAdaptationForProcess(Long adaptationId) {
        Adaptation gotAdaptation = adaptationRepository.getOne(adaptationId);
        if(allTranscriptionsHasRecordedFiles(gotAdaptation)) {
            //TODO add exception handler
            throw new RuntimeException("all transcriptions must have voice recorded files");
        }
        return gotAdaptation;
    }

    @SneakyThrows
    private Path generateTemporaryWorkingDirectory(Adaptation gotAdaptation) {
        String adaptationIdentifier = getAdaptationIdentifier(gotAdaptation);
        return Files.createTempDirectory(adaptationIdentifier);
    }

    private String getAdaptationIdentifier(Adaptation gotAdaptation) {
        return gotAdaptation.getName() + gotAdaptation.getId();
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
