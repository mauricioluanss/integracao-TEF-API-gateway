# Configuração do Webhook.site

## O que é o Webhook.site?

O [Webhook.site](https://webhook.site) é uma ferramenta gratuita que permite receber e visualizar webhooks/callbacks de APIs externas. É perfeita para testes e desenvolvimento, pois não requer instalação ou configuração complexa.

## Como Configurar

### 1. Acesse o Webhook.site
- Vá para [https://webhook.site](https://webhook.site)
- Uma URL única será gerada automaticamente
- Exemplo: `https://webhook.site/abc123-def456-ghi789`

### 2. Configure no Projeto
- Copie a URL gerada
- Abra o arquivo `src/main/resources/application.properties`
- Atualize a propriedade `CALLBACK_URL` com a URL do webhook.site

```properties
CALLBACK_URL=https://webhook.site/abc123-def456-ghi789
```

### 3. Teste a Configuração
- Mantenha a página do webhook.site aberta no navegador
- Execute uma transação no seu projeto
- Você verá a requisição aparecer na página do webhook.site

## Vantagens do Webhook.site

✅ **Simples**: Não requer instalação ou configuração  
✅ **Gratuito**: Sem custos para uso básico  
✅ **Visual**: Interface clara para visualizar os dados  
✅ **Histórico**: Mantém histórico das requisições  
✅ **Detalhado**: Mostra headers, body, timestamp  
✅ **Tempo Real**: Atualização automática  

## Visualizando os Dados

Quando uma transação é processada, você verá na página do webhook.site:

- **Timestamp**: Quando a requisição foi recebida
- **Headers**: Informações da requisição HTTP
- **Body**: Payload completo da transação
- **Método**: POST, GET, etc.
- **IP de Origem**: De onde veio a requisição

## Dicas de Uso

1. **Mantenha a página aberta** durante os testes
2. **Clique nas requisições** para ver detalhes completos
3. **Use a URL única** para cada sessão de teste
4. **Copie os dados** se precisar analisar offline

## Alternativas

Se precisar de funcionalidades mais avançadas, considere:
- **ngrok** (mais complexo, mas mais flexível)
- **Postman Webhooks** (para testes mais estruturados)
- **RequestBin** (similar ao webhook.site) 