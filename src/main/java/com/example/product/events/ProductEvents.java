package com.example.product.events;

import com.example.product.entity.Product;
import lombok.AllArgsConstructor;

public interface ProductEvents {
    @AllArgsConstructor
    public class ProductCreated implements ProductEvents {
        public String type = "product-created";
        public Product product;

        public ProductCreated(Product product) {
            this.product = product;
        }
    }

    class ProductUpdated implements ProductEvents {
        public String type = "product-updated";
        public Product product;

        public ProductUpdated(Product product) {
            this.product = product;
        }
    }

    @AllArgsConstructor
    public class ProductDeleted implements ProductEvents {
        public String type = "product-deleted";
        public String id;

        public ProductDeleted(String id) {
            this.id = id;
        }
    }
}
