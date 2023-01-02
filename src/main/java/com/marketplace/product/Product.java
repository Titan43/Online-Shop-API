package com.marketplace.product;

import com.marketplace.order.OrderedProduct;
import com.marketplace.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double price;
    private Long quantity;
    @Lob
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "product")
    private Set<OrderedProduct> orderedProducts = new HashSet<>();

}