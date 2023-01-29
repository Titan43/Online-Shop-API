package com.marketplace.order;

import com.marketplace.order.orderService.OrderRepository;
import com.marketplace.order.orderService.OrderedProductRepository;
import com.marketplace.product.Product;
import com.marketplace.product.productService.ProductRepository;
import com.marketplace.user.User;
import com.marketplace.user.userService.UserRepository;
import com.marketplace.validator.ValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService implements com.marketplace.order.orderService.OrderService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final ValidatorService validatorService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final OrderedProductRepository orderedProductRepository;

    @Override
    public ResponseEntity<String> orderProduct(String id, String quantity, Principal principal) {

        Optional<User> user = userRepository.findUserByUsername(principal.getName());
        if (user.isEmpty()){
            return new ResponseEntity<>("Such User does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }

        if(validatorService.idIsNotValid(id)){
            return new ResponseEntity<>("Invalid id passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        if(validatorService.idIsNotValid(quantity)){
            return new ResponseEntity<>("Invalid quantity passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }

        long prodId, prodQuantity;
        try {
            prodId = Long.parseLong(id);
            prodQuantity = Long.parseLong(quantity);
            if(prodQuantity<1){
                return new ResponseEntity<>("Invalid quantity passed(CODE 400)", HttpStatus.BAD_REQUEST);
            }
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>("Invalid id or quantity passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }

        Optional<Product> product = productRepository.findByIdAvailable(prodId);

        if(product.isEmpty()) {
            return new ResponseEntity<>("Product with such id does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }

        Optional<Order> order = orderRepository.findUnfinishedByUserId(user.get().getId());
        Order userOrder;

        double amount = product.get().getPrice()*prodQuantity;

        if(order.isEmpty()){
            userOrder = orderRepository.save(new Order(user.get(), amount, LocalDate.now()));
        }
        else{
            userOrder = order.get();
            userOrder.setAmount(userOrder.getAmount()+amount);
            userOrder.setDate(LocalDate.now());
        }

        orderRepository.save(userOrder);

        OrderedProduct orderedProduct;

        Optional<OrderedProduct> previouslyOrderedProduct = orderedProductRepository.findByOrderIdAndProductId(
                userOrder.getId(), product.get().getId()
        );

        if(previouslyOrderedProduct.isEmpty()){
            orderedProduct = new OrderedProduct(userOrder, product.get(), prodQuantity, amount);
        }
        else{
            orderedProduct = previouslyOrderedProduct.get();
            orderedProduct.setAmount(orderedProduct.getAmount()+amount);
            orderedProduct.setQuantity(orderedProduct.getQuantity()+prodQuantity);
        }

        orderedProductRepository.save(orderedProduct);


        return new ResponseEntity<>("Product was successfully ordered(CODE 201)", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> removeOrderedProduct(String id, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> cancelOrder(Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> confirmOrder(Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<?> showMyOrder(Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<?> showOrderById(String id, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<?> showOrders(String page, String count, Principal principal) {
        return null;
    }
}
