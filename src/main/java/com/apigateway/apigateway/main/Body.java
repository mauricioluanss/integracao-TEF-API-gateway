package com.apigateway.apigateway.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A classe Body é responsável por abrigar os metodos que geram os body das
 * requisições utilizadas nas operações de pagamento e cancelamento.
 */
@Component
public class Body {
    /**
     * A variável `callbackUrl` recebe do arquivo `application.properties` a URL de
     * callback.
     */
    @Value("${CALLBACK_URL}")
    private String callbackUrl;

    /**
     * Injeção de dependência da classe Payload para obter o id da transação,
     * e mandar no body de cancelamento.
     */
    @Autowired
    private Payload manipulacaoPayload;

    /**
     * Instancia a classe `CorrelationId` para uso do metodo que irá gerar o id correlatiion da transação.
     * Será encaminhada no body de pagamento e cancelamento.
     */
    CorrelationId correlationId = new CorrelationId();

    /**
     * Metodo responsável por criar o body para a requisição de pagamento. Ele
     * recebe quatro parâmetros relacionados às opções de pagamento, cujos valores
     * são definidos a partir da interação do operador com o menu principal da
     * aplicação.
     * Exemplo de valores: (10.99f, "CARD", "DEBIT", "FULL_PAYMENT").
     */
    public String bodyPagamento(float value, String paymentMethod, String paymentType, String paymentMethodSubType)
            throws JsonProcessingException {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "INPUT");
        body.put("origin", "MIT - Mauricio Integration Tests");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("callbackUrl", callbackUrl);
        data.put("correlationId", correlationId.geraCorrelationId());
        data.put("flow", "SYNC");
        data.put("automationName", "AUTOMACAO_TESTE"); //PODE ALTERAR

        Map<String, Object> receiver = new LinkedHashMap<>();
        receiver.put("companyId", "000001"); //PODE ALTERAR
        receiver.put("storeId", "0025"); //PODE ALTERAR
        receiver.put("terminalId", "03"); //PODE ALTERAR
        data.put("receiver", receiver);

        Map<String, Object> message = new LinkedHashMap<>();
        message.put("command", "PAYMENT");
        message.put("value", value);
        message.put("paymentMethod", paymentMethod);
        message.put("paymentType", paymentType);
        message.put("paymentMethodSubType", paymentMethodSubType);
        data.put("message", message);
        body.put("data", data);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(body);
        /* Modelo de body requisicao de pag usado no Postman.
         * {
         *     "type": "INPUT",
         *     "origin": "MAURICIO POSTMAN",
         *     "data": {
         *         "callbackUrl": "http://localhost:8080/callback",
         *         "correlationId": "4f5b65-asdas6-fefd6-fff78121",
         *         "flow": "SYNC",
         *         "automationName": "Nome da automação",
         *         "receiver": {
         *             "companyId": "CONTA",
         *             "storeId": "LOJA",
         *             "terminalId": "TERMINAL"
         *         },
         *         "message": {
         *             "command": "PAYMENT",
         *             "value": 1,
         *             "paymentMethod": "PIX",
         *             "paymentType": "DEBIT",
         *             "paymentMethodSubType": "FULL_PAYEMENT"
         *         }
         *     }
         * }
         */
    }

    /**
     * Metodo responsável por criar o body para a requisição de cancelamento.
     * Não são necessários parâmetros. A diferença do metodo `bodyPagamento`
     * são os valores do campos `command` e `idPayer`. O valor de `idPayer` é
     * usado para identificar a transação e poder cancela-la.
     */
    public String bodyCancelamento() throws JsonProcessingException {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "INPUT");
        body.put("origin", "MIT - Mauricio Integration Tests");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("callbackUrl", callbackUrl);
        data.put("correlationId", correlationId.geraCorrelationId());
        data.put("flow", "SYNC");
        data.put("automationName", "AUTOMACAO_TESTE");

        Map<String, Object> receiver = new LinkedHashMap<>();
        receiver.put("companyId", "000001");
        receiver.put("storeId", "0025");
        receiver.put("terminalId", "03");
        data.put("receiver", receiver);

        Map<String, Object> message = new LinkedHashMap<>();
        message.put("command", "CANCELLMENT");
        message.put("idPayer", manipulacaoPayload.getIdPayer());
        data.put("message", message);
        body.put("data", data);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(body);
    }
}