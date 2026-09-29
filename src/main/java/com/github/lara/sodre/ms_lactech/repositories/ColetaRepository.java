package com.github.lara.sodre.ms_lactech.repositories;
import com.github.lara.sodre.ms_lactech.entities.Coleta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ColetaRepository extends JpaRepository<Coleta,Long>{ List<Coleta> findByDoadoraIdOrderByDataDesc(Long doadoraId); }
