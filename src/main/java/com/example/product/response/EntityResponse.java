package com.example.product.response;

import lombok.Data;

@Data
public class EntityResponse {
    private int status;

    private String id;
    private String message;

    public EntityResponse(int status, String id,String message) {
        this.status = status;
        this.id = id;
        this.message = message;
    }
}
