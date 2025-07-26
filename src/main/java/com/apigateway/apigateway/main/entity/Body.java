package com.apigateway.apigateway.main.entity;

import com.apigateway.apigateway.main.dto.CancellationMessageDto;
import com.apigateway.apigateway.main.dto.DataDto;
import com.apigateway.apigateway.main.dto.MessagePayload;
import com.apigateway.apigateway.main.dto.PaymentMessageDto;
import com.apigateway.apigateway.main.dto.ReceiverDto;
import com.apigateway.apigateway.main.dto.RequestBodyDto;
import com.apigateway.apigateway.main.enums.parametrosBody.FlowRequest;
import com.apigateway.apigateway.main.enums.parametrosBody.TypeRequest;
import com.apigateway.apigateway.main.enums.parametrosPagamento.Command;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentMethod;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentMethodSubType;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentType;
import com.apigateway.apigateway.main.utils.CorrelationId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Classe responsável por construir o corpo (body) da requisição para integração com o API Gateway.
 * Utiliza propriedades configuradas via application.properties e monta o payload conforme o comando recebido.
 */
@Component
public class Body {
    @Value("${COMPANY_ID}")
    private String companyId;

    @Value("${STORE_ID}")
    private String storeId;

    @Value("${TERMINAL_ID}")
    private String terminalId;

    @Value("${CALLBACK_URL}")
    private String callbackUrl;

    @Value("${AUTOMATION_NAME}")
    private String automationName;

    @Autowired
    private Payload payload;

    CorrelationId correlationId = new CorrelationId();

    /**
     * Monta o corpo da requisição (body) em formato JSON, de acordo com o comando informado.
     *
     * @param command              Comando da transação Ex: (Payment, Cancellment...).
     * @param value                Valor da transação Ex: (4.59, 99.1).
     * @param paymentMethod        Método de pagamento Ex: (CARD, PIX, LINK).
     * @param paymentType          Tipo de pagamento Ex: (DEBIT, CREDIT).
     * @param paymentMethodSubType Subtipo do método de pagamento Ex: (FULL_PAYMENT,FINANCED_NO_FEES).
     * @return                     String JSON representando o corpo da requisição
     * @throws JsonProcessingException Caso ocorra erro na serialização para JSON
     */
    public String bodyRequest(
            Command command,
            float value,
            PaymentMethod paymentMethod,
            PaymentType paymentType,
            PaymentMethodSubType paymentMethodSubType)
            throws JsonProcessingException {

        ReceiverDto receiver = new ReceiverDto();
        receiver.setCompanyId(companyId);
        receiver.setStoreId(storeId);
        receiver.setTerminalId(terminalId);

        MessagePayload messagePayload;
        if (command == Command.CANCELLMENT) {
            CancellationMessageDto cancellation = new CancellationMessageDto();
            cancellation.setCommand(command);
            cancellation.setIdPayer(payload.getIdPayer());
            messagePayload = cancellation;
        } else if (command == Command.PAYMENT) {
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

        DataDto data = new DataDto();
        data.setCallbackUrl(callbackUrl);
        data.setCorrelationId(correlationId.getCorrelationId());
        data.setFlow(FlowRequest.SYNC);
        data.setAutomationName(automationName);
        data.setReceiver(receiver);
        data.setMessage(messagePayload);

        RequestBodyDto body = new RequestBodyDto();
        body.setType(TypeRequest.INPUT);
        body.setOrigin("MIT - Mauricio Integration Tests");
        body.setData(data);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(body);
    }
}