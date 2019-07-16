package com.subtilitas.doctalk.adapter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "TRANSCRIPTION")
@NoArgsConstructor
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

    @ManyToMany(mappedBy = "transcriptions")
    private Set<Adaptation> adaptations;

    @OneToMany(mappedBy = "transcription")
    private Set<VoiceRecordingFileInfo> voiceRecordingFileInfos;

}
