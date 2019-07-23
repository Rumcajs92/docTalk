package com.subtilitas.doctalk.adapter.repository;

import com.subtilitas.doctalk.adapter.model.VoiceRecordingFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceRecordingFileRepository extends JpaRepository<VoiceRecordingFile, Long> {
}
