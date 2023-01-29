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

}
