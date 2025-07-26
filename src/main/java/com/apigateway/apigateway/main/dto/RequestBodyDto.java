package com.apigateway.apigateway.main.dto;

import com.apigateway.apigateway.main.enums.parametrosBody.TypeRequest;
public class RequestBodyDto {
    private TypeRequest type;
    private String origin;
    private DataDto data;

    public RequestBodyDto() {
    }

    public TypeRequest getType() {
        return type;
    }

    public void setType(TypeRequest type) {
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public DataDto getData() {
        return data;
    }

    public void setData(DataDto data) {
        this.data = data;
    }

}
