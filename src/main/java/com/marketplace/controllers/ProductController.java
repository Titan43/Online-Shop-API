package com.marketplace.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.marketplace.product.Product;
import com.marketplace.product.productService.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

import static com.marketplace.constants.IAPIConstants.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(path = API_PREFIX+"product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping
    public ResponseEntity<String> addNewProduct(@RequestBody Product product, Principal principal) {
        return productService.addNewProduct(product, principal);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProduct(@RequestParam String id, Principal principal) {
        return productService.deleteProduct(id, principal);
    }

    @GetMapping
    public ResponseEntity<?> getProduct(@RequestParam String id) {
        return productService.getProduct(id);
    }

    @GetMapping(path = "/products")
    public ResponseEntity<?> getProducts(@RequestParam String page,
                                         @RequestParam String count) {
        return productService.getProducts(page, count);
    }

    @PutMapping
    public ResponseEntity<?> changeProductQuantity(@RequestParam String id,
                                                   @RequestBody JsonNode quantityJson,
                                                   Principal principal) {
        return productService.changeProductQuantity(id, quantityJson, principal);
    }
}
