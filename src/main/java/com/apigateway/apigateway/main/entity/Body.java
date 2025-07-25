package com.apigateway.apigateway.main.entity;

import com.apigateway.apigateway.main.dto.CancellationMessageDto;
import com.apigateway.apigateway.main.dto.DataDto;
import com.apigateway.apigateway.main.dto.MessagePayload;
import com.apigateway.apigateway.main.dto.PaymentMessageDto;
import com.apigateway.apigateway.main.dto.ReceiverDto;
import com.apigateway.apigateway.main.dto.RequestBodyDto;
import com.apigateway.apigateway.main.utils.CorrelationId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class Body {

    @Value("${CALLBACK_URL}")
    private String callbackUrl;

    @Autowired
    private Payload manipulacaoPayload;

    CorrelationId correlationId = new CorrelationId();

    public String bodyRequest(
        String command,
        float value,
        String paymentMethod,
        String paymentType,
        String paymentMethodSubType) throws JsonProcessingException {

        // Monta o objeto Receiver
        ReceiverDto receiver = new ReceiverDto();
        receiver.setCompanyId("000001");
        receiver.setStoreId("0025");
        receiver.setTerminalId("02");

        MessagePayload messagePayload;
        if ("CANCELLMENT".equalsIgnoreCase(command)) {
            CancellationMessageDto cancellation = new CancellationMessageDto();
            cancellation.setCommand(command);
            cancellation.setIdPayer(manipulacaoPayload.getIdPayer());
            messagePayload = cancellation;
        } else if ("PAYMENT".equalsIgnoreCase(command)) {
            PaymentMessageDto payment = new PaymentMessageDto();
            payment.setCommand(command);
            payment.setValue(value);
            payment.setPaymentMethod(paymentMethod);
            payment.setPaymentType(paymentType);
            payment.setPaymentMethodSubType(paymentMethodSubType);
            messagePayload = payment;
        } else {
            throw new IllegalArgumentException("Comando inválido ou não suportado: " + command);
        }

        // Monta o objeto Data principal
        DataDto data = new DataDto();
        data.setCallbackUrl(callbackUrl);
        data.setCorrelationId(correlationId.geraCorrelationId());
        data.setFlow("SYNC");
        data.setAutomationName("AUTOMACAO_TESTE");
        data.setReceiver(receiver);
        data.setMessage(messagePayload); // Adiciona o payload correto

        // Monta o corpo da requisição final
        RequestBodyDto body = new RequestBodyDto();
        body.setType("INPUT");
        body.setOrigin("MIT - Mauricio Integration Tests");
        body.setData(data);

        // Serializa o objeto completo para JSON
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(body);
    }
}