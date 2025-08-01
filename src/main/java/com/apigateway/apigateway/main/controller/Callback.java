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

@Component
public class Callback {
    
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String webhookApiUrl;
    private final Payload payload;
    
    public Callback(@Value("${POOLING_CALLBACK_URL}") String webhookApiUrl,
                    @Autowired Payload payload)
    {
        this.webhookApiUrl = webhookApiUrl;
        this.payload = payload;
    }
    
    public void startPolling() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::checkForNewRequests, 0, 5, TimeUnit.SECONDS);
    }
    
    private void checkForNewRequests() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookApiUrl))
                .GET()
                .build();
                
            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
                
            if (response.statusCode() == 200) {
                JSONObject requests = new JSONObject(response.body());
                //JSONArray requests = new JSONArray(response.body());
                processNewRequests(requests);
            }
        } catch (Exception e) {
            System.err.println("Erro no polling: " + e.getMessage());
        }
    }
    
    private void processNewRequests(JSONObject requests) {
        /* for (int i = 0; i < requests.length(); i++) {
            String body = requests.getString(i)
            JSONObject request = requests.getJSONObject(i);
            String body = request.getString("body"); */
            String body = requests.toString();
            
            // Atualiza o payload local
            payload.setPayload(body);
            
            // Extrai idPayer se existir
            /* try {
                JSONObject jsonBody = new JSONObject(body);
                if (jsonBody.has("idPayer")) {
                    payload.setIdPayer(jsonBody.getString("idPayer"));
                }
            } catch (Exception e) {
                // Ignora erros de parsing
            } */
            
            System.out.println("Nova transação recebida via polling!");
            System.out.println("Payload: " + body);
        }
}