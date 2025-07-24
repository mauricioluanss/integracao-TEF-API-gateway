package com.apigateway.apigateway.main.service;

import com.apigateway.apigateway.main.entity.Body;
import com.apigateway.apigateway.main.entity.CredenciaisAuth;
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
    @Value("${EP_TOKEN}") // Endpoint para obter token de autenticação.
    private String endpointToken;

    @Value("${EP_PAGAMENTOS}") // Endpoint para chamar pagamentos.
    private String endpointPagamentos;

    @Autowired
    private Body body; // Acesso aos metodos da classe Body.

    @Autowired
    private CredenciaisAuth credenciaisAuth; // Acesso ao metodo que retorna as credenciais de login.

    HttpClient client = HttpClient.newHttpClient();

    //Autentica o usuário e retorna o token
    private String pegaToken() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointToken))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(credenciaisAuth.retornaCredenciais()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return capturaIdToken(response);
    }

    // Extrai o campo "IdToken" do corpo da resposta JSON de autenticação.
    public String capturaIdToken (HttpResponse<String> response) {
        JSONObject jsonObject = new JSONObject(response.body());
        JSONObject authenticationResult = jsonObject.getJSONObject("AuthenticationResult");
        return authenticationResult.getString("IdToken");
    }

    /** Metodo para realizar a requisição de pagamento. Ele leva como parâmetros as OCPOES DE PAGAMENTO, que serão
     * capturadas na interação com o usuário em `MenuDeOpcoes`.
     */
    public void chamaPagamento (float v, String pm, String pt, String pmst) throws IOException, InterruptedException {
        System.out.println("Chamando Payer...\n");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointPagamentos))
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + this.pegaToken())
                .POST(HttpRequest.BodyPublishers.ofString(body.bodyPagamento("payment", v, pm, pt, pmst)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        /*System.out.println("Resposta da Payer para o callback após receber a requisição enviada\n" +
                response.body() + "\n"); //debug*/
    }

    /**
     * Metodo para realizar a requisição de cancelamento de pagamento. Ela não leva parâmetros.
     * O valor usado para identificar o pagamento que será cancelado é o id da transação. O metodo
     * que captura esse valor é chamado em `CallbackController`, e atribuído diretamente no body
     * da requisição, em `bodyCancelamento()`.
     */
    public void chamaCancelamento() throws IOException, InterruptedException {
        System.out.println("Requisição que está sendo da enviada da automação para a Payer\n" +
                body.bodyCancelamento() + "\n"); //debug

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointPagamentos))
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + this.pegaToken())
                .POST(HttpRequest.BodyPublishers.ofString(body.bodyCancelamento()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        /*System.out.println("Resposta da Payer para o callback após receber a requisição enviada\n" +
                response.body() + "\n"); //debug*/
    }
}