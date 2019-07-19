package com.subtilitas.doctalk.adapter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "VOICE_RECORDING_FILE_INFOS")
@Data
@ToString(exclude = {"adaptation", "transcription"})
@EqualsAndHashCode(exclude = {"adaptation", "transcription"})
public class VoiceRecordingFileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private UUID transcriptionId;

    @Column
    private long adaptationId;

//    @Lob
//    private byte [] data;

    private String extension;

    @ManyToOne
    @JoinColumn(name="adaptationId", nullable=false, insertable=false, updatable=false)
    private Adaptation adaptation;

    @ManyToOne
    @JoinColumn(name="transcriptionId", nullable=false, insertable=false, updatable=false)
    private Transcription transcription;

}
