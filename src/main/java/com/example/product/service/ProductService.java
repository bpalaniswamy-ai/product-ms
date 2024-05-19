package com.example.product.service;

import com.example.product.entity.Product;
import com.example.product.events.ProductEvents;
import com.example.product.exception.EntityNotFoundException;
import com.example.product.repository.ProductRepository;
import com.example.product.request.ProductRequest;
import com.example.product.response.EntityResponse;
import com.example.product.util.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletionStage;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, Object> template;

    public EntityResponse saveProduct(ProductRequest productRequest){
        log.info("ProductService::saveProduct ::  {}", productRequest);
        Product product = productRepository.save(Mapper.mapRequestToEntity(productRequest));
        sendProductToKafka(new ProductEvents.ProductCreated(product))
                .thenApply(done -> product).toCompletableFuture().join();
        return new EntityResponse(HttpStatus.CREATED.value(), product.getId(), "Success");
    }

    public Product getProduct(String id) {
        log.info("getting Product ::  {}", id);
        return productRepository.findById(id).orElse(null);
    }

    public EntityResponse updateProduct(String id, ProductRequest productRequest) {
        log.info("updating Product ::  {} with {}", id, productRequest);
        Product product = getProduct(id);
        if (product == null) {
            throw new EntityNotFoundException("Product not found for id: " + id);
        }
        productRepository.save(Mapper.mapRequestToEntityForUpdate(product, productRequest));
        sendProductToKafka(new ProductEvents.ProductUpdated(product))
                .thenApply(done -> product).toCompletableFuture().join();
        return new EntityResponse(HttpStatus.OK.value(), product.getId(), "Success");
    }

    public EntityResponse deleteProduct(String id) {
        log.info("deleting Product ::  {}", id);
        Product product = getProduct(id);
        if (product == null) {
            throw new EntityNotFoundException("Product not found for id: " + id);
        }
        productRepository.deleteById(id);
        sendProductToKafka(new ProductEvents.ProductDeleted(id))
                .thenApply(done -> true).toCompletableFuture().join();
        return new EntityResponse(HttpStatus.OK.value(), product.getId(), "Success");
    }

    public Iterable<Product> getAllProducts() {
        log.info("getting all Products");
        return productRepository.findAll();
    }

    public CompletionStage<Boolean> sendProductToKafka(ProductEvents events) {
        log.info("sending ProductEvent to Kafka ::  {}", events);
        return template.send("product-kafka-topic", events).thenApply(done -> {
            log.info("ProductEvent sent to Kafka");
            return true;
        });
    }

}
