package com.example.product.util;

import com.example.product.entity.Product;
import com.example.product.request.ProductRequest;

import java.util.UUID;

public class Mapper {
    public static Product mapRequestToEntity(ProductRequest request) {
        return new Product(
                request.getId() == null? UUID.randomUUID().toString() : request.getId(),
                request.getName(),
                request.getDescription()
        );
    }
}
