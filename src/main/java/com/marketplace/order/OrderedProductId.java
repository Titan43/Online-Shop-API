package com.marketplace.order;

import com.marketplace.product.Product;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class OrderedProductId implements Serializable {

    private Order order;
    private Product product;

}
