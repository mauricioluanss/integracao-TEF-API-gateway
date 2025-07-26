package com.apigateway.apigateway.main.utils;

import java.util.UUID;

public class CorrelationId {
    public String geraCorrelationId() {
        UUID correlationId = UUID.randomUUID();
        return correlationId.toString();
    }
}