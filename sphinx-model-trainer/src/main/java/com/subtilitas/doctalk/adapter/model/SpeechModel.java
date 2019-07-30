package com.subtilitas.doctalk.adapter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "SPEECH_MODELS")
@NoArgsConstructor
@ToString(exclude = {"adaptations"})
@EqualsAndHashCode(exclude = {"adaptations"})
public class SpeechModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String desc;

    @Column
    private String path;

    @OneToMany(mappedBy = "adaptedSpeechModel")
    private Set<Adaptation> adaptations;

}
