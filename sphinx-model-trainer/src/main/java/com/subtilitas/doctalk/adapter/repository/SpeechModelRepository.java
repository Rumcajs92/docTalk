package com.subtilitas.doctalk.adapter.repository;

import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.model.SpeechModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeechModelRepository extends JpaRepository<SpeechModel, Long> {
}
