package com.apigateway.apigateway.main.dto;

public class CancellationMessageDto extends MessagePayload {
    private String idPayer;
    private String command;

    public CancellationMessageDto() {
    }

    public String getIdPayer() {
        return idPayer;
    }

    public void setIdPayer(String idPayer) {
        this.idPayer = idPayer;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
