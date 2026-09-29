package com.github.lara.sodre.ms_lactech.repositories;

import com.github.lara.sodre.ms_lactech.entities.Kit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KitRepository extends JpaRepository<Kit, Long> {
    Optional<Kit> findTopByDoadoraIdOrderByIdDesc(Long doadoraId);
}
