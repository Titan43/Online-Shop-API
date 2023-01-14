package com.marketplace.product.productService;

import com.marketplace.product.Product;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface IProductService {
    ResponseEntity<String> addNewProduct(Product product, Principal principal);
    ResponseEntity<String> deleteProduct(String id, Principal principal);
    ResponseEntity<?> getProduct(String id);
    ResponseEntity<?> getProducts(String page, String count);
}
