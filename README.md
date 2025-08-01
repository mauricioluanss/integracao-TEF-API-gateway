# Integração com Checkout de Pagamentos via API Gateway

Antes de mais nada, preciso deixar claro que o objetivo de desenvolver este projeto foi entender de verdade como é o funcionamento da integração entre automações comerciais e nossa aplicação, via api gateway. Eu costumava escutar todos os dias o pessoal do trabalho comentando coisas sobre requisição, autenticação, callback, webhook. E eu queria entender disso para não ficar para trás. A forma que eu encontrei de aprender foi tentando desenvolver uma solução que fizesse isso. Não é perfeito, mas funciona. E para o que eu me propus, está muito bom!

Sendo assim, este projeto implementa a lógica essencial para integrar um sistema de automação comercial ao Checkout de pagamentos da empresa que trabalho, via API Gateway. Além disso, tem um menu interativo para testar chamdas e consultar as transações.

**🔄 Atualização Recente:** O projeto foi atualizado para usar o [webhook.site](https://webhook.site) ao invés do ngrok para receber callbacks das transações, simplificando a configuração e melhorando a experiência de desenvolvimento.

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 21** - [Download do JDK 21](https://adoptium.net/temurin/releases/?version=21)
- **Maven 3.6+** - [Download do Maven](https://maven.apache.org/download.cgi)
- **Webhook.site** - [Acesse aqui](https://webhook.site) (para receber callbacks das transações)


## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.3
- Maven
- Webhook.site (para receber callbacks das transações)

## Configuração do Ambiente

### 1. Configuração das Variáveis de Ambiente

1. **Copie o arquivo de exemplo:**
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

2. **Edite o arquivo `application.properties`** com suas credenciais:

   ```properties
   spring.application.name=apigateway

   # Credenciais de login
   CLIENT_ID=seu_client_id_aqui
   PAYER_USERNAME=seu_username_aqui
   PASSWORD=sua_senha_aqui

   # URL de callback (webhook.site)
   CALLBACK_URL=https://webhook.site/abc123-def456

   # Endpoints da API de pagamento
   TOKEN_ENDPOINT_URL=https://api.exemplo.com/auth/token
   TRANSACTION_ENDPOINT_URL=https://api.exemplo.com/payments

   # Identificação da Empresa, Loja e terminal (checkout)
   COMPANY_ID=seu_company_id
   STORE_ID=seu_store_id
   TERMINAL_ID=seu_terminal_id

   AUTOMATION_NAME=nome_da_sua_automacao

   # Configurações de log (opcional)
   logging.level.org.springframework=warn
   logging.level.org.apache.catalina=warn
   logging.level.org.apache.coyote=warn
   logging.level.org.apache.tomcat=warn
   ```

### 2. Configuração do Webhook.site

**📖 Para instruções detalhadas, consulte o arquivo [WEBHOOK_SETUP.md](WEBHOOK_SETUP.md)**

1. **Acesse o Webhook.site:**
   - Vá para [https://webhook.site](https://webhook.site)
   - Uma URL única será gerada automaticamente (exemplo: `https://webhook.site/abc123-def456`)

2. **Copie a URL gerada** e atualize o `CALLBACK_URL` no `application.properties`

3. **Mantenha a página do webhook.site aberta** para visualizar os callbacks recebidos



## Como Usar

Após iniciar a aplicação, você verá um menu interativo no terminal:

```
1) Pagar  2) Cancelar transação
Digite a opção:
```

### Opções Disponíveis:

1. **Pagar** - Inicia uma nova transação de pagamento
   - Debito
   - Credito (à vista ou parcelado)
   - Pix

2. **Cancelar transação** - Cancela a ultima transação existente

3. **Consultar transação** - Visualiza o ultimo payload de retorno da transação

**Visualizando os Callbacks no Webhook.site:**
- Mantenha a página do webhook.site aberta no navegador
- Cada transação realizada aparecerá como uma nova requisição na página
- Clique em qualquer requisição para ver os detalhes completos do payload
- Os dados incluem headers, body e timestamp da transação

## Fluxo de Funcionamento padrão

1. **Disparo da Venda:**  
   O sistema de automação realiza uma requisição HTTP `POST` para um endpoint da API do Checkout.  
   O corpo (`payload`) dessa requisição contém:
   - Informações da venda
   - Dados de identificação do terminal de pagamento (checkout)
   - URL de callback para o retorno da transação
   - Um id único que identifica cada transação

2. **Ativação do Checkout:**  
   Ao receber a requisição, o Checkout é ativado e a sobe na tela chamando a transação.

3. **Processamento via Gateway de TEF:**  
   Após o cliente finalizar o pagamento, o Checkout envia uma requisição ao Gateway de TEF, que processa a transação e retorna um `payload` com os dados da resposta.

4. **Retorno à Automação (Callback):**  
   A API do Checkout, ao receber o `payload` de resposta, envia os dados para a URL de callback previamente informada pela automação. No nosso caso, essa URL é do webhook.site, que recebe e exibe os dados da transação para visualização.


## Estrutura do Projeto

```
integracao-api-gateway/
├── src/main/java/com/apigateway/apigateway/main/
│   ├── controller/
│   │   └── CallbackController.java          # Endpoint para callbacks (desabilitado - usando webhook.site)
│   ├── dto/                                 # Data Transfer Objects
│   ├── entity/                              # Entidades do sistema
│   ├── enums/                               # Enumerações
│   │   ├── parametrosBody/                  # Enums para parâmetros do body
│   │   └── parametrosPagamento/             # Enums para métodos de pagamento
│   ├── service/
│   │   └── Services.java                    # Serviços de integração com API
│   ├── utils/
│   │   └── CorrelationId.java               # Utilitários
│   ├── Main.java                            # Classe principal
│   └── Menu.java                            # Menu interativo
├── src/main/resources/
│   └── application.properties.example       # Configurações de exemplo
├── WEBHOOK_SETUP.md                         # Instruções para webhook.site
├── pom.xml                                  # Dependências Maven
└── README.md
```

## Configurações

### Logs

Para reduzir os logs no console e melhorar a visualização, as seguintes configurações estão disponíveis no `application.properties`:

```properties
# Reduz o log do Spring Boot para WARN
logging.level.org.springframework=warn
# Reduz o log do Tomcat para WARN
logging.level.org.apache.catalina=warn
logging.level.org.apache.coyote=warn
logging.level.org.apache.tomcat=warn
```

## Contribuição

Este projeto foi desenvolvido para fins de estudo. Sinta-se à vontade para fazer melhorias e contribuições.

---

