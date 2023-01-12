package com.marketplace.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketplace.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fName;
    private String sName;
    private String username;
    private String password;
    private String email;
    private LocalDate dob;
    private String phoneNumber;
    @Enumerated(EnumType.ORDINAL)
    private UserRole role;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Product> products = new HashSet<>();

    public User(String fName, String sName, String username, String password, String email, LocalDate dob, String phoneNumber, UserRole role) {
        this.fName = fName;
        this.sName = sName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
