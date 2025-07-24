package com.apigateway.apigateway.main.dto;

public class RequestBodyDto {
    private String type;
    private String origin;
    private DataDto data;

    public RequestBodyDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
