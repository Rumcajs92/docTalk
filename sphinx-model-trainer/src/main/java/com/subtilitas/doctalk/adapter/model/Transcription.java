package com.subtilitas.doctalk.adapter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "TRANSCRIPTIONS")
@NoArgsConstructor
@ToString(exclude = {"adaptations", "voiceRecordingFiles"})
@EqualsAndHashCode(exclude = {"adaptations", "voiceRecordingFiles"})
public class Transcription {

    public Transcription(String transcriptionText) {
        this.transcriptionText = transcriptionText;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column
    private String transcriptionText;

    @JsonIgnore
    @ManyToMany(mappedBy = "transcriptions")
    private Set<Adaptation> adaptations;

    @OneToMany(mappedBy = "transcription")
    private Set<VoiceRecordingFile> voiceRecordingFiles;

}
