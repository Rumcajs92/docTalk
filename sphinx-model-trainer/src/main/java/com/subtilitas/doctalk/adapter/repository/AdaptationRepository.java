package com.subtilitas.doctalk.adapter.repository;

import com.subtilitas.doctalk.adapter.model.Adaptation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdaptationRepository extends JpaRepository<Adaptation, Long> {

}
