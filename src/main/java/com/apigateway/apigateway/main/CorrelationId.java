package com.apigateway.apigateway.main;

import java.util.UUID;

/**  Classe e metodo para gerar um correlationId aleatório.
 *  O correlationId retornado é único para cada transação.
 *
 * Fonte: https://www.uuidgenerator.net/dev-corner/java
 */
public class CorrelationId {
    public String geraCorrelationId() {
        UUID correlationId = UUID.randomUUID();
        return correlationId.toString();
    }
}