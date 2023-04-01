package com.marketplace.user;

import com.marketplace.order.orderEntities.Order;
import com.marketplace.order.orderService.OrderRepository;
import com.marketplace.order.orderService.OrderedProductRepository;
import com.marketplace.product.Product;
import com.marketplace.product.productService.ProductRepository;
import com.marketplace.constants.APIConstants;
import com.marketplace.user.userEntities.User;
import com.marketplace.user.userEntities.UserRole;
import com.marketplace.user.userService.UserRepository;
import com.marketplace.validator.ValidatorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static com.marketplace.constants.APIConstants.API_PREFIX;
import static com.marketplace.constants.APIConstants.ITEM_LINK_START;

@Service
@Qualifier("firstImplementation")
@RequiredArgsConstructor
public class UserService implements com.marketplace.user.userService.UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final ValidatorService validatorService;

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final OrderedProductRepository orderedProductRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    private UserRole requestSenderRole;

    @Override
    public ResponseEntity<String> addNewUser(User user) {
        if(validatorService.usernameIsNotValid(user.getUsername())){
            return new ResponseEntity<>("Invalid username (CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(validatorService.emailIsNotValid(user.getEmail())) {
            return new ResponseEntity<>("Invalid email (CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(validatorService.phoneNumberIsNotValid(user.getPhoneNumber())) {
            return new ResponseEntity<>("Invalid phone number (CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if (userRepository.findUserByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<>("User with such username already exists (CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>( "User with such email already exists (CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(validatorService.ageIsNotValid(user.getDob())) {
            return new ResponseEntity<>( "Invalid date of birth entered. You should be at least " +
                    APIConstants.VALID_AGE + " years old to use the marketplace (CODE 400)", HttpStatus.BAD_REQUEST);
        }
        user.setRole(UserRole.BUYER);

        if(user.getFName()==null){
            user.setFName("");
        }
        if(user.getSName()==null){
            user.setSName("");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromPath(ITEM_LINK_START+API_PREFIX+"user")
                .queryParam("username", user.getUsername())
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>("User was successfully created(CODE 201)", headers, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deleteUser(String username, Principal principal) {
        if(!principal.getName().equals(username))
            return new ResponseEntity<>("Yor token is not valid for this user(CODE 403)", HttpStatus.FORBIDDEN);
        else if(validatorService.usernameIsNotValid(username)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username has to be provided (CODE 400)");
        }
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Such user does not exist(CODE 404)");

        List<Product> userProducts = productRepository.findAllByUserId(user.get().getId());
        userProducts.forEach(
                p->{
                    p.setAvailable(false);
                    p.setUser(null);
                });
        productRepository.saveAll(
                userProducts
        );

        Optional<Order> unfinishedOrder = orderRepository.findUnfinishedByUserId(user.get().getId());
        if(unfinishedOrder.isPresent()){
            orderedProductRepository.deleteAll(
                    orderedProductRepository.findAllByOrderId(unfinishedOrder.get().getId())
            );
            orderRepository.delete(unfinishedOrder.get());
        }

        List<Order> userOrders = orderRepository.findAllByUserId(user.get().getId());
        userOrders.forEach(o-> o.setUser(null));
        orderRepository.saveAll(userOrders);

        userRepository.deleteById(user.get().getId());

        return new ResponseEntity<>("User deleted successfully(CODE 200)", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUser(String username, Principal principal) {

        if(username!=null) {
            if (validatorService.usernameIsNotValid(username)) {
                return new ResponseEntity<>("Invalid username(CODE 400)", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            username = principal.getName();
        }

        Optional<User> requestSender = userRepository.findUserByUsername(principal.getName());
        if (requestSender.isEmpty()){
            return new ResponseEntity<>("Owner of this token does not exist(CODE 401)", HttpStatus.UNAUTHORIZED);
        }
        requestSenderRole = requestSender.get().getRole();
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isEmpty())
            return new ResponseEntity<>("User not found (CODE 404)", HttpStatus.NOT_FOUND);
        else if(!principal.getName().equals(username) && !requestSenderRole.equals(UserRole.MANAGER))
            return new ResponseEntity<>("Your token is not valid for this action(CODE 403)", HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateUser(String username, User user, Principal principal) {
        if(user.toString().equals(new User().toString())) {
            return new ResponseEntity<>( "No data to update or wrong fields passed(CODE 400)",HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<?> oldUser = getUser(username, principal);
        if(oldUser.getStatusCode() != HttpStatus.OK)
            return new ResponseEntity<>((String)oldUser.getBody(), oldUser.getStatusCode());
        User oldUserData = (User) oldUser.getBody();
        if(oldUserData == null){
            throw new IllegalStateException("Something went wrong");
        }
        if(user.getId() != null){
            return new ResponseEntity<>("UserId cannot be changed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(user.getUsername()!=null){
            return new ResponseEntity<>("Username cannot be changed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(user.getEmail()!=null){
            return new ResponseEntity<>( "User email cannot be changed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(user.getRole() != null){
            if(!requestSenderRole.equals(UserRole.MANAGER)) {
                return new ResponseEntity<>("User role cannot be changed by ordinary User(CODE 400)",
                        HttpStatus.BAD_REQUEST);
            }
            oldUserData.setRole(user.getRole());
        }

        if(user.getDob() != null){
            if(validatorService.ageIsNotValid(user.getDob()))
                return new ResponseEntity<>( "Invalid date of birth entered. You should be at least " +
                        APIConstants.VALID_AGE + " years old to use the marketplace (CODE 400)", HttpStatus.BAD_REQUEST);
            oldUserData.setDob(user.getDob());
        }
        if(user.getPhoneNumber()!= null && !user.getPhoneNumber().trim().equals("")){
            if(validatorService.phoneNumberIsNotValid(user.getPhoneNumber()))
                return new ResponseEntity<>("Invalid phone number (CODE 400)", HttpStatus.BAD_REQUEST);
            oldUserData.setPhoneNumber(user.getPhoneNumber());
        }
        if(user.getFName()!=null && !user.getFName().trim().equals(""))
            oldUserData.setFName(user.getFName());
        if(user.getSName()!=null && !user.getSName().trim().equals(""))
            oldUserData.setSName(user.getSName());
        if(user.getPassword()!=null){
            oldUserData.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(oldUserData);
        return new ResponseEntity<>("User updated successfully(CODE 200)", HttpStatus.OK);
    }
}