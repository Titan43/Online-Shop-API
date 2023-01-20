package com.marketplace.product.productService;

import com.marketplace.product.Product;
import com.marketplace.user.User;
import com.marketplace.user.userService.UserRepository;
import com.marketplace.validator.IValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService implements IProductService{

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final IValidatorService validatorService;

    @Override
    public ResponseEntity<String> addNewProduct(Product product, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteProduct(String id, Principal principal) {

        if(validatorService.idIsNotValid(id)){
            return new ResponseEntity<>("Invalid id passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        long prodId;
        try {
            prodId = Long.parseLong(id);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>("Invalid id passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }

        Optional<Product> product = productRepository.findById(prodId);

        if(product.isEmpty()){
            return new ResponseEntity<>("Product with such id does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }

        Optional<User> user = userRepository.findById(
                product.get().getUser_id()
        );

        if(user.isEmpty()){
            return new ResponseEntity<>("Owner of this product does not exist (CODE 500)",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!user.get().getUsername().equals(principal.getName())){
            return new ResponseEntity<>("Yor token is not valid for this user(CODE 403)", HttpStatus.FORBIDDEN);
        }

        productRepository.delete(product.get());

        return new ResponseEntity<>("Product deleted successfully(CODE 200)", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getProduct(String id) {
        if(validatorService.idIsNotValid(id)){
            return new ResponseEntity<>("Invalid id passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        long prodId;
        try {
            prodId = Long.parseLong(id);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>("Invalid id passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }

        Optional<Product> product = productRepository.findById(prodId);

        if(product.isEmpty()){
            return new ResponseEntity<>("Product with such id does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getProducts(String page, String count) {

        if(validatorService.idIsNotValid(page)){
            return new ResponseEntity<>("Invalid page number passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(validatorService.idIsNotValid(count)){
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
