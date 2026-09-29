package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.ProfissionalRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.ProfissionalResponseDTO;
import com.github.lara.sodre.ms_lactech.entities.Profissional;
import com.github.lara.sodre.ms_lactech.exceptions.DatabaseException;
import com.github.lara.sodre.ms_lactech.exceptions.ResourceNotFoundException;
import com.github.lara.sodre.ms_lactech.repositories.ProfissionalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Transactional(readOnly = true)
    public List<ProfissionalResponseDTO> findAllProfissionais() {

        return profissionalRepository.findAll()
                .stream().map(ProfissionalResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public ProfissionalResponseDTO findProfissionalById(Long id) {

        Profissional profissional = profissionalRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        return new ProfissionalResponseDTO(profissional);
    }

    @Transactional
    public ProfissionalResponseDTO saveProfissional(ProfissionalRequestDTO inputDto) {

        Profissional profissional = new Profissional();
        copyDtoToProfissional(inputDto, profissional);
        profissional = profissionalRepository.save(profissional);
        return new ProfissionalResponseDTO(profissional);
    }

    @Transactional
    public ProfissionalResponseDTO updateProfissional(Long id, ProfissionalRequestDTO inputDto) {

        try {
            Profissional profissional = profissionalRepository.getReferenceById(id);
            copyDtoToProfissional(inputDto, profissional);
            profissional = profissionalRepository.save(profissional);
            return new ProfissionalResponseDTO(profissional);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteProfissionalById(Long id) {

        if (!profissionalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }

        try {
            profissionalRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível excluir profissional. Existem consultas ou avaliações associadas a ele");
        }
    }

    private void copyDtoToProfissional(ProfissionalRequestDTO inputDto, Profissional profissional) {

        profissional.setNome(inputDto.getNome());
        profissional.setEmail(inputDto.getEmail());
        profissional.setTelefone(inputDto.getTelefone());
        profissional.setCargo(inputDto.getCargo());
    }
}