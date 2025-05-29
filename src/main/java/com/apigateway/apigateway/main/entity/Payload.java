package com.apigateway.apigateway.main.entity;

import org.springframework.stereotype.Component;

/**
 * A classe Payload fornece acesso ao payload da transação e
 * ao ID de transação extraído dele para disponibilizar em outras
 * partes do código. Isso é possível por meio dos getters e setters
 * criados.
 */
@Component
public class Payload {
    private String payload;
    private String idPayer;

    public void setPayload(String payload) {
        this.payload = payload;
    }
    public String getPayload() {
        return payload;
    }

    public void setIdPayer(String idPayer) {
        this.idPayer = idPayer;
    }
    public String getIdPayer() {
        return idPayer;
    }
}
