package com.marketplace.order;

import com.marketplace.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"orderedProduct\"")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(OrderedProductId.class)
public class OrderedProduct {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private Order order;
    @Transient
    private Long orderId;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;
    @Transient
    private Long productId;
    private Long quantity;
    private Double amount;

    public Long getOrderId() {
        return order.getId();
    }

    public Long getProductId() {
        return product.getId();
    }
}
