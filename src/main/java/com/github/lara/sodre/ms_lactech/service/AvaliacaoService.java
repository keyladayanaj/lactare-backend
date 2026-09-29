package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.AvaliacaoRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.AvaliacaoResponseDTO;
import com.github.lara.sodre.ms_lactech.entities.Apoio;
import com.github.lara.sodre.ms_lactech.entities.Avaliacao;
import com.github.lara.sodre.ms_lactech.entities.Consulta;
import com.github.lara.sodre.ms_lactech.entities.Doadora;
import com.github.lara.sodre.ms_lactech.entities.Profissional;
import com.github.lara.sodre.ms_lactech.exceptions.DatabaseException;
import com.github.lara.sodre.ms_lactech.exceptions.ResourceNotFoundException;
import com.github.lara.sodre.ms_lactech.repositories.ApoioRepository;
import com.github.lara.sodre.ms_lactech.repositories.AvaliacaoRepository;
import com.github.lara.sodre.ms_lactech.repositories.ConsultaRepository;
import com.github.lara.sodre.ms_lactech.repositories.DoadoraRepository;
import com.github.lara.sodre.ms_lactech.repositories.ProfissionalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private DoadoraRepository doadoraRepository;

    @Autowired
    private ApoioRepository apoioRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> findAllAvaliacoes() {

        return avaliacaoRepository.findAll()
                .stream().map(AvaliacaoResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponseDTO findAvaliacaoById(Long id) {

        Avaliacao avaliacao = avaliacaoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        return new AvaliacaoResponseDTO(avaliacao);
    }

    @Transactional
    public AvaliacaoResponseDTO saveAvaliacao(AvaliacaoRequestDTO inputDto) {

        Avaliacao avaliacao = new Avaliacao();
        copyDtoToAvaliacao(inputDto, avaliacao);
        avaliacao = avaliacaoRepository.save(avaliacao);
        return new AvaliacaoResponseDTO(avaliacao);
    }

    @Transactional
    public AvaliacaoResponseDTO updateAvaliacao(Long id, AvaliacaoRequestDTO inputDto) {

        try {
            Avaliacao avaliacao = avaliacaoRepository.getReferenceById(id);
            copyDtoToAvaliacao(inputDto, avaliacao);
            avaliacao = avaliacaoRepository.save(avaliacao);
            return new AvaliacaoResponseDTO(avaliacao);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteAvaliacaoById(Long id) {

        if (!avaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }

        try {
            avaliacaoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível excluir avaliação");
        }
    }

    private void copyDtoToAvaliacao(AvaliacaoRequestDTO inputDto, Avaliacao avaliacao) {

        avaliacao.setData(inputDto.getData());
        avaliacao.setNota(inputDto.getNota());
        avaliacao.setComentario(inputDto.getComentario());

        if ((inputDto.getDoadoraId() == null) == (inputDto.getApoioId() == null)) {
            throw new DatabaseException("A avaliação deve estar vinculada a uma doadora ou a um apoio, mas não aos dois ao mesmo tempo.");
        }

        Doadora doadora = inputDto.getDoadoraId() == null ? null : doadoraRepository.findById(inputDto.getDoadoraId()).orElseThrow(
                () -> new DatabaseException("Não foi possível salvar Avaliação. Doadora inexistente. (ID: " + inputDto.getDoadoraId() + ")")
        );

        Apoio apoio = inputDto.getApoioId() == null ? null : apoioRepository.findById(inputDto.getApoioId()).orElseThrow(
                () -> new DatabaseException("Não foi possível salvar Avaliação. Apoio inexistente. (ID: " + inputDto.getApoioId() + ")")
        );

        Profissional profissional = profissionalRepository.findById(inputDto.getProfissionalId()).orElseThrow(
                () -> new DatabaseException("Não foi possível salvar Avaliação. Profissional inexistente. "
                        + "(ID: " + inputDto.getProfissionalId() + ")")
        );

        Consulta consulta = consultaRepository.findById(inputDto.getConsultaId()).orElseThrow(
                () -> new DatabaseException("Não foi possível salvar Avaliação. Consulta inexistente. "
                        + "(ID: " + inputDto.getConsultaId() + ")")
        );

        avaliacao.setDoadora(doadora);
        avaliacao.setApoio(apoio);
        avaliacao.setProfissional(profissional);
        avaliacao.setConsulta(consulta);
    }

}