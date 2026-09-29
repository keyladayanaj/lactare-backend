package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.KitResponseDTO;
import com.github.lara.sodre.ms_lactech.entities.Consulta;
import com.github.lara.sodre.ms_lactech.entities.Doadora;
import com.github.lara.sodre.ms_lactech.entities.Kit;
import com.github.lara.sodre.ms_lactech.exceptions.DatabaseException;
import com.github.lara.sodre.ms_lactech.exceptions.ResourceNotFoundException;
import com.github.lara.sodre.ms_lactech.repositories.ConsultaRepository;
import com.github.lara.sodre.ms_lactech.repositories.DoadoraRepository;
import com.github.lara.sodre.ms_lactech.repositories.KitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KitService {
    private static final String AGUARDANDO_PREPARACAO = "AGUARDANDO_PREPARACAO";
    private static final String EM_PREPARACAO = "EM_PREPARACAO";
    private static final String ENVIADO = "ENVIADO";
    private static final String ENTREGUE = "ENTREGUE";

    @Autowired private KitRepository kitRepository;
    @Autowired private DoadoraRepository doadoraRepository;
    @Autowired private ConsultaRepository consultaRepository;

    @Transactional(readOnly = true)
    public List<KitResponseDTO> findAll() {
        return kitRepository.findAll().stream().map(KitResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public KitResponseDTO findByDoadora(Long doadoraId) {
        if (!doadoraRepository.existsById(doadoraId)) {
            throw new ResourceNotFoundException("Doadora não encontrada. ID: " + doadoraId);
        }
        return kitRepository.findTopByDoadoraIdOrderByIdDesc(doadoraId)
                .map(KitResponseDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Kit de coleta ainda não disponível para esta doadora."));
    }

    @Transactional
    public Kit criarParaDoadoraSeNecessario(Long doadoraId) {
        return kitRepository.findTopByDoadoraIdOrderByIdDesc(doadoraId)
                .orElseGet(() -> {
                    Doadora d = doadoraRepository.findById(doadoraId)
                            .orElseThrow(() -> new ResourceNotFoundException("Doadora não encontrada. ID: " + doadoraId));
                    Kit kit = new Kit();
                    kit.setDoadora(d);
                    kit.setStatus(AGUARDANDO_PREPARACAO);
                    kit.setCriadoEm(LocalDateTime.now());
                    kit.setAtualizadoEm(LocalDateTime.now());
                    return kitRepository.save(kit);
                });
    }

    @Transactional
    public KitResponseDTO preparar(Long id) {
        Kit kit = buscar(id);
        exigirStatus(kit, AGUARDANDO_PREPARACAO, "Somente kits aguardando preparação podem ser preparados.");
        kit.setStatus(EM_PREPARACAO);
        return salvar(kit);
    }

    @Transactional
    public KitResponseDTO enviar(Long id) {
        Kit kit = buscar(id);
        exigirStatus(kit, EM_PREPARACAO, "O kit precisa estar em preparação antes de ser enviado.");
        kit.setStatus(ENVIADO);
        return salvar(kit);
    }

    @Transactional
    public KitResponseDTO entregar(Long id) {
        Kit kit = buscar(id);
        exigirStatus(kit, ENVIADO, "O kit precisa estar enviado antes de ser marcado como entregue.");
        kit.setStatus(ENTREGUE);
        return salvar(kit);
    }

    private KitResponseDTO salvar(Kit kit) {
        kit.setAtualizadoEm(LocalDateTime.now());
        return new KitResponseDTO(kitRepository.save(kit));
    }

    private Kit buscar(Long id) {
        return kitRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Kit não encontrado. ID: " + id));
    }

    private void exigirStatus(Kit kit, String esperado, String mensagem) {
        if (!esperado.equalsIgnoreCase(kit.getStatus())) throw new DatabaseException(mensagem);
    }

    public boolean entregueParaDoadora(Long doadoraId) {
        return kitRepository.findTopByDoadoraIdOrderByIdDesc(doadoraId)
                .map(k -> ENTREGUE.equalsIgnoreCase(k.getStatus()))
                .orElse(false);
    }
}
