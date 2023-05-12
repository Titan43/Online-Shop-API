package com.marketplace.order.orderEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketplace.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"orderedProduct\"")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(OrderedProductId.class)
@Data public class OrderedProduct {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Order order;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Product product;
    @Transient
    private Long productId;
    @Transient
    private String productName;
    private Long quantity;
    private Double totalCost;

    @Transient
    private byte[] image;

    public Long getProductId() {
        return product.getId();
    }

    public String getProductName() {
        return product.getName();
    }

    public byte[] getImage() {
        return product.getImage();
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = Math.round(totalCost *100.0)/100.0;
    }

    public OrderedProduct(Order order, Product product, Long quantity, Double totalCost) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.totalCost = totalCost;
    }
}
