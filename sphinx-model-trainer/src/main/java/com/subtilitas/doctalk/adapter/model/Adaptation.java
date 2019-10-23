package com.subtilitas.doctalk.adapter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "ADAPTATIONS")
@ToString(exclude = {"transcriptions"})
@EqualsAndHashCode(exclude = {"transcriptions"})
public class Adaptation implements FileHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "ADAPTED_SPEECH_MODEL_ID")
    private long adaptedSpeechModelId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ADAPTATION_TRANSCRIPTION", joinColumns = { @JoinColumn(name = "ADAPTATION_ID") }, inverseJoinColumns = { @JoinColumn(name = "TRANSCRIPTION_ID") })
    private Set<Transcription> transcriptions;

    @ManyToOne
    @JoinColumn(name="ADAPTED_SPEECH_MODEL_ID", nullable=false, insertable=false, updatable=false)
    private SpeechModel adaptedSpeechModel;

    @Lob
    private byte [] data;

    @Column
    private String path;

}
