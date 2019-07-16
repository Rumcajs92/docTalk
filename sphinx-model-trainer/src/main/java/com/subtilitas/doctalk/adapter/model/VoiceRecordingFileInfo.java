package com.subtilitas.doctalk.adapter.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class VoiceRecordingFileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private UUID transcriptionId;

    @Column
    private long adaptationId;

    @Column
    private String someData;

    @ManyToOne
    @JoinColumn(name="adaptationId", nullable=false, insertable=false, updatable=false)
    private Adaptation adaptation;

    @ManyToOne
    @JoinColumn(name="transcriptionId", nullable=false, insertable=false, updatable=false)
    private Transcription transcription;

}
