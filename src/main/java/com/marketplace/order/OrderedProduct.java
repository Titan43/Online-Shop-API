package com.marketplace.order;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
}
