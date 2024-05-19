package com.example.product.service;

import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import com.example.product.request.ProductRequest;
import com.example.product.util.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Product saveProduct(ProductRequest productRequest){
        log.info("ProductService::saveProduct ::  {}", productRequest);
        Product product = productRepository.save(Mapper.mapRequestToEntity(productRequest));
        return sendProductToKafka(product)
                .thenApply(done -> product).toCompletableFuture().join();
    }

    public Product getProduct(String id) {
        log.info("getting Product ::  {}", id);
        return productRepository.findById(id).orElse(null);
    }


    public void deleteProduct(String id) {
        log.info("deleting Product ::  {}", id);
        productRepository.deleteById(id);
    }

    public CompletionStage<Boolean> sendProductToKafka(Product product) {
        log.info("sending Product ::  {}", product);
        return template.send("product-kafka-topic", product).thenApply(done -> {
            log.info("Product sent to Kafka");
            return true;
        });
    }

}
