package com.apigateway.apigateway.main.dto;

import com.apigateway.apigateway.main.enums.parametrosPagamento.Command;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentMethod;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentMethodSubType;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentType;

public class PaymentMessageDto extends MessagePayload {
    private Command command;
    private float value;
    private PaymentMethod paymentMethod;
    private PaymentType paymentType;
    private PaymentMethodSubType paymentMethodSubType;

    public PaymentMessageDto() {
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentMethodSubType getPaymentMethodSubType() {
        return paymentMethodSubType;
    }

    public void setPaymentMethodSubType(PaymentMethodSubType paymentMethodSubType) {
        this.paymentMethodSubType = paymentMethodSubType;
    }

    
    
}