package com.marketplace.product.productService;

import com.marketplace.product.Product;
import com.marketplace.validator.IValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ProductService implements IProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IValidatorService validatorService;

    @Override
    public ResponseEntity<String> addNewProduct(Product product, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteProduct(String id, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<?> getProduct(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> getProducts(String page, String count) {

        if(!validatorService.idIsValid(page)){
            return new ResponseEntity<>("Invalid page number passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(!validatorService.idIsValid(count)){
            return new ResponseEntity<>("Invalid entity count passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        Page<Product> productPage;

        try {
            productPage = productRepository.findAll(
                    PageRequest.of(Integer.parseInt(page), Integer.parseInt(count)
                    ));
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>("Invalid entity count or page number passed(CODE 400)",
                    HttpStatus.BAD_REQUEST);
        }
        List<Product> products = productPage.getContent();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
