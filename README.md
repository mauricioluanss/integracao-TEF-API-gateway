# 📌 Integração com Checkout de Pagamentos via API Gateway

Antes de mais nada, preciso deixar claro que o objetivo de desenvolver este projeto foi entender de verdade como é o funcionamento da integração via api. Eu costumava escutar todos os dias o pessoal do trabalho comentando coisas sobre requisição, autenticação, callback, webhook. E eu queria entender disso para não ficar para trás. A forma que eu encontrei de aprender foi tentando desenvolver uma solução que fizesse isso. 

Sendo assim, este projeto implementa a lógica essencial para integrar um sistema de automação comercial ao Checkout de pagamentos da empresa que trabalho, via API Gateway.


## 🔄 Fluxo de Funcionamento padrão

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
   A API do Checkout, ao receber o `payload` de resposta, envia os dados para a URL de callback previamente informada pela automação. Essa URL trata o retorno da transação no sistema de origem.

---

## 🧪 Simulação de Ambiente de Automação

Para fins de teste e simulação, eu desenvolvi um menu interativo em terminal que emula o comportamento de uma automação comercial. Por meio deste menu, é possível informar o valor da venda e selecionar as opções de pagamento disponíveis.

Para integração com um ambiente de automação real, basta remover os métodos relacionados ao menu interativo, uma vez que sua função é apenas ilustrativa.

Também foi implementado um endpoint responsável por receber o payload de retorno da transação (callback).

Eu utilizei o [Ngrok](https://ngrok.com/docs/getting-started/?os=windows) para expor a aplicação local (porta `8080`) para uma URL pública acessível externamente.

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Maven
- *Ngrok

---

Contino o resto depois...
