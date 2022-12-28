package com.marketplace.shoppingCart;

import com.marketplace.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data public class OrderedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long quantity;
    @Transient
    private Double price;
    @ManyToOne
    private ShoppingCart shoppingCart;
    @ManyToOne
    private Product product;
}
