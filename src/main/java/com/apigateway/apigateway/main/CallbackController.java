package com.apigateway.apigateway.main;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe e metodo em Spring Boot que cria um endpoint
 * para receber a resposta da transação do backend da Payer.
 */
@RestController
@RequestMapping("/webhook")
public class CallbackController {
    @Autowired
    private Payload manipulacaoPayload;

    /**
     * O metodo cria o endpoint(callback) para receber requisicoes HTTP POST.
     * Ele converte o paylaod recebido para um objeto json e extrai o id da
     * transação. Além disso, exporta para classe `Payload` o payload e o id
     * da transação.
     */
    @PostMapping
    public void callbackReturn(@RequestBody String payload) {
        JSONObject jsonObject = new JSONObject(payload);
        String idPayer = jsonObject.getString("idPayer");
        manipulacaoPayload.setIdPayer(idPayer);

        manipulacaoPayload.setPayload(payload);
    }
}