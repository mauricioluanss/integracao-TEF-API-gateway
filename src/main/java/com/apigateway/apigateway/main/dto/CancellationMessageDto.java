package com.apigateway.apigateway.main.dto;

public class CancellationMessageDto extends MessagePayload {
    private String idPayer;

    public CancellationMessageDto() {
    }

    public String getIdPayer() {
        return idPayer;
    }

    public void setIdPayer(String idPayer) {
        this.idPayer = idPayer;
    }

}
