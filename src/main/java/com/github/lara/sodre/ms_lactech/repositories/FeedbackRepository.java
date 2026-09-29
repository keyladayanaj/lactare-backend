package com.github.lara.sodre.ms_lactech.repositories;

import com.github.lara.sodre.ms_lactech.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByDoadoraIdOrderByDataCriacaoDesc(Long doadoraId);
    boolean existsByDoadoraIdAndColetaId(Long doadoraId, Long coletaId);
}
