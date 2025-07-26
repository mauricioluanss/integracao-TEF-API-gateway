package com.apigateway.apigateway.main.utils;

import java.util.UUID;

public class CorrelationId {
    public String getCorrelationId() {
        UUID correlationId = UUID.randomUUID();
        return correlationId.toString();
    }
}