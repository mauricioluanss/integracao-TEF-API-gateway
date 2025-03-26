package com.apigateway.apigateway.main;

import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A classe `Services` contem os métodos das requsições.
 */
@Service
public class Services {
    /**
     * A variável endpointToken recebe de `application.properties` o endpoint da API
     * externa para realizar a autenticação do usuário e receber o idtoken.
     */
    @Value("${EP_TOKEN}")
    private String endpointToken;

    /**
     * A variável endpointPagamentos recebe de `application.properties` o endpoint
     * da API externa para chamada de pagamentos / cancelamentos.
     */
    @Value("${EP_PAGAMENTOS}")
    private String endpointPagamentos;

    /**
     * Injeção de dependência da classe `Body` para acesso aos metodos de construção dos
     * JSON para as requisições.
     */
    @Autowired
    private Body body;

    /**
     * Injeção de dependência da classe `CredenciaisAuth` para acesso aos metodos getters,
     * para obter as credenciais de login.
     */
    @Autowired
    private CredenciaisAuth credenciaisAuth;

    HttpClient client = HttpClient.newHttpClient();


    /**
     * Metodo para realizar autenticação do usuário. A autenticação é necessária para que
     * se obtenha o idtoken. O idtoken extraído irá como header nas requisições de pagamento
     * / cancelamento.
     */
    private String pegaToken() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointToken))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(credenciaisAuth.retornaCredenciais()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return capturaIdToken(response);
    }

    public String capturaIdToken (HttpResponse<String> response) {
        JSONObject jsonObject = new JSONObject(response.body());
        JSONObject authenticationResult = jsonObject.getJSONObject("AuthenticationResult");
        return authenticationResult.getString("IdToken");
    }

    /**
     * Metodo para realizar a requisição de pagamento. Ele leva como parâmetros as
     * OCPOES DE PAGAMENTO, que serão capturadas na interação com o usuário em `MenuDeOpcoes`.
     */
    public void chamaPagamento(float v, String pm, String pt, String pmst)
            throws IOException, InterruptedException {
        System.out.println("Chamando Payer...\n");

        System.out.println("<<<--- Requisição que está sendo da enviada da automação para a Payer --->>>\n" + body.bodyPagamento(v, pm, pt, pmst) + "\n"); //debug

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointPagamentos))
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + this.pegaToken())
                .POST(HttpRequest.BodyPublishers.ofString(body.bodyPagamento(v, pm, pt, pmst)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("<<<--- Resposta da Payer para o callback após receber a requisição enviada --->>>\n" + response.body() + "\n"); //debug
    }

    /**
     * Metodo para realizar a requisição de cancelamento de pagamento. Ela não leva parâmetros.
     * O valor usado para identificar o pagamento que será cancelado é o id da transação. O metodo
     * que captura esse valor é chamado em `CallbackController`, e atribuído diretamente no body
     * da requisição, em `bodyCancelamento()`.
     */
    public void chamaCancelamento() throws IOException, InterruptedException {
        System.out.println("Chamando cancelamento...");

        System.out.println("<<<--- Requisição que está sendo da enviada da automação para a Payer --->>>\n" + body.bodyCancelamento() + "\n"); //debug

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointPagamentos))
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + this.pegaToken())
                .POST(HttpRequest.BodyPublishers.ofString(body.bodyCancelamento()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("<<<--- Resposta da Payer para o callback após receber a requisição enviada --->>>\n" + response.body() + "\n"); //debug
    }
}