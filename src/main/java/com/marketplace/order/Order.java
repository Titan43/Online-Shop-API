package com.marketplace.order;

import com.marketplace.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Order\"")
@AllArgsConstructor
@NoArgsConstructor
@Data public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "order")
    private Set<OrderedProduct> orderedProducts = new HashSet<>();
}
