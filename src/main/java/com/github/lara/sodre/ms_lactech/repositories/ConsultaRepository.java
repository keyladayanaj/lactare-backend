package com.github.lara.sodre.ms_lactech.repositories;
import com.github.lara.sodre.ms_lactech.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ConsultaRepository extends JpaRepository<Consulta,Long>{
 List<Consulta> findByDoadoraIdOrderByDataDescHorarioDesc(Long doadoraId);
 Optional<Consulta> findTopByDoadoraIdAndMotivoOrderByDataDescHorarioDesc(Long doadoraId,String motivo);
}
