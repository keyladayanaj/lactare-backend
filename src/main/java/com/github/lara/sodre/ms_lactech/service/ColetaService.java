package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.ColetaRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.ColetaResponseDTO;
import com.github.lara.sodre.ms_lactech.entities.Coleta;
import com.github.lara.sodre.ms_lactech.entities.Doadora;
import com.github.lara.sodre.ms_lactech.exceptions.DatabaseException;
import com.github.lara.sodre.ms_lactech.exceptions.ResourceNotFoundException;
import com.github.lara.sodre.ms_lactech.repositories.ColetaRepository;
import com.github.lara.sodre.ms_lactech.repositories.DoadoraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ColetaService {

    @Autowired
    private ColetaRepository coletaRepository;

    @Autowired
    private DoadoraRepository doadoraRepository;

    @Autowired
    private com.github.lara.sodre.ms_lactech.repositories.ConsultaRepository consultaRepository;

    @Autowired
    private KitService kitService;

    @Transactional(readOnly = true)
    public List<ColetaResponseDTO> findAllColeta() {

        return coletaRepository.findAll()
                .stream().map(ColetaResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public List<ColetaResponseDTO> findByDoadora(Long doadoraId) {
        if(!doadoraRepository.existsById(doadoraId)) throw new ResourceNotFoundException("Doadora não encontrada. ID: "+doadoraId);
        return coletaRepository.findByDoadoraIdOrderByDataDesc(doadoraId).stream().map(ColetaResponseDTO::new).toList();
    }

    @Transactional
    public ColetaResponseDTO agendar(com.github.lara.sodre.ms_lactech.dto.AgendamentoColetaRequestDTO dto) {
        var exame=consultaRepository.findTopByDoadoraIdAndMotivoOrderByDataDescHorarioDesc(dto.getDoadoraId(), "EXAME_MEDICO");
        if(exame.isEmpty() || !"APROVADA".equalsIgnoreCase(exame.get().getStatus())) throw new DatabaseException("A coleta só pode ser agendada após a aprovação do exame médico.");
        if (!kitService.entregueParaDoadora(dto.getDoadoraId())) throw new DatabaseException("A coleta só pode ser agendada depois que o kit de coleta for entregue.");
        Coleta c=new Coleta(); c.setDoadora(doadoraRepository.findById(dto.getDoadoraId()).orElseThrow(() -> new ResourceNotFoundException("Doadora não encontrada."))); c.setData(dto.getData()); c.setTurno(dto.getTurno()); c.setObservacoes(dto.getObservacoes()); c.setStatus("AGENDADA"); c.setVolume(null); return new ColetaResponseDTO(coletaRepository.save(c));
    }

    @Transactional
    public ColetaResponseDTO confirmar(Long id) {
        Coleta coleta = coletaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Coleta não encontrada. ID: " + id)
        );
        String status = coleta.getStatus() == null ? "" : coleta.getStatus().toUpperCase();
        if (!"AGENDADA".equals(status) && !"PENDENTE".equals(status)) {
            throw new DatabaseException("A coleta só pode ser confirmada quando estiver agendada.");
        }
        coleta.setStatus("CONFIRMADA");
        return new ColetaResponseDTO(coletaRepository.save(coleta));
    }

    @Transactional
    public ColetaResponseDTO realizar(Long id) {
        Coleta coleta = coletaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Coleta não encontrada. ID: " + id)
        );
        if (!"CONFIRMADA".equalsIgnoreCase(coleta.getStatus())) {
            throw new DatabaseException("A coleta só pode ser marcada como realizada depois de ser confirmada.");
        }
        coleta.setStatus("REALIZADA");
        return new ColetaResponseDTO(coletaRepository.save(coleta));
    }

    @Transactional(readOnly = true)
    public ColetaResponseDTO findColetaById(Long id) {

        Coleta coleta = coletaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        return new ColetaResponseDTO(coleta);
    }

    @Transactional
    public ColetaResponseDTO saveColeta(ColetaRequestDTO inputDto) {

        Coleta coleta = new Coleta();
        copyDtoToColeta(inputDto, coleta);
        coleta = coletaRepository.save(coleta);
        return new ColetaResponseDTO(coleta);
    }

    @Transactional
    public ColetaResponseDTO updateColeta(Long id, ColetaRequestDTO inputDto) {

        try {
            Coleta coleta = coletaRepository.getReferenceById(id);
            copyDtoToColeta(inputDto, coleta);
            coleta = coletaRepository.save(coleta);
            return new ColetaResponseDTO(coleta);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteColetaById(Long id) {

        if (!coletaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }

        try {
            coletaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível excluir coleta");
        }
    }

    private void copyDtoToColeta(ColetaRequestDTO inputDto, Coleta coleta) {

        coleta.setData(inputDto.getData());
        coleta.setVolume(inputDto.getVolume());
        coleta.setStatus(inputDto.getStatus());
        coleta.setTurno(inputDto.getTurno());
        if ("AGENDADA".equalsIgnoreCase(inputDto.getStatus())) {
            var exame = consultaRepository.findTopByDoadoraIdAndMotivoOrderByDataDescHorarioDesc(inputDto.getDoadoraId(), "EXAME_MEDICO");
            if (exame.isEmpty() || !"APROVADA".equalsIgnoreCase(exame.get().getStatus())) {
                throw new DatabaseException("A coleta só pode ser agendada após a aprovação do exame médico.");
            }
            if (!kitService.entregueParaDoadora(inputDto.getDoadoraId())) {
                throw new DatabaseException("A coleta só pode ser agendada depois que o kit de coleta for entregue.");
            }
        }
        coleta.setObservacoes(inputDto.getObservacoes());

        Doadora doadora = doadoraRepository.findById(inputDto.getDoadoraId()).orElseThrow(
                () -> new DatabaseException("Não foi possível salvar Coleta. Doadora inexistente. "
                        + "(ID: " + inputDto.getDoadoraId() + ")")
        );

        coleta.setDoadora(doadora);
    }
}