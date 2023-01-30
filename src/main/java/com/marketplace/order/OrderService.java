package com.marketplace.order;

import com.marketplace.order.orderEntities.Order;
import com.marketplace.order.orderEntities.OrderedProduct;
import com.marketplace.order.orderService.OrderRepository;
import com.marketplace.order.orderService.OrderedProductRepository;
import com.marketplace.product.Product;
import com.marketplace.product.productService.ProductRepository;
import com.marketplace.user.userEntities.User;
import com.marketplace.user.userEntities.UserRole;
import com.marketplace.user.userService.UserRepository;
import com.marketplace.validator.ValidatorService;
import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

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
            userOrder.setTotalCost(userOrder.getTotalCost()+amount);
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
            orderedProduct.setTotalCost(orderedProduct.getTotalCost()+amount);
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

        Optional<User> user = userRepository.findUserByUsername(principal.getName());
        if (user.isEmpty()){
            return new ResponseEntity<>("Such User does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }
        Optional<Order> unfinishedOrder = orderRepository.findUnfinishedByUserId(user.get().getId());
        if(unfinishedOrder.isEmpty()){
            return new ResponseEntity<>("Theres no pending order(CODE 404)", HttpStatus.NOT_FOUND);
        }

        List<OrderedProduct> orderedProducts = orderedProductRepository.findAllByOrderId(unfinishedOrder.get().getId());
        orderedProductRepository.deleteAll(orderedProducts);
        orderRepository.delete(unfinishedOrder.get());

        return new ResponseEntity<>("Order was successfully canceled(CODE 200)", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> confirmOrder(Principal principal) {
        Optional<User> user = userRepository.findUserByUsername(principal.getName());
        if (user.isEmpty()){
            return new ResponseEntity<>("Such User does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }
        Optional<Order> unfinishedOrder = orderRepository.findUnfinishedByUserId(user.get().getId());
        if(unfinishedOrder.isEmpty()){
            return new ResponseEntity<>("Theres no pending order to confirm(CODE 404)", HttpStatus.NOT_FOUND);
        }

        List<OrderedProduct> orderedProducts = orderedProductRepository.findAllByOrderId(unfinishedOrder.get().getId());
        List<Product> products = new ArrayList<>();


        for(OrderedProduct orderedProduct: orderedProducts){
            Optional<Product> product = productRepository.findByIdAvailable(orderedProduct.getProductId());
            if(product.isEmpty()){
                return new ResponseEntity<>(
                        "Order cannot be confirmed, Product with id:"
                                +orderedProduct.getProductId()+
                                " is unavailable (CODE 404)", HttpStatus.NOT_FOUND);
            }
            else if(product.get().getQuantity()<orderedProduct.getQuantity()){
                return new ResponseEntity<>(
                        "Order cannot be confirmed, such quantity of Product with id:"
                                +orderedProduct.getProductId()+
                                " cannot be ordered (CODE 409)", HttpStatus.CONFLICT);
            }
            Product updatedProduct = product.get();
            updatedProduct.setQuantity(updatedProduct.getQuantity() - orderedProduct.getQuantity());
            products.add(updatedProduct);
        }

        try{
            productRepository.saveAll(products);
        }
        catch (OptimisticLockException e){
            return new ResponseEntity<>(
                    "Order cannot be confirmed right now(CODE 409)", HttpStatus.CONFLICT);
        }

        Order order = unfinishedOrder.get();
        order.setFinished(true);
        order.setDate(LocalDate.now());
        orderRepository.save(order);

        return new ResponseEntity<>(
                "Order was successfully confirmed(CODE 200)", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> showMyOrder(Principal principal) {
        Optional<User> user = userRepository.findUserByUsername(principal.getName());
        if (user.isEmpty()){
            return new ResponseEntity<>("Such User does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }
        Optional<Order> unfinishedOrder = orderRepository.findUnfinishedByUserId(user.get().getId());
        if(unfinishedOrder.isEmpty()){
            return new ResponseEntity<>("Theres no pending order to show(CODE 404)", HttpStatus.NOT_FOUND);
        }

        Map<String, Object> orderDetails = new HashMap<>();

        List<OrderedProduct> orderedProducts = orderedProductRepository.findAllByOrderId(unfinishedOrder.get().getId());

        orderDetails.put("ordered_products", orderedProducts);
        orderDetails.put("total_price", unfinishedOrder.get().getTotalCost());
        orderDetails.put("date", unfinishedOrder.get().getDate());

        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> showOrderById(String id, Principal principal) {
        Optional<User> user = userRepository.findUserByUsername(principal.getName());
        if (user.isEmpty()){
            return new ResponseEntity<>("Such User does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }
        else if(!user.get().getRole().equals(UserRole.MANAGER)){
            return new ResponseEntity<>("You don't have enough rights to preview other orders(CODE 403)",
                    HttpStatus.NOT_FOUND);
        }
        if(validatorService.idIsNotValid(id)){
            return new ResponseEntity<>("Invalid id passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        long orderId;
        try {
            orderId = Long.parseLong(id);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>("Invalid id passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }

        Optional<Order> order = orderRepository.findByOrderId(orderId);
        if(order.isEmpty()){
            return new ResponseEntity<>("Order with such id does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }

        Map<String, Object> orderDetails = new HashMap<>();

        List<OrderedProduct> orderedProducts = orderedProductRepository.findAllByOrderId(order.get().getId());

        orderDetails.put("order_owner", order.get().getUserId());
        orderDetails.put("confirmed", order.get().isFinished());
        orderDetails.put("ordered_products", orderedProducts);
        orderDetails.put("total_price", order.get().getTotalCost());
        orderDetails.put("date", order.get().getDate());

        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> showOrders(String page, String count, Principal principal) {

        Optional<User> user = userRepository.findUserByUsername(principal.getName());
        if (user.isEmpty()){
            return new ResponseEntity<>("Such User does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        }
        else if(!user.get().getRole().equals(UserRole.MANAGER)){
            return new ResponseEntity<>("You don't have enough rights to preview other orders(CODE 403)",
                    HttpStatus.NOT_FOUND);
        }

        if(validatorService.idIsNotValid(page)){
            return new ResponseEntity<>("Invalid page number passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(validatorService.idIsNotValid(count)){
            return new ResponseEntity<>("Invalid entity count passed(CODE 400)", HttpStatus.BAD_REQUEST);
        }

        Page<Order> orderPage;

        try {
            orderPage = orderRepository.findAll(
                    PageRequest.of(Integer.parseInt(page), Integer.parseInt(count)
                    ));
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>("Invalid entity count or page number passed(CODE 400)",
                    HttpStatus.BAD_REQUEST);
        }

        List<Order> orders = orderPage.getContent();

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
