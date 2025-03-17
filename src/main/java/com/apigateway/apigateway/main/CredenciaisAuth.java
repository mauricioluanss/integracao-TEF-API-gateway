package com.apigateway.apigateway.main;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public String getClientId() {
        return clientId;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
