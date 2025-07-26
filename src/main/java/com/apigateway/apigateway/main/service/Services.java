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

@Service
public class Services {
    @Value("${EP_TOKEN}")
    private String endpointToken;

    @Value("${EP_PAGAMENTOS}")
    private String endpointPagamentos;

    @Autowired
    private Body body;

    @Autowired
    private CredenciaisAuth credenciaisAuth;

    HttpClient client = HttpClient.newHttpClient();

    private String pegaToken() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointToken))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(credenciaisAuth.retornaCredenciais()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return capturaIdToken(response);
    }

    public String capturaIdToken(HttpResponse<String> response) {
        JSONObject jsonObject = new JSONObject(response.body());
        JSONObject authenticationResult = jsonObject.getJSONObject("AuthenticationResult");
        return authenticationResult.getString("IdToken");
    }

    public void transactionRequest(
            Command command,
            float value,
            PaymentMethod paymentMethod,
            PaymentType paymentType,
            PaymentMethodSubType paymentMethodSubType)
            throws IOException, InterruptedException {
        System.out.println("Chamando Payer...\n");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointPagamentos))
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + this.pegaToken())
                .POST(HttpRequest.BodyPublishers
                        .ofString(body.bodyRequest(command, value, paymentMethod, paymentType, paymentMethodSubType)))
                .build();

        // pra imprimir no console a requisição enviada
        System.out.println(body.bodyRequest(command, value, paymentMethod, paymentType, paymentMethodSubType) + "\n");
        /* HttpResponse<String> response = */client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}