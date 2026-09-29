package com.github.lara.sodre.ms_lactech.config;

import com.github.lara.sodre.ms_lactech.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Proxy;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Auditoria das regras do AuthInterceptor: MÉDICO e EQUIPE LACTARE são perfis distintos.
 * Usa o AuthInterceptor real; somente a leitura da sessão é substituída (sem banco/Spring).
 */
class AuthInterceptorPermissoesTest {

    private static final int OK = 200, NAO_AUTENTICADO = 401, PROIBIDO = 403;

    private static final Map<String, AuthService.Session> SESSOES = Map.of(
            "t-medico", new AuthService.Session("MEDICO", null, "Médico Lactare"),
            "t-equipe", new AuthService.Session("EQUIPE", null, "Equipe Lactare"),
            "t-equipe-legado", new AuthService.Session("EQUIPE_LACTARE", null, "Equipe Lactare"),
            "t-doadora-7", new AuthService.Session("DOADORA", 7L, "Doadora 7"),
            "t-doadora-8", new AuthService.Session("DOADORA", 8L, "Doadora 8"),
            "t-apoio-5", new AuthService.Session("APOIO", 5L, "Apoio 5"));

    private final AuthInterceptor interceptor = new AuthInterceptor(new AuthService(null, null) {
        @Override
        public Session session(String token) {
            return token == null ? null : SESSOES.get(token);
        }
    });

    /** Executa o interceptor real e devolve 200 (liberado) ou o status HTTP escrito (401/403). */
    private int chamar(String token, String metodo, String uri) throws Exception {
        int[] status = {OK};
        StringWriter corpo = new StringWriter();
        HttpServletRequest req = (HttpServletRequest) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{HttpServletRequest.class}, (p, m, a) -> switch (m.getName()) {
                    case "getMethod" -> metodo;
                    case "getRequestURI" -> uri;
                    case "getHeader" -> "Authorization".equals(a[0]) && token != null ? "Bearer " + token : null;
                    case "setAttribute" -> null;
                    default -> throw new UnsupportedOperationException(m.getName());
                });
        HttpServletResponse res = (HttpServletResponse) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{HttpServletResponse.class}, (p, m, a) -> switch (m.getName()) {
                    case "setStatus" -> { status[0] = (Integer) a[0]; yield null; }
                    case "setContentType" -> null;
                    case "getWriter" -> new PrintWriter(corpo);
                    default -> throw new UnsupportedOperationException(m.getName());
                });
        boolean liberado = interceptor.preHandle(req, res, new Object());
        if (liberado) assertEquals(OK, status[0], "liberado não deve escrever status de erro");
        else if (status[0] == OK) throw new AssertionError("bloqueado sem status de erro: " + metodo + " " + uri);
        return status[0];
    }

    private void esperar(int esperado, String token, String metodo, String uri) throws Exception {
        assertEquals(esperado, chamar(token, metodo, uri), token + " " + metodo + " " + uri);
    }

    // ---------------------------------------------------------------- TESTE 1 — MÉDICO
    @Test
    void medicoAcessaFuncionalidadesMedicas() throws Exception {
        String m = "t-medico";
        esperar(OK, m, "GET", "/api/v1/triagens");
        esperar(OK, m, "GET", "/api/v1/triagens/15");                 // analisar triagem
        esperar(OK, m, "PATCH", "/api/v1/triagens/15/status");        // aprovar / não aprovar
        esperar(OK, m, "GET", "/api/v1/triagens/doadora/7");
        esperar(OK, m, "GET", "/api/v1/consultas");                   // agendamentos de exame
        esperar(OK, m, "GET", "/api/v1/consultas/3");
        esperar(OK, m, "GET", "/api/v1/consultas/doadora/7/exame-medico");
        esperar(OK, m, "PUT", "/api/v1/consultas/3/confirmar-exame");
        esperar(OK, m, "PUT", "/api/v1/consultas/3/realizar-exame");
        esperar(OK, m, "PUT", "/api/v1/consultas/3/resultado-exame"); // registrar resultado / aprovar / não aprovar
        esperar(OK, m, "PUT", "/api/v1/consultas/3/aprovar-exame");
    }

    @Test
    void medicoNaoRecebeOperacoesExclusivasDaEquipe() throws Exception {
        String m = "t-medico";
        esperar(PROIBIDO, m, "GET", "/api/v1/kits");
        esperar(PROIBIDO, m, "GET", "/api/v1/kits/doadora/7");
        esperar(PROIBIDO, m, "PUT", "/api/v1/kits/4/preparar");
        esperar(PROIBIDO, m, "PUT", "/api/v1/kits/4/enviar");
        esperar(PROIBIDO, m, "PUT", "/api/v1/kits/4/entregar");
        esperar(PROIBIDO, m, "GET", "/api/v1/coletas");
        esperar(PROIBIDO, m, "GET", "/api/v1/coletas/doadora/7");
        esperar(PROIBIDO, m, "GET", "/api/v1/coletas/9");
        esperar(PROIBIDO, m, "PUT", "/api/v1/coletas/9/confirmar");
        esperar(PROIBIDO, m, "PUT", "/api/v1/coletas/9/realizar");
        esperar(PROIBIDO, m, "GET", "/api/v1/doadoras");
        esperar(PROIBIDO, m, "GET", "/api/v1/doadoras/7");
    }

    // ---------------------------------------------------------------- TESTE 2 — EQUIPE LACTARE
    @Test
    void equipeAcessaFuncionalidadesOperacionais() throws Exception {
        for (String e : new String[]{"t-equipe", "t-equipe-legado"}) {
            esperar(OK, e, "GET", "/api/v1/kits");
            esperar(OK, e, "GET", "/api/v1/kits/doadora/7");
            esperar(OK, e, "PUT", "/api/v1/kits/4/preparar");
            esperar(OK, e, "PUT", "/api/v1/kits/4/enviar");
            esperar(OK, e, "PUT", "/api/v1/kits/4/entregar");
            esperar(OK, e, "GET", "/api/v1/coletas");
            esperar(OK, e, "GET", "/api/v1/coletas/doadora/7");
            esperar(OK, e, "PUT", "/api/v1/coletas/9/confirmar");
            esperar(OK, e, "PUT", "/api/v1/coletas/9/realizar");
            esperar(OK, e, "GET", "/api/v1/doadoras");
            esperar(OK, e, "GET", "/api/v1/doadoras/7");
            esperar(OK, e, "GET", "/api/v1/consultas");               // indicadores do painel
        }
    }

    @Test
    void equipeNaoRecebePermissoesExclusivasDoMedico() throws Exception {
        for (String e : new String[]{"t-equipe", "t-equipe-legado"}) {
            esperar(PROIBIDO, e, "GET", "/api/v1/triagens");
            esperar(PROIBIDO, e, "GET", "/api/v1/triagens/15");
            esperar(PROIBIDO, e, "PATCH", "/api/v1/triagens/15/status");
            esperar(PROIBIDO, e, "GET", "/api/v1/triagens/doadora/7");
            esperar(PROIBIDO, e, "PUT", "/api/v1/consultas/3/confirmar-exame");
            esperar(PROIBIDO, e, "PUT", "/api/v1/consultas/3/realizar-exame");
            esperar(PROIBIDO, e, "PUT", "/api/v1/consultas/3/resultado-exame");
            esperar(PROIBIDO, e, "PUT", "/api/v1/consultas/3/aprovar-exame");
            esperar(PROIBIDO, e, "POST", "/api/v1/consultas");
            esperar(PROIBIDO, e, "PUT", "/api/v1/consultas/3");
            esperar(PROIBIDO, e, "DELETE", "/api/v1/consultas/3");
        }
    }

    // ---------------------------------------------------------------- Doadora / anônimo / lacunas antigas
    @Test
    void doadoraSoAcessaOQueEDela() throws Exception {
        esperar(OK, "t-doadora-7", "POST", "/api/v1/triagens");
        esperar(OK, "t-doadora-7", "GET", "/api/v1/triagens/doadora/7");
        esperar(PROIBIDO, "t-doadora-7", "GET", "/api/v1/triagens/doadora/8");
        esperar(OK, "t-doadora-7", "POST", "/api/v1/consultas/exame-medico");
        esperar(OK, "t-doadora-7", "GET", "/api/v1/consultas/doadora/7/exame-medico");
        esperar(PROIBIDO, "t-doadora-7", "GET", "/api/v1/consultas/doadora/8/exame-medico");
        esperar(OK, "t-doadora-7", "GET", "/api/v1/kits/doadora/7");
        esperar(PROIBIDO, "t-doadora-7", "GET", "/api/v1/kits/doadora/8");
        esperar(OK, "t-doadora-7", "GET", "/api/v1/coletas/doadora/7");
        esperar(PROIBIDO, "t-doadora-7", "GET", "/api/v1/coletas/doadora/8");
        esperar(OK, "t-doadora-7", "POST", "/api/v1/coletas/agendamento");
        esperar(OK, "t-doadora-7", "GET", "/api/v1/notificacoes/doadora/7/lidas");
        esperar(OK, "t-doadora-7", "PUT", "/api/v1/notificacoes/doadora/7/lidas");
        esperar(PROIBIDO, "t-doadora-7", "PUT", "/api/v1/notificacoes/doadora/8/lidas");
        esperar(PROIBIDO, "t-medico", "PUT", "/api/v1/notificacoes/doadora/7/lidas");
        esperar(PROIBIDO, "t-equipe", "PUT", "/api/v1/notificacoes/doadora/7/lidas");
    }

    @Test
    void acoesInternasNaoFicamAbertasParaDoadoraOuApoio() throws Exception {
        // Antes: /coletas/{id}/confirmar|realizar e /consultas/{id}/realizar-exame caíam no "return true" final.
        for (String t : new String[]{"t-doadora-7", "t-apoio-5"}) {
            esperar(PROIBIDO, t, "PUT", "/api/v1/coletas/9/confirmar");
            esperar(PROIBIDO, t, "PUT", "/api/v1/coletas/9/realizar");
            esperar(PROIBIDO, t, "PUT", "/api/v1/consultas/3/realizar-exame");
            esperar(PROIBIDO, t, "PUT", "/api/v1/consultas/3/confirmar-exame");
        }
    }

    @Test
    void semTokenOuTokenInvalidoRecebe401() throws Exception {
        esperar(NAO_AUTENTICADO, null, "GET", "/api/v1/triagens");
        esperar(NAO_AUTENTICADO, "token-invalido", "PUT", "/api/v1/kits/4/preparar");
        esperar(NAO_AUTENTICADO, null, "PUT", "/api/v1/coletas/9/confirmar");
        esperar(NAO_AUTENTICADO, null, "PUT", "/api/v1/consultas/3/realizar-exame");
    }

    // ---------------------------------------------------------------- Rede de Apoio: comportamento preservado
    @Test
    void redeDeApoioMantemOsMesmosAcessosDeAntes() throws Exception {
        esperar(OK, null, "POST", "/api/v1/apoios");                  // cadastro público
        esperar(OK, "t-apoio-5", "GET", "/api/v1/apoios/5");
        esperar(PROIBIDO, "t-apoio-5", "GET", "/api/v1/apoios/6");
        esperar(OK, "t-apoio-5", "PUT", "/api/v1/apoios/5");
        esperar(OK, "t-apoio-5", "DELETE", "/api/v1/apoios/5");
        esperar(PROIBIDO, "t-doadora-7", "PUT", "/api/v1/apoios/5");
        // Equipe e Médico: exatamente como antes desta correção (leitura de apoios).
        esperar(OK, "t-equipe", "GET", "/api/v1/apoios");
        esperar(OK, "t-medico", "GET", "/api/v1/apoios");
        esperar(OK, "t-equipe", "GET", "/api/v1/apoios/5");
        esperar(OK, "t-medico", "GET", "/api/v1/apoios/5");
        esperar(PROIBIDO, "t-doadora-7", "GET", "/api/v1/apoios");
    }
}
