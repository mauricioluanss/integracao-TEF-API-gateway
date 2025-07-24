package com.apigateway.apigateway.main.dto;

public class PaymentMessageDto extends MessagePayload {
    private float value;
    private String paymentMethod;
    private String paymentType;
    private String paymentMethodSubType;
    
    public PaymentMessageDto() {
    }

    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    public String getPaymentMethodSubType() {
        return paymentMethodSubType;
    }
    public void setPaymentMethodSubType(String paymentMethodSubType) {
        this.paymentMethodSubType = paymentMethodSubType;
    }

    
}
