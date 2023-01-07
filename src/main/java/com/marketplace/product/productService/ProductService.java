package com.marketplace.product.productService;

import com.marketplace.product.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProductService implements IProductService{
    @Override
    public ResponseEntity<String> addNewProduct(Product product, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteProduct(String id, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> getProduct(String id) {
        return null;
    }

    @Override
    public ResponseEntity<String> getProducts(String page, String count) {
        return null;
    }
}
