package com.example.product.contoller;

import com.example.product.entity.Product;
import com.example.product.exception.EntityNotFoundException;
import com.example.product.request.ProductRequest;
import com.example.product.response.EntityResponse;
import com.example.product.response.ErrorResponse;
import com.example.product.service.ProductService;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/ping")
    public String ping() {
        return "Product Service is Up";
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityResponse saveProduct(@RequestBody @Valid ProductRequest productRequest) {
        log.info("ProductController: saveProduct() called with request: {}", productRequest);
        return productService.saveProduct(productRequest);
    }

    @PutMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityResponse updateProduct(@PathVariable("id") String id, @RequestBody @Valid ProductRequest productRequest) {
        return productService.updateProduct(id, productRequest);
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") String id) {
        Product product = productService.getProduct(id);
        if (product != null)
            return product;
        throw new EntityNotFoundException("Product not found for id: " + id);
    }

    @DeleteMapping("/products/{id}")
    public EntityResponse deleteProduct(@PathVariable("id") String id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/products")
    public Iterable<Product> getAllProducts(@RequestParam(required = false, value = "limit", defaultValue = "0") Integer limit,
                                            @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset) {
        return productService.getAllProducts(limit, offset);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
