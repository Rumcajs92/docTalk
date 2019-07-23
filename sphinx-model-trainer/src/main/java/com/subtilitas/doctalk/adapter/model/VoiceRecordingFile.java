package com.subtilitas.doctalk.adapter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.print.attribute.standard.MediaSize;
import java.util.UUID;

@Entity(name = "VOICE_RECORDING_FILE_INFOS")
@Data
@ToString(exclude = {/*"adaptation", */"transcription"})
@EqualsAndHashCode(exclude = {/*"adaptation", */"transcription"})
public class VoiceRecordingFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "TRANSCRIPTION_ID")
    private UUID transcriptionId;

    @Column(name = "ADAPTATION_ID")
    private long adaptationId;

    @Column
    private String path;

    @Lob
    private byte [] data;

    @Column
    private String extension;

    @ManyToOne
    @JoinColumn(name="ADAPTATION_ID", nullable=false, insertable=false, updatable=false)
    private Adaptation adaptation;

    @ManyToOne
    @JoinColumn(name="TRANSCRIPTION_ID", nullable=false, insertable=false, updatable=false)
    private Transcription transcription;

}
