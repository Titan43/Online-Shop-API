package com.marketplace.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.marketplace.product.Product;
import com.marketplace.product.productService.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.marketplace.constants.APIConstants.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(path = API_PREFIX+"product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<String> addNewProduct(@RequestBody Product product, Principal principal) {
        return productService.addNewProduct(product, principal);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id, Principal principal) {
        return productService.deleteProduct(id, principal);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @GetMapping(path = "/products")
    public ResponseEntity<?> getProducts(@RequestParam String page,
                                         @RequestParam String count) {
        return productService.getProducts(page, count);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> changeProductQuantity(@PathVariable String id,
                                                   @RequestBody JsonNode quantityJson,
                                                   Principal principal) {
        return productService.changeProductQuantity(id, quantityJson, principal);
    }
}
