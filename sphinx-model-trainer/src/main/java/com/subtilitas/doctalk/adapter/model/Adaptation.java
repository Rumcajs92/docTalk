package com.subtilitas.doctalk.adapter.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
public class Adaptation {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

//    private List<Transcription> transcriptions;

//    private List<VoiceRecordingFileInfo> recordingFiles;
}
