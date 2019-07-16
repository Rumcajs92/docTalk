package com.subtilitas.doctalk.adapter.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "ADAPTATION")
public class Adaptation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "ADAPTATION_TRANSCRIPTION", joinColumns = { @JoinColumn(name = "ADAPTATION_ID") }, inverseJoinColumns = { @JoinColumn(name = "TRANSCRIPTION_ID") })
    private Set<Transcription> transcriptions;

}
