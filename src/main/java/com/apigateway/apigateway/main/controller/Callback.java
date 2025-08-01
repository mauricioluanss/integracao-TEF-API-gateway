package com.apigateway.apigateway.main.controller;

import com.apigateway.apigateway.main.entity.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Componente responsável por realizar o polling no endpoint de callback,
 * processar as respostas recebidas e atualizar o payload da aplicação.
 */
@Component
public class Callback {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String webhookApiUrl;
    private final Payload payload;

    /**
     * Construtor da classe Callback.
     *
     * @param webhookApiUrl URL do endpoint de callback (POOLING_CALLBACK_URL)
     * @param payload       Instância de Payload injetada pelo Spring
     */
    public Callback(@Value("${POOLING_CALLBACK_URL}") String webhookApiUrl, @Autowired Payload payload) {
        this.webhookApiUrl = webhookApiUrl;
        this.payload = payload;
    }

    /**
     * Inicia o processo de polling, agendando a verificação de callbacks a cada 5 segundos.
     */
    public void startPolling() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::checkCallbacks, 0, 5, TimeUnit.SECONDS);
    }

    private void checkCallbacks() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookApiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject requestJson = new JSONObject(response.body());
                JSONArray requests = requestJson.getJSONArray("data");
                processNewRequests(requests);
            }
        } catch (Exception e) {}
    }

    /**
     * Processa o array de requisições recebidas do callback, extraindo o conteúdo do último item.
     * Atualiza o payload e armazena o idPayer.
     *
     * @param requests JSONArray contendo as requisições recebidas
     */
    private void processNewRequests(JSONArray requests) {
        // obtém o último objeto do array de requisições
        JSONObject request = requests.getJSONObject(requests.length() -1);
        String content = request.getString("content");

        JSONObject contentJson = new JSONObject(content);

        // salva o idPayer para uso em cancelamento de transação, se existir
        if (contentJson.has("idPayer")) {
            String idPayer = contentJson.getString("idPayer");
            payload.setIdPayer(idPayer);
        } else {
            System.out.println("id payer não existe no payload");
        }

        payload.setPayload(content);
    }
}