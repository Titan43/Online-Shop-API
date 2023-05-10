package com.marketplace.controllers;

import com.marketplace.order.orderService.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.marketplace.constants.APIConstants.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(path = API_PREFIX+"order")
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @GetMapping(path = "/orderProduct/{id}")
    public ResponseEntity<String> orderProduct(@PathVariable String id,
                                               @RequestParam String quantity,
                                               Principal principal) {
        return orderService.orderProduct(id, quantity, principal);
    }

    @DeleteMapping(path = "/cancel")
    public ResponseEntity<String> cancelOrder(Principal principal){
        return orderService.cancelOrder(principal);
    }

    @GetMapping(path = "/myOrder")
    public ResponseEntity<?> showMyOrder(Principal principal){
        return orderService.showMyOrder(principal);
    }

    @PostMapping(path = "/confirm")
    public ResponseEntity<String> confirmOrder(Principal principal){
        return orderService.confirmOrder(principal);
    }

    @GetMapping(path = "/{id}")
    public  ResponseEntity<?> showOrderById(@PathVariable String id, Principal principal){
        return orderService.showOrderById(id, principal);
    }

    @GetMapping(path = "/allOrders")
    public ResponseEntity<?> showOrders(@RequestParam String page, @RequestParam String count, Principal principal){
        return orderService.showOrders(page, count, principal);
    }

    @DeleteMapping(path = "/removeOrdered/{id}")
    public ResponseEntity<String> removeOrderedProduct(@PathVariable String id, Principal principal){
        return orderService.removeOrderedProduct(id, principal);
    }
}
