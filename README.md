# Integração com Checkout de Pagamentos via API Gateway

Antes de mais nada, preciso deixar claro que o objetivo de desenvolver este projeto foi entender de verdade como é o funcionamento da integração entre automações comerciais e nossa aplicação, via api gateway. Eu costumava escutar todos os dias o pessoal do trabalho comentando coisas sobre requisição, autenticação, callback, webhook. E eu queria entender disso para não ficar para trás. A forma que eu encontrei de aprender foi tentando desenvolver uma solução que fizesse isso. Não é perfeito, mas funciona. E para o que eu me propus, está muito bom!

Sendo assim, este projeto implementa a lógica essencial para integrar um sistema de automação comercial ao Checkout de pagamentos da empresa que trabalho, via API Gateway. Além disso, tem um menu interativo para testar chamdas e consultar as transações.

## Tecnologias
- Java 21
- Spring Boot
- Maven
- Webhook.site (para receber callbacks)

## Como funciona
1. **Menu Interativo:**
   - Ao iniciar a aplicação, um menu é exibido no terminal para:
     - Realizar pagamentos (débito, crédito, pix)
     - Cancelar a última transação
     - Consultar o resultado da última transação
2. **Processo de Pagamento:**
   - O usuário escolhe o tipo de pagamento e informa o valor.
   - A aplicação envia a requisição para o endpoint de pagamento.
   - O resultado é enviado via callback para a URL configurada (webhook.site).
3. **Callback:**
   - O sistema faz polling na URL do webhook para buscar o resultado da transação e exibe no menu.

## Configuração Rápida
1. Copie o arquivo de propriedades:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   # Edite com suas credenciais e a URL do webhook.site
   ```

2. **Edite o arquivo `application.properties`** com suas credenciais:

   ```properties
   spring.application.name=apigateway

   # Credenciais de login
   CLIENT_ID=
   PAYER_USERNAME=
   PASSWORD=
   
   # URL de callback (webhook.site)
   # Acesse https://webhook.site e copie a URL única gerada
   #ex: https://webhook.site/dc80c03e-6e14-49a0-aa3b-14915cafb999
   CALLBACK_URL=
   
   #URL DE CALLBACK PARA FAZER O POLLING
   #exemlo: https://webhook.site/token/dc80c03e-6e14-49a0-aa3b-14915cafb999/requests
   POOLING_CALLBACK_URL=
   
   # Endpoints da API de pagamento
   TOKEN_ENDPOINT_URL=
   TRANSACTION_ENDPOINT_URL=
   
   # Identificação da Empresa, Loja e terminal (checkout)
   COMPANY_ID=
   STORE_ID=
   TERMINAL_ID=
   
   AUTOMATION_NAME=

   ### PARA REDUZIR OS LOGS NO CONSOLE E MELHORAR A VISUALIZAÇÃO ###
   # Reduz o log do Spring Boot para WARN
   logging.level.org.springframework=warn
   # Reduz o log do Tomcat para WARN
   logging.level.org.apache.catalina=warn
   logging.level.org.apache.coyote=warn
   logging.level.org.apache.tomcat=warn
   ```

3. Execute a aplicação.


## Estrutura Principal
```
├── src/main/java/com/apigateway/apigateway/main/
│   ├── Main.java         # Inicialização da aplicação
│   ├── Menu.java         # Menu interativo e fluxo principal
│   ├── controller/Callback.java # Polling do webhook
│   ├── service/Services.java    # Integração com API de pagamentos
│   └── entity/Payload.java      # Armazena dados das transações
```


---
Projeto para fins de estudo e demonstração de integração com API Gateway de pagamentos.

