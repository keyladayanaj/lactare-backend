package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.InformacaoRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.InformacaoResponseDTO;
import com.github.lara.sodre.ms_lactech.entities.Doadora;
import com.github.lara.sodre.ms_lactech.entities.Informacao;
import com.github.lara.sodre.ms_lactech.exceptions.DatabaseException;
import com.github.lara.sodre.ms_lactech.exceptions.ResourceNotFoundException;
import com.github.lara.sodre.ms_lactech.repositories.DoadoraRepository;
import com.github.lara.sodre.ms_lactech.repositories.InformacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InformacaoService {

    @Autowired
    private InformacaoRepository informacaoRepository;

    @Autowired
    private DoadoraRepository doadoraRepository;

    @Transactional(readOnly = true)
    public List<InformacaoResponseDTO> findAllInformacoes() {

        return informacaoRepository.findAll()
                .stream().map(InformacaoResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public InformacaoResponseDTO findInformacaoById(Long id) {

        Informacao informacao = informacaoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        return new InformacaoResponseDTO(informacao);
    }

    @Transactional
    public InformacaoResponseDTO saveInformacao(InformacaoRequestDTO inputDto) {

        Informacao informacao = new Informacao();
        copyDtoToInformacao(inputDto, informacao);
        informacao = informacaoRepository.save(informacao);
        return new InformacaoResponseDTO(informacao);
    }

    @Transactional
    public InformacaoResponseDTO updateInformacao(Long id, InformacaoRequestDTO inputDto) {

        try {
            Informacao informacao = informacaoRepository.getReferenceById(id);
            copyDtoToInformacao(inputDto, informacao);
            informacao = informacaoRepository.save(informacao);
            return new InformacaoResponseDTO(informacao);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteInformacaoById(Long id) {

        if (!informacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }

        try {
            informacaoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível excluir informação");
        }
    }

    private void copyDtoToInformacao(InformacaoRequestDTO inputDto, Informacao informacao) {

        informacao.setIdadeBebe(inputDto.getIdadeBebe());
        informacao.setAmamentando(inputDto.getAmamentando());
        informacao.setUsoMedicamento(inputDto.getUsoMedicamento());
        informacao.setMetodoColeta(inputDto.getMetodoColeta());
        informacao.setAlergia(inputDto.getAlergia());
        informacao.setDoencaCronicas(inputDto.getDoencaCronicas());
        informacao.setObservacoes(inputDto.getObservacoes());
        informacao.setFuma(inputDto.getFuma());
        informacao.setAlcool(inputDto.getAlcool());
        informacao.setConsentimento(inputDto.getConsentimento());

        Doadora doadora = doadoraRepository.findById(inputDto.getDoadoraId()).orElseThrow(
                () -> new DatabaseException("Não foi possível salvar Informação. Doadora inexistente. "
                        + "(ID: " + inputDto.getDoadoraId() + ")")
        );

        // só bloqueia duplicidade quando é um cadastro novo (id == null);
        // numa atualização (id != null) a doadora já é a dona dessa mesma informação
        if (informacao.getId() == null && doadora.getInformacao() != null) {
            throw new DatabaseException("Esta doadora já possui informações de saúde cadastradas");
        }

        informacao.setDoadora(doadora);
    }
}