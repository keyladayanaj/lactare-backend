package com.github.lara.sodre.ms_lactech.repositories;

import com.github.lara.sodre.ms_lactech.entities.Triagem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TriagemRepository extends JpaRepository<Triagem, Long> {
    Optional<Triagem> findTopByDoadoraIdOrderByDataEnvioDesc(Long doadoraId);
    List<Triagem> findAllByOrderByDataEnvioDesc();
}
