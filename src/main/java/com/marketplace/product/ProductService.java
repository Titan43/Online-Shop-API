package com.marketplace.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.marketplace.product.productService.ProductRepository;
import com.marketplace.user.User;
import com.marketplace.user.UserRole;
import com.marketplace.user.userService.UserRepository;
import com.marketplace.validator.ValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static com.marketplace.constants.APIConstants.API_PREFIX;
import static com.marketplace.constants.APIConstants.ITEM_LINK_START;

@Service
@AllArgsConstructor
public class ProductService implements com.marketplace.product.productService.ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ValidatorService validatorService;

    @Override
    public ResponseEntity<String> addNewProduct(Product product, Principal principal) {

        Optional<User> seller = userRepository.findUserByUsername(principal.getName());
        if (seller.isEmpty()){
            return new ResponseEntity<>("Such User does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }

        if(!seller.get().getRole().equals(UserRole.VENDOR)){
            return new ResponseEntity<>("Only Vendor is allowed to provide products(CODE 403)", HttpStatus.FORBIDDEN);
        }

        product.setUser(seller.get());

        if(validatorService.usernameIsNotValid(product.getName())){
            return new ResponseEntity<>("Invalid product name passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(product.getPrice()==null || product.getPrice()<0){
            return new ResponseEntity<>("Invalid price passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(product.getQuantity() == null || product.getQuantity()<0){
            return new ResponseEntity<>("Invalid quantity passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(product.getDescription() == null || product.getDescription().strip().equals("")){
            return new ResponseEntity<>("Invalid description passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }

        Product savedProduct = productRepository.save(product);
        URI location = ServletUriComponentsBuilder
                .fromPath(ITEM_LINK_START+API_PREFIX+"product")
                .queryParam("id", savedProduct.getId())
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>("Product was successfully created(CODE 201)", headers, HttpStatus.CREATED);
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

        Optional<Product> product = productRepository.findByIdAvailable(prodId);

        if(product.isEmpty()){
            return new ResponseEntity<>("Product with such id does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }

        Optional<User> user = userRepository.findById(
                product.get().getUser_id()
        );

        if(user.isEmpty()){
            return new ResponseEntity<>("Owner of this product does not exist (CODE 404)",
                    HttpStatus.NOT_FOUND);
        }

        if(!user.get().getUsername().equals(principal.getName())){
            return new ResponseEntity<>("Yor token is not valid for this user(CODE 403)", HttpStatus.FORBIDDEN);
        }

        Product productForDeletion = product.get();
        productForDeletion.setAvailable(false);
        productRepository.save(productForDeletion);

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

        Optional<Product> product = productRepository.findByIdAvailable(prodId);

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
            productPage = productRepository.findAllAvailable(
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

    @Override
    public ResponseEntity<?> changeProductQuantity(String id, JsonNode quantityJson, Principal principal) {
        if(validatorService.idIsNotValid(id)){
            return new ResponseEntity<>("Invalid id passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(!quantityJson.has("changeQuantityBy")){
            return new ResponseEntity<>("changeQuantityBy field was not passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }

        JsonNode quantityField = quantityJson.get("changeQuantityBy");

        if(!(quantityField.isLong() || quantityField.isInt())){
            return new ResponseEntity<>("changeQuantityBy field has to be a number(CODE 400)", HttpStatus.BAD_REQUEST);
        }

        long prodId, prodQuantity = quantityJson.get("changeQuantityBy").asLong();
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

        Product updatedProduct = product.get();

        if(updatedProduct.getQuantity()+prodQuantity<0){
            updatedProduct.setQuantity(0L);
        }
        else{
            updatedProduct.setQuantity(updatedProduct.getQuantity()+prodQuantity);
        }

        productRepository.save(updatedProduct);

        return new ResponseEntity<>("Product quantity updated successfully(CODE 200)", HttpStatus.OK);
    }
}
