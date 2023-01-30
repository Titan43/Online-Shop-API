package com.marketplace.order.orderEntities;

import com.marketplace.product.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class OrderedProductId implements Serializable {

    private Order order;
    private Product product;

}
