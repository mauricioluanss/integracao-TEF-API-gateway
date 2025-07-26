package com.apigateway.apigateway.main.dto;

import com.apigateway.apigateway.main.enums.parametrosPagamento.Command;

public class CancellationMessageDto extends MessagePayload {
    private String idPayer;
    private Command command;

    public CancellationMessageDto() {
    }

    public String getIdPayer() {
        return idPayer;
    }

    public void setIdPayer(String idPayer) {
        this.idPayer = idPayer;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

}
