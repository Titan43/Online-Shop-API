package com.marketplace.product.productService;

import com.fasterxml.jackson.databind.JsonNode;
import com.marketplace.product.Product;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface ProductService {
    ResponseEntity<String> addNewProduct(Product product, Principal principal);
    ResponseEntity<String> deleteProduct(String id, Principal principal);
    ResponseEntity<?> getProduct(String id);
    ResponseEntity<?> getProducts(String page, String count);
    ResponseEntity<?> changeProductQuantity(String id, JsonNode quantityJson, Principal principal);
}
