package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.ApoioRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.ApoioResponseDTO;
import com.github.lara.sodre.ms_lactech.entities.Apoio;
import com.github.lara.sodre.ms_lactech.exceptions.DatabaseException;
import com.github.lara.sodre.ms_lactech.exceptions.ResourceNotFoundException;
import com.github.lara.sodre.ms_lactech.repositories.ApoioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApoioService {

    @Autowired
    private ApoioRepository apoioRepository;

    @Transactional(readOnly = true)
    public List<ApoioResponseDTO> findAllApoios() {

        return apoioRepository.findAll()
                .stream().map(ApoioResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public ApoioResponseDTO findApoioById(Long id) {

        Apoio apoio = apoioRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        return new ApoioResponseDTO(apoio);
    }

    @Transactional
    public ApoioResponseDTO saveApoio(ApoioRequestDTO inputDto) {

        Apoio apoio = new Apoio();
        copyDtoToApoio(inputDto, apoio);
        apoio = apoioRepository.save(apoio);
        return new ApoioResponseDTO(apoio);
    }

    @Transactional
    public ApoioResponseDTO updateApoio(Long id, ApoioRequestDTO inputDto) {

        try {
            Apoio apoio = apoioRepository.getReferenceById(id);
            copyDtoToApoio(inputDto, apoio);
            apoio = apoioRepository.save(apoio);
            return new ApoioResponseDTO(apoio);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteApoioById(Long id) {

        if (!apoioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }

        try {
            apoioRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível excluir apoio. Existem consultas ou avaliações associadas a ele");
        }
    }

    private void copyDtoToApoio(ApoioRequestDTO inputDto, Apoio apoio) {

        apoio.setNome(inputDto.getNome());
        apoio.setUsuario(inputDto.getUsuario());
        apoio.setSenha(inputDto.getSenha());
        apoio.setDataNascimento(inputDto.getDataNascimento());
        apoio.setEmail(inputDto.getEmail());
        apoio.setEndereco(inputDto.getEndereco());
        apoio.setRg(inputDto.getRg());
        apoio.setCpf(inputDto.getCpf());
        apoio.setTelefone(inputDto.getTelefone());
        apoio.setCidade(inputDto.getCidade());
        apoio.setBairro(inputDto.getBairro());
        apoio.setCep(inputDto.getCep());
    }

}