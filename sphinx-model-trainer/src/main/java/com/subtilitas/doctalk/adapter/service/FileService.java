package com.subtilitas.doctalk.adapter.service;

import com.google.common.collect.ImmutableMap;
import com.subtilitas.doctalk.adapter.model.FileHolder;
import com.subtilitas.doctalk.adapter.constants.FileSourceEnum;
import com.subtilitas.doctalk.adapter.repository.AdaptationRepository;
import com.subtilitas.doctalk.adapter.repository.VoiceRecordingFileRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Log4j2
@Transactional
public class FileService {

    private final Map<FileSourceEnum, JpaRepository<? extends FileHolder, Long>> fileHolderRepositoryMap;

    public FileService(VoiceRecordingFileRepository recordingFileRepository, AdaptationRepository adaptationRepository) {
        fileHolderRepositoryMap = ImmutableMap.<FileSourceEnum, JpaRepository<? extends FileHolder, Long>>builder()
                .put(FileSourceEnum.VOICE_RECORDING, recordingFileRepository)
                .put(FileSourceEnum.ADAPTED_MODELS, adaptationRepository)
                .build();
    }

    public byte [] getFile(FileSourceEnum fileSourceEnum, Long id) {
        FileHolder fileHolder = fileHolderRepositoryMap.get(fileSourceEnum)
                .getOne(id);
        return fileHolder.getData();
    }
}
