package com.marketplace.order;

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
