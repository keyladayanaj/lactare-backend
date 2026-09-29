package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.*;
import com.github.lara.sodre.ms_lactech.entities.*;
import com.github.lara.sodre.ms_lactech.exceptions.*;
import com.github.lara.sodre.ms_lactech.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TriagemService {
    private final TriagemRepository repo;
    private final DoadoraRepository doadoraRepo;
    public TriagemService(TriagemRepository repo, DoadoraRepository doadoraRepo){this.repo=repo;this.doadoraRepo=doadoraRepo;}

    @Transactional
    public TriagemResponseDTO criar(TriagemRequestDTO dto){
        Doadora d=doadoraRepo.findById(dto.getDoadoraId()).orElseThrow(()->new ResourceNotFoundException("Doadora não encontrada. ID: "+dto.getDoadoraId()));
        Triagem t=new Triagem(); t.setDoadora(d); t.setDataEnvio(LocalDateTime.now()); t.setStatus("PENDENTE");
        t.setAmamentando(dto.getAmamentando()); t.setIdadeBebe(dto.getIdadeBebe()); t.setMedicamento(dto.getMedicamento());
        t.setFuma(dto.getFuma()); t.setExamesRecentes(dto.getExamesRecentes()); t.setJaDoou(dto.getJaDoou());
        return new TriagemResponseDTO(repo.save(t));
    }
    @Transactional(readOnly=true) public List<TriagemResponseDTO> listar(){return repo.findAllByOrderByDataEnvioDesc().stream().map(TriagemResponseDTO::new).toList();}
    @Transactional(readOnly=true) public TriagemResponseDTO porId(Long id){return new TriagemResponseDTO(repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Triagem não encontrada. ID: "+id)));}
    @Transactional(readOnly=true) public TriagemResponseDTO ultimaDaDoadora(Long id){return new TriagemResponseDTO(repo.findTopByDoadoraIdOrderByDataEnvioDesc(id).orElseThrow(()->new ResourceNotFoundException("Nenhuma triagem encontrada para a doadora. ID: "+id)));}
    @Transactional public TriagemResponseDTO alterarStatus(Long id,String status){
        Triagem t=repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Triagem não encontrada. ID: "+id));
        if(!status.equals("PENDENTE")&&!status.equals("EM_ANALISE")&&!status.equals("APROVADA")&&!status.equals("NAO_APROVADA")) throw new DatabaseException("Status de triagem inválido");
        t.setStatus(status); t.setAnalisadaEm(LocalDateTime.now()); return new TriagemResponseDTO(repo.save(t));
    }
}
