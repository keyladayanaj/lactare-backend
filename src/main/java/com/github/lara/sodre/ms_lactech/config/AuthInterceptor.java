package com.github.lara.sodre.ms_lactech.config;

import com.github.lara.sodre.ms_lactech.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService auth;

    public AuthInterceptor(AuthService auth) {
        this.auth = auth;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(req.getMethod()) || req.getRequestURI().startsWith("/api/v1/auth/")) {
            return true;
        }

        String uri = req.getRequestURI();
        String h = req.getHeader("Authorization");
        String token = h != null && h.startsWith("Bearer ") ? h.substring(7) : null;
        AuthService.Session session = auth.session(token);

        if (session == null && requiresAuthentication(req, uri)) {
            return unauthorized(res);
        }
        if (session == null) {
            return true;
        }

        // Cadastros de novas Doadoras e da Rede de Apoio são públicos.
        if ("POST".equalsIgnoreCase(req.getMethod()) && ("/api/v1/doadoras".equals(uri) || "/api/v1/apoios".equals(uri))) {
            return true;
        }

        // ------------------------------------------------------------------
        // TRIAGENS — análise é responsabilidade exclusiva do MÉDICO.
        // ------------------------------------------------------------------
        if (uri.startsWith("/api/v1/triagens")) {
            if ("POST".equalsIgnoreCase(req.getMethod())) {
                return role(res, session, "DOADORA");
            }
            if ("GET".equalsIgnoreCase(req.getMethod()) && uri.matches("/api/v1/triagens/doadora/\\d+")) {
                Long id = pathId(uri);
                return ownerOrDoadoraOrMedico(res, session, id);
            }
            if ("GET".equalsIgnoreCase(req.getMethod()) && uri.matches("/api/v1/triagens/\\d+")) {
                return role(res, session, "MEDICO");
            }
            if ("GET".equalsIgnoreCase(req.getMethod()) && "/api/v1/triagens".equals(uri)) {
                return role(res, session, "MEDICO");
            }
            if ("PATCH".equalsIgnoreCase(req.getMethod())) {
                return role(res, session, "MEDICO");
            }
        }

        // ------------------------------------------------------------------
        // REDE DE APOIO — regras preservadas exatamente como estavam
        // (Equipe Lactare e Médico continuam com o mesmo acesso de antes).
        // ------------------------------------------------------------------
        if (uri.startsWith("/api/v1/apoios")) {
            if ("POST".equalsIgnoreCase(req.getMethod()) && "/api/v1/apoios".equals(uri)) return true;
            if ("GET".equalsIgnoreCase(req.getMethod()) && uri.matches("/api/v1/apoios/\\d+")) {
                return ownerOrEquipeOuMedicoApoio(res, session, pathId(uri));
            }
            if ("GET".equalsIgnoreCase(req.getMethod()) && "/api/v1/apoios".equals(uri)) return equipeOuMedico(res, session);
            if ("PUT".equalsIgnoreCase(req.getMethod()) || "DELETE".equalsIgnoreCase(req.getMethod())) return role(res, session, "APOIO");
        }

        // ------------------------------------------------------------------
        // DOADORAS — visão geral da plataforma pertence à EQUIPE LACTARE.
        // ------------------------------------------------------------------
        if ("/api/v1/doadoras".equals(uri)) {
            if ("GET".equalsIgnoreCase(req.getMethod())) return role(res, session, "EQUIPE");
            if ("POST".equalsIgnoreCase(req.getMethod())) return true;
        }

        if (uri.startsWith("/api/v1/feedbacks")) {
            if ("POST".equalsIgnoreCase(req.getMethod()) && "/api/v1/feedbacks".equals(uri)) {
                if (!role(res, session, "DOADORA")) return false;
                req.setAttribute("lactareDoadoraId", session.usuarioId());
                return true;
            }
            if ("GET".equalsIgnoreCase(req.getMethod()) && uri.matches("/api/v1/feedbacks/doadora/\\d+")) {
                return ownerOrForbidden(res, session, pathId(uri));
            }
        }

        if (uri.startsWith("/api/v1/notificacoes")) {
            // Estado lida/não lida pertence somente à própria doadora logada.
            if (uri.matches("/api/v1/notificacoes/doadora/\\d+/lidas")) {
                return ownerOrForbidden(res, session, pathId(uri));
            }
            return forbidden(res);
        }

        if (uri.startsWith("/api/v1/doadoras/")) {
            Long id = pathId(uri);
            if (isEquipe(session)) return true;
            return ownerOrForbidden(res, session, id);
        }

        // ------------------------------------------------------------------
        // EXAME MÉDICO — a doadora agenda; o MÉDICO confirma, realiza,
        // registra resultado e aprova. Equipe Lactare NÃO executa essas ações.
        // ------------------------------------------------------------------
        if (uri.contains("/consultas/exame-medico") || uri.matches(".*/consultas/doadora/\\d+/exame-medico")
                || uri.matches(".*/consultas/\\d+/(aprovar-exame|confirmar-exame|realizar-exame|resultado-exame)")) {
            if ("POST".equalsIgnoreCase(req.getMethod()) && "/api/v1/consultas/exame-medico".equals(uri)) {
                return role(res, session, "DOADORA");
            }
            if ("GET".equalsIgnoreCase(req.getMethod()) && uri.matches(".*/consultas/doadora/\\d+/exame-medico")) {
                // Leitura do exame da doadora: própria doadora, Médico ou Equipe (leitura compartilhada).
                return ownerOrEquipeOuMedico(res, session, pathId(uri));
            }
            if ("PUT".equalsIgnoreCase(req.getMethod())
                    && uri.matches(".*/consultas/\\d+/(aprovar-exame|confirmar-exame|realizar-exame|resultado-exame)")) {
                return role(res, session, "MEDICO");
            }
            return forbidden(res);
        }

        // ------------------------------------------------------------------
        // CONSULTAS (CRUD genérico) — leitura compartilhada (o Médico usa na
        // agenda de exames e a Equipe nos indicadores do painel). Escrita é do
        // MÉDICO, pois o CRUD genérico permite alterar o status do exame.
        // ------------------------------------------------------------------
        if ("/api/v1/consultas".equals(uri) || uri.matches("/api/v1/consultas/\\d+")) {
            if ("GET".equalsIgnoreCase(req.getMethod())) return equipeOuMedico(res, session);
            return role(res, session, "MEDICO");
        }

        // ------------------------------------------------------------------
        // KITS — operação exclusiva da EQUIPE LACTARE.
        // ------------------------------------------------------------------
        if (uri.startsWith("/api/v1/kits/doadora/")) {
            return ownerOrEquipe(res, session, pathId(uri));
        }
        if ("/api/v1/kits".equals(uri)) {
            return role(res, session, "EQUIPE");
        }
        if (uri.matches("/api/v1/kits/\\d+/(preparar|enviar|entregar)")) {
            return role(res, session, "EQUIPE");
        }

        // ------------------------------------------------------------------
        // COLETAS — operação exclusiva da EQUIPE LACTARE (a doadora agenda).
        // ------------------------------------------------------------------
        if (uri.startsWith("/api/v1/coletas/doadora/")) {
            return ownerOrEquipe(res, session, pathId(uri));
        }
        if ("/api/v1/coletas".equals(uri)) {
            return role(res, session, "EQUIPE");
        }
        if (uri.matches("/api/v1/coletas/\\d+")) return role(res, session, "EQUIPE");
        if (uri.matches("/api/v1/coletas/\\d+/(confirmar|realizar)")) {
            return role(res, session, "EQUIPE");
        }
        if (uri.equals("/api/v1/coletas/agendamento")) {
            return role(res, session, "DOADORA");
        }


        return true;
    }

    private boolean requiresAuthentication(HttpServletRequest req, String uri) {
        if ("POST".equalsIgnoreCase(req.getMethod()) && ("/api/v1/doadoras".equals(uri) || "/api/v1/apoios".equals(uri))) return false;
        return uri.startsWith("/api/v1/triagens")
                || uri.startsWith("/api/v1/apoios")
                || uri.startsWith("/api/v1/doadoras/")
                || uri.startsWith("/api/v1/feedbacks")
                || uri.startsWith("/api/v1/notificacoes")
                || uri.startsWith("/api/v1/coletas")
                || uri.startsWith("/api/v1/consultas")
                || uri.startsWith("/api/v1/kits");
    }

    private Long pathId(String uri) {
        String[] parts = uri.split("/");
        for (int i = parts.length - 1; i >= 0; i--) {
            try { return Long.valueOf(parts[i]); } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    // ----------------------------------------------------------------------
    // Perfis. MEDICO e EQUIPE LACTARE são perfis DISTINTOS: nenhum é
    // considerado o outro. EQUIPE_LACTARE é apenas o nome legado do mesmo
    // perfil EQUIPE (sessões antigas), por isso é aceito como Equipe.
    // ----------------------------------------------------------------------
    private String perfil(AuthService.Session s) {
        if (s == null || s.perfil() == null) return "";
        return s.perfil().trim().toUpperCase();
    }

    private boolean isEquipe(AuthService.Session s) {
        String p = perfil(s);
        return "EQUIPE".equals(p) || "EQUIPE_LACTARE".equals(p);
    }

    private boolean isMedico(AuthService.Session s) {
        return "MEDICO".equals(perfil(s));
    }

    private boolean isDoadoraDona(AuthService.Session s, Long id) {
        return "DOADORA".equals(perfil(s)) && id != null && id.equals(s.usuarioId());
    }

    /** Leitura compartilhada, declarada explicitamente: Equipe Lactare OU Médico. */
    private boolean equipeOuMedico(HttpServletResponse res, AuthService.Session s) throws IOException {
        if (isEquipe(s) || isMedico(s)) return true;
        return forbidden(res);
    }

    private boolean ownerOrDoadoraOrMedico(HttpServletResponse res, AuthService.Session s, Long id) throws IOException {
        if (isDoadoraDona(s, id) || isMedico(s)) return true;
        return forbidden(res);
    }

    private boolean ownerOrEquipeOuMedico(HttpServletResponse res, AuthService.Session s, Long id) throws IOException {
        if (isEquipe(s) || isMedico(s) || isDoadoraDona(s, id)) return true;
        return forbidden(res);
    }

    private boolean ownerOrEquipe(HttpServletResponse res, AuthService.Session s, Long id) throws IOException {
        if (isDoadoraDona(s, id) || isEquipe(s)) return true;
        return forbidden(res);
    }

    /** Rede de Apoio: regra original preservada (Equipe/Médico continuam com o mesmo acesso de antes). */
    private boolean ownerOrEquipeOuMedicoApoio(HttpServletResponse res, AuthService.Session s, Long id) throws IOException {
        if (isEquipe(s) || isMedico(s) || ("APOIO".equals(perfil(s)) && id != null && id.equals(s.usuarioId()))) return true;
        return forbidden(res);
    }

    private boolean ownerOrForbidden(HttpServletResponse res, AuthService.Session s, Long id) throws IOException {
        if (isDoadoraDona(s, id)) return true;
        return forbidden(res);
    }

    private boolean ownerOrForbidden(HttpServletResponse res, AuthService.Session s, String id) throws IOException {
        try { return ownerOrForbidden(res, s, Long.valueOf(id)); } catch (Exception e) { return forbidden(res); }
    }

    private boolean role(HttpServletResponse res, AuthService.Session s, String expected) throws IOException {
        if (s == null || s.perfil() == null) return forbidden(res);

        String esperado = expected == null ? "" : expected.trim().toUpperCase();
        boolean ok;
        switch (esperado) {
            case "EQUIPE" -> ok = isEquipe(s);   // somente Equipe Lactare (inclui o nome legado EQUIPE_LACTARE)
            case "MEDICO" -> ok = isMedico(s);   // somente Médico
            default -> ok = esperado.equals(perfil(s));
        }
        return ok || forbidden(res);
    }

    private boolean unauthorized(HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setContentType("application/json");
        res.getWriter().write("{\"message\":\"Sessão inválida ou token ausente.\"}");
        return false;
    }

    private boolean forbidden(HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.FORBIDDEN.value());
        res.setContentType("application/json");
        res.getWriter().write("{\"message\":\"Acesso não permitido para este perfil ou recurso.\"}");
        return false;
    }
}
