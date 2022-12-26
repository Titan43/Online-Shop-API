package com.marketplace.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO
    )
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
