package com.marketplace.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketplace.order.orderEntities.OrderedProduct;
import com.marketplace.user.userEntities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Data public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double price;
    private Long quantity;
    @JsonIgnore
    private boolean isAvailable = true;
    @Lob
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Set<OrderedProduct> orderedProducts = new HashSet<>();

    @Transient
    private Long user_id;

    @JsonIgnore
    @Version
    private Long version;

    public Long getUser_id() {
        if(user != null){
            return user.getId();
        }
        return null;
    }

    public Product(String name, Double price, Long quantity, String description, User user) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.user = user;
    }
}