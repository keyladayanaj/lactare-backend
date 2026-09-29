package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.ConsultaRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.ConsultaResponseDTO;
import com.github.lara.sodre.ms_lactech.entities.Apoio;
import com.github.lara.sodre.ms_lactech.entities.Consulta;
import com.github.lara.sodre.ms_lactech.entities.Doadora;
import com.github.lara.sodre.ms_lactech.entities.Profissional;
import com.github.lara.sodre.ms_lactech.exceptions.DatabaseException;
import com.github.lara.sodre.ms_lactech.exceptions.ResourceNotFoundException;
import com.github.lara.sodre.ms_lactech.repositories.ApoioRepository;
import com.github.lara.sodre.ms_lactech.repositories.ConsultaRepository;
import com.github.lara.sodre.ms_lactech.repositories.DoadoraRepository;
import com.github.lara.sodre.ms_lactech.repositories.ProfissionalRepository;
import com.github.lara.sodre.ms_lactech.repositories.TriagemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private DoadoraRepository doadoraRepository;

    @Autowired
    private ApoioRepository apoioRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private TriagemRepository triagemRepository;

    @Autowired
    private KitService kitService;

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> findAllConsultas() {

        return consultaRepository.findAll()
                .stream().map(ConsultaResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public ConsultaResponseDTO findConsultaById(Long id) {

        Consulta consulta = consultaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        return new ConsultaResponseDTO(consulta);
    }

    @Transactional
    public ConsultaResponseDTO saveConsulta(ConsultaRequestDTO inputDto) {

        Consulta consulta = new Consulta();
        copyDtoToConsulta(inputDto, consulta);
        consulta = consultaRepository.save(consulta);
        return new ConsultaResponseDTO(consulta);
    }

    @Transactional
    public ConsultaResponseDTO updateConsulta(Long id, ConsultaRequestDTO inputDto) {

        try {
            Consulta consulta = consultaRepository.getReferenceById(id);
            copyDtoToConsulta(inputDto, consulta);
            consulta = consultaRepository.save(consulta);
            return new ConsultaResponseDTO(consulta);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteConsultaById(Long id) {

        if (!consultaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }

        try {
            consultaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível excluir consulta. Existem avaliações associadas a ela");
        }
    }

    @Transactional
    public ConsultaResponseDTO agendarExame(com.github.lara.sodre.ms_lactech.dto.ExameMedicoRequestDTO dto) {
        Doadora d = doadoraRepository.findById(dto.getDoadoraId())
                .orElseThrow(() -> new ResourceNotFoundException("Doadora não encontrada. ID: " + dto.getDoadoraId()));

        var triagem = triagemRepository.findTopByDoadoraIdOrderByDataEnvioDesc(d.getId())
                .orElseThrow(() -> new DatabaseException("A doadora ainda não possui uma triagem."));
        if (!"APROVADA".equalsIgnoreCase(triagem.getStatus())) {
            throw new DatabaseException("O exame médico só pode ser agendado após a aprovação da triagem.");
        }

        var anterior = consultaRepository.findTopByDoadoraIdAndMotivoOrderByDataDescHorarioDesc(d.getId(), "EXAME_MEDICO");
        if (anterior.isPresent() && !"CANCELADA".equalsIgnoreCase(anterior.get().getStatus())
                && !"NAO_APROVADA".equalsIgnoreCase(anterior.get().getStatus())) {
            throw new DatabaseException("A doadora já possui um exame médico em andamento.");
        }

        // O acesso do médico é um usuário fixo da Equipe Lactare, não um cadastro de profissional.
        // Por isso o agendamento não cria nem depende de um profissional fictício no banco.
        Consulta c = new Consulta();
        c.setDoadora(d);
        c.setProfissional(null);
        c.setApoio(null);
        c.setData(dto.getData());
        c.setHorario(dto.getHorario());
        c.setMotivo("EXAME_MEDICO");
        c.setObservacoes(dto.getObservacoes());
        c.setStatus("AGENDADA");
        return new ConsultaResponseDTO(consultaRepository.save(c));
    }

    @Transactional(readOnly = true)
    public ConsultaResponseDTO exameDaDoadora(Long doadoraId) {
        return consultaRepository.findTopByDoadoraIdAndMotivoOrderByDataDescHorarioDesc(doadoraId, "EXAME_MEDICO")
                .map(ConsultaResponseDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Exame médico não encontrado para a doadora. ID: " + doadoraId));
    }

    @Transactional
    public ConsultaResponseDTO aprovarExame(Long id) {
        return registrarResultadoExame(id, "APROVADA");
    }

    @Transactional
    public ConsultaResponseDTO confirmarAgendamentoExame(Long id) {
        Consulta c = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada. ID: " + id));
        if (!"EXAME_MEDICO".equals(c.getMotivo())) {
            throw new DatabaseException("A consulta informada não é um exame médico");
        }
        if (!"AGENDADA".equalsIgnoreCase(c.getStatus())) {
            throw new DatabaseException("Somente exames aguardando confirmação podem ser confirmados.");
        }
        c.setStatus("CONFIRMADA");
        return new ConsultaResponseDTO(consultaRepository.save(c));
    }

    @Transactional
    public ConsultaResponseDTO realizarExame(Long id) {
        Consulta c = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada. ID: " + id));
        if (!"EXAME_MEDICO".equals(c.getMotivo())) {
            throw new DatabaseException("A consulta informada não é um exame médico");
        }
        if (!"CONFIRMADA".equalsIgnoreCase(c.getStatus())) {
            throw new DatabaseException("O exame só pode ser marcado como realizado após a confirmação do agendamento.");
        }
        c.setStatus("REALIZADA");
        return new ConsultaResponseDTO(consultaRepository.save(c));
    }

    @Transactional
    public ConsultaResponseDTO registrarResultadoExame(Long id, String status) {
        Consulta c = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada. ID: " + id));
        if (!"EXAME_MEDICO".equals(c.getMotivo())) {
            throw new DatabaseException("A consulta informada não é um exame médico");
        }
        if (!"APROVADA".equals(status) && !"NAO_APROVADA".equals(status)) {
            throw new DatabaseException("Resultado de exame inválido. Use APROVADA ou NAO_APROVADA.");
        }
        if (!"REALIZADA".equalsIgnoreCase(c.getStatus())) {
            throw new DatabaseException("O resultado só pode ser registrado depois que o exame for marcado como realizado.");
        }
        c.setStatus(status);
        Consulta saved = consultaRepository.save(c);
        if ("APROVADA".equalsIgnoreCase(status)) {
            kitService.criarParaDoadoraSeNecessario(c.getDoadora().getId());
        }
        return new ConsultaResponseDTO(saved);
    }

    private void copyDtoToConsulta(ConsultaRequestDTO inputDto, Consulta consulta) {

        consulta.setData(inputDto.getData());
        consulta.setHorario(inputDto.getHorario());
        consulta.setMotivo(inputDto.getMotivo());
        consulta.setObservacoes(inputDto.getObservacoes());
        consulta.setStatus(inputDto.getStatus());

        Doadora doadora = doadoraRepository.findById(inputDto.getDoadoraId()).orElseThrow(
                () -> new DatabaseException("Não foi possível salvar Consulta. Doadora inexistente. "
                        + "(ID: " + inputDto.getDoadoraId() + ")")
        );

        Apoio apoio = null;
        if (inputDto.getApoioId() != null) {
            apoio = apoioRepository.findById(inputDto.getApoioId()).orElseThrow(
                    () -> new DatabaseException("Não foi possível salvar Consulta. Apoio inexistente. (ID: " + inputDto.getApoioId() + ")")
            );
        }

        Profissional profissional = profissionalRepository.findById(inputDto.getProfissionalId()).orElseThrow(
                () -> new DatabaseException("Não foi possível salvar Consulta. Profissional inexistente. "
                        + "(ID: " + inputDto.getProfissionalId() + ")")
        );

        consulta.setDoadora(doadora);
        consulta.setApoio(apoio);
        consulta.setProfissional(profissional);
    }

}