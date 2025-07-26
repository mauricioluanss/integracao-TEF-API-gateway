package com.apigateway.apigateway.main.entity;

import org.springframework.stereotype.Component;

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
