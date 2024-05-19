package com.example.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    String id;
    String name;
    String description;
}
