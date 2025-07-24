package com.apigateway.apigateway.main.dto;

public class DataDto {
    private String callbackUrl;
    private String correlationId;
    private String flow;
    private String automationName;
    private ReceiverDto receiver;
    private MessagePayload message;
    
    public DataDto(){}
    
    public String getCallbackUrl() {
        return callbackUrl;
    }
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    public String getCorrelationId() {
        return correlationId;
    }
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
    public String getFlow() {
        return flow;
    }
    public void setFlow(String flow) {
        this.flow = flow;
    }
    public String getAutomationName() {
        return automationName;
    }
    public void setAutomationName(String automationName) {
        this.automationName = automationName;
    }
    public ReceiverDto getReceiver() {
        return receiver;
    }
    public void setReceiver(ReceiverDto receiver) {
        this.receiver = receiver;
    }
    public MessagePayload getMessage() {
        return message;
    }
    public void setMessage(MessagePayload message) {
        this.message = message;
    }

    
}
