package com.apigateway.apigateway.main.controller;

import com.apigateway.apigateway.main.entity.Payload;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para receber callbacks de transações.
 * 
 * ATENÇÃO: Este controller foi desabilitado pois agora utilizamos o webhook.site
 * para receber os callbacks das transações. Os dados das transações podem ser
 * visualizados diretamente na página do webhook.site.
 * 
 * Para reativar este controller (caso queira processar os callbacks localmente):
 * 1. Remova os comentários abaixo
 * 2. Configure o CALLBACK_URL para apontar para este endpoint local
 * 3. Use ngrok ou similar para expor o localhost
 */

/*
@RestController
@RequestMapping("/webhook")
public class CallbackController {

    @Autowired
    private Payload manipulacaoPayload;

    @PostMapping
    public void callbackReturn(@RequestBody String payload) {
        JSONObject jsonObject = new JSONObject(payload);

        if (jsonObject.has("idPayer")) {
            String idPayer = jsonObject.getString("idPayer");
            manipulacaoPayload.setIdPayer(idPayer);
        } else {
            // Trate o caso de ausência do campo
            System.out.println("Campo idPayer não encontrado no payload!");
        }
        manipulacaoPayload.setPayload(payload);
    }
}
*/