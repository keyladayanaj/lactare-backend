package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.AuthRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.AuthResponseDTO;
import com.github.lara.sodre.ms_lactech.repositories.ApoioRepository;
import com.github.lara.sodre.ms_lactech.repositories.DoadoraRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    public record Session(String perfil, Long usuarioId, String nome) {}

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Value("${lactare.equipe.usuario}")
    private String equipeUsuario;

    @Value("${lactare.equipe.senha}")
    private String equipeSenha;

    @Value("${lactare.medico.usuario}")
    private String medicoUsuario;

    @Value("${lactare.medico.senha}")
    private String medicoSenha;

    @Value("${lactare.medico.nome:Médico Lactare}")
    private String medicoNome;

    private final DoadoraRepository doadoras;
    private final ApoioRepository apoios;

    public AuthService(DoadoraRepository doadoras, ApoioRepository apoios) {
        this.doadoras = doadoras;
        this.apoios = apoios;
    }

    public AuthResponseDTO login(AuthRequestDTO dto) {
        String usuario = dto.getUsuario() == null ? "" : dto.getUsuario().trim();
        String senha = dto.getSenha() == null ? "" : dto.getSenha();

        var d = doadoras.findAll().stream()
                .filter(x -> x.getUsuario() != null
                        && x.getSenha() != null
                        && x.getUsuario().equalsIgnoreCase(usuario)
                        && x.getSenha().equals(senha))
                .findFirst();
        if (d.isPresent()) {
            return create("DOADORA", d.get().getId(), d.get().getNome());
        }

        var a = apoios.findAll().stream()
                .filter(x -> x.getUsuario() != null
                        && x.getSenha() != null
                        && x.getUsuario().equalsIgnoreCase(usuario)
                        && x.getSenha().equals(senha))
                .findFirst();
        if (a.isPresent()) {
            return create("APOIO", a.get().getId(), a.get().getNome());
        }

        // Médico: acesso fixo, igual ao modelo já usado pela Equipe Lactare.
        // Não existe cadastro de médico no portal.
        if (usuario.equalsIgnoreCase(medicoUsuario) && senha.equals(medicoSenha)) {
            return create("MEDICO", null, medicoNome);
        }

        // Equipe Lactare: acesso fixo, sem cadastro no portal.
        if (usuario.equalsIgnoreCase(equipeUsuario) && senha.equals(equipeSenha)) {
            return create("EQUIPE", null, "Equipe Lactare");
        }

        throw new IllegalArgumentException("Usuário ou senha inválidos");
    }

    private AuthResponseDTO create(String perfil, Long id, String nome) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, new Session(perfil, id, nome));
        return new AuthResponseDTO(token, perfil, id, nome);
    }

    public Session session(String token) {
        return token == null ? null : sessions.get(token);
    }

    public void logout(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }
}
