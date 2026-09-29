package com.github.lara.sodre.ms_lactech.repositories;

import com.github.lara.sodre.ms_lactech.entities.NotificacaoLida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoLidaRepository extends JpaRepository<NotificacaoLida, Long> {
    List<NotificacaoLida> findByDoadoraId(Long doadoraId);
}
