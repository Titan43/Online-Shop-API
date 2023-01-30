package com.marketplace.order.orderEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketplace.user.userEntities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"order\"")
@AllArgsConstructor
@NoArgsConstructor
@Data public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Transient
    private Long userId;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private Set<OrderedProduct> orderedProducts = new HashSet<>();
    private Double totalCost;
    private LocalDate date;
    private boolean confirmed = false;

    public Long getUserId() {
        if(user != null){
            return user.getId();
        }
        return null;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = Math.round(totalCost *100.0)/100.0;
    }

    public Order(User user, Double totalCost, LocalDate date) {
        this.user = user;
        this.totalCost = totalCost;
        this.date = date;
    }
}
