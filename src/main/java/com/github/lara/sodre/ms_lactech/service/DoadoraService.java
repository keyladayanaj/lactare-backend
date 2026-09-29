package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.DoadoraRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.DoadoraResponseDTO;
import com.github.lara.sodre.ms_lactech.entities.Doadora;
import com.github.lara.sodre.ms_lactech.exceptions.DatabaseException;
import com.github.lara.sodre.ms_lactech.exceptions.ResourceNotFoundException;
import com.github.lara.sodre.ms_lactech.repositories.DoadoraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoadoraService {

    @Autowired
    private DoadoraRepository doadoraRepository;

    @Transactional(readOnly = true)
    public List<DoadoraResponseDTO> findAllDoadora() {

        return doadoraRepository.findAll()
                .stream().map(DoadoraResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public DoadoraResponseDTO findDoadoraById(Long id) {

        Doadora doadora = doadoraRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        return new DoadoraResponseDTO(doadora);
    }

    @Transactional
    public DoadoraResponseDTO saveDoadora(DoadoraRequestDTO inputDto) {

        Doadora doadora = new Doadora();
        copyDtoToDoadora(inputDto, doadora);
        doadora = doadoraRepository.save(doadora);
        return new DoadoraResponseDTO(doadora);
    }

    @Transactional
    public DoadoraResponseDTO updateDoadora(Long id, DoadoraRequestDTO inputDto) {

        try {
            Doadora doadora = doadoraRepository.getReferenceById(id);
            copyDtoToDoadora(inputDto, doadora);
            doadora = doadoraRepository.save(doadora);
            return new DoadoraResponseDTO(doadora);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteDoadoraById(Long id) {

        if (!doadoraRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }

        try {
            doadoraRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível excluir doadora. Existem coletas, consultas ou avaliações associadas a ela");
        }
    }

    private void copyDtoToDoadora(DoadoraRequestDTO inputDto, Doadora doadora) {

        doadora.setCpf(inputDto.getCpf());
        doadora.setRg(inputDto.getRg());
        doadora.setNome(inputDto.getNome());
        doadora.setDataNascimento(inputDto.getDataNascimento());
        doadora.setTelefone(inputDto.getTelefone());
        doadora.setEmail(inputDto.getEmail());
        doadora.setEndereco(inputDto.getEndereco());
        doadora.setBairro(inputDto.getBairro());
        doadora.setCidade(inputDto.getCidade());
        doadora.setCep(inputDto.getCep());
        doadora.setUsuario(inputDto.getUsuario());
        doadora.setSenha(inputDto.getSenha());
    }
}