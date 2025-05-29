package com.apigateway.apigateway.main.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Classe para acesso às credenciais de login na API.
 * O login é necessário para obter o token que será enviado
 * nas requisições de pagamento / cancelamento.
 */
@Component
public class CredenciaisAuth {
    @Value("${CLIENT_ID}")
    private String clientId;
    @Value("${PAYER_USERNAME}")
    private String username;
    @Value("${PASSWORD}")
    private String password;

    public String retornaCredenciais() throws JsonProcessingException {
        Map<String, String> credenciais = new LinkedHashMap<>();
        credenciais.put("clientId", clientId);
        credenciais.put("username", username);
        credenciais.put("password", password);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(credenciais);
    }
}
