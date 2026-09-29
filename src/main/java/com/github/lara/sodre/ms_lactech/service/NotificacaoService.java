package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.NotificacaoLidasResponseDTO;
import com.github.lara.sodre.ms_lactech.entities.Doadora;
import com.github.lara.sodre.ms_lactech.entities.NotificacaoLida;
import com.github.lara.sodre.ms_lactech.exceptions.ResourceNotFoundException;
import com.github.lara.sodre.ms_lactech.repositories.DoadoraRepository;
import com.github.lara.sodre.ms_lactech.repositories.NotificacaoLidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoLidaRepository notificacaoLidaRepository;

    @Autowired
    private DoadoraRepository doadoraRepository;

    @Transactional(readOnly = true)
    public NotificacaoLidasResponseDTO lidas(Long doadoraId) {
        if (!doadoraRepository.existsById(doadoraId)) {
            throw new ResourceNotFoundException("Doadora não encontrada. ID: " + doadoraId);
        }
        return new NotificacaoLidasResponseDTO(chavesLidas(doadoraId));
    }

    /** Idempotente: chaves já registradas como lidas são ignoradas. */
    @Transactional
    public NotificacaoLidasResponseDTO marcarLidas(Long doadoraId, List<String> chaves) {
        Doadora doadora = doadoraRepository.findById(doadoraId)
                .orElseThrow(() -> new ResourceNotFoundException("Doadora não encontrada. ID: " + doadoraId));

        Set<String> existentes = new HashSet<>(chavesLidas(doadoraId));
        for (String bruta : chaves) {
            String chave = bruta == null ? "" : bruta.trim();
            if (chave.isEmpty() || !existentes.add(chave)) continue;
            NotificacaoLida n = new NotificacaoLida();
            n.setDoadora(doadora);
            n.setChave(chave);
            notificacaoLidaRepository.save(n);
        }
        return new NotificacaoLidasResponseDTO(chavesLidas(doadoraId));
    }

    private List<String> chavesLidas(Long doadoraId) {
        return notificacaoLidaRepository.findByDoadoraId(doadoraId)
                .stream().map(NotificacaoLida::getChave).toList();
    }
}
