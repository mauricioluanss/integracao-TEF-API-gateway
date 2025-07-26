package com.apigateway.apigateway.main.service;

import com.apigateway.apigateway.main.entity.Body;
import com.apigateway.apigateway.main.entity.CredenciaisAuth;
import com.apigateway.apigateway.main.enums.parametrosPagamento.Command;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentMethod;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentMethodSubType;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentType;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Classe responsável por realizar operações de autenticação e transações
 * utilizando a entidade e enums.
 */
@Service
public class Services {
    @Value("${TOKEN_ENDPOINT_URL}")
    private String tokenEndpointUrl;

    @Value("${TRANSACTION_ENDPOINT_URL}")
    private String transactionEndpointUrl;

    @Autowired
    private Body body;

    @Autowired
    private CredenciaisAuth credenciaisAuth;

    // Cliente HTTP
    HttpClient client = HttpClient.newHttpClient();

    /**
     * Realiza a requisição para obter o token de autenticação.
     *
     * @return String contendo o IdToken.
     * @throws IOException
     * @throws InterruptedException
     */
    private String getToken() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenEndpointUrl))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(credenciaisAuth.retornaCredenciais()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return getIdToken(response);
    }

    /**
     * Extrai o IdToken da resposta de autenticação.
     *
     * @param response Resposta HTTP recebida do endpoint de autenticação.
     * @return String contendo o IdToken.
     */
    public String getIdToken(HttpResponse<String> response) {
        JSONObject jsonObject = new JSONObject(response.body());
        JSONObject authenticationResult = jsonObject.getJSONObject("AuthenticationResult");
        return authenticationResult.getString("IdToken");
    }

    /**
     * Realiza a requisição de transação.
     *
     * @param command              Comando da transação Ex: (Payment, Cancellment
     *                             ...).
     * @param value                Valor da transação Ex: (4.59, 99.1).
     * @param paymentMethod        Método de pagamento Ex: (CARD, PIX, LINK).
     * @param paymentType          Tipo de pagamento Ex: (DEBIT, CREDIT).
     * @param paymentMethodSubType Subtipo do método de pagamento Ex: (FULL_PAYMENT,
     *                             FINANCED_NO_FEES).
     * @throws IOException
     * @throws InterruptedException
     */
    public void transactionRequest(
            Command command,
            float value,
            PaymentMethod paymentMethod,
            PaymentType paymentType,
            PaymentMethodSubType paymentMethodSubType)
            throws IOException, InterruptedException {
        System.out.println("Chamando Payer...\n");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(transactionEndpointUrl))
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + this.getToken())
                .POST(HttpRequest.BodyPublishers
                        .ofString(body.bodyRequest(command, value, paymentMethod, paymentType, paymentMethodSubType)))
                .build();

        // Imprime no console a requisição enviada pra debug
        System.out.println(body.bodyRequest(command, value, paymentMethod, paymentType, paymentMethodSubType) + "\n");
        /* HttpResponse<String> response = */client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}