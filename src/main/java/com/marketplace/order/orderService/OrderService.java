package com.marketplace.order.orderService;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface OrderService {
    ResponseEntity<String> orderProduct(String id, Principal principal);
    ResponseEntity<String> removeOrderedProduct(String id, Principal principal);
    ResponseEntity<String> cancelOrder(Principal principal);
    ResponseEntity<String> confirmOrder(Principal principal);
    ResponseEntity<?> showMyOrder(Principal principal);
    ResponseEntity<?> showOrderById(String id, Principal principal);
    ResponseEntity<?> showOrders(String page, String count, Principal principal);
}
