package com.marketplace.user;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO
    )
    private Long id;
    private String fName;
    private String sName;
    private String username;
    private String email;
    private LocalDate dob;
    private String phoneNumber;
    @Enumerated(EnumType.ORDINAL)
    private UserRole role;

    public User() {
    }

    public User(String fName, String sName, String username, String email, LocalDate dob, String phoneNumber, UserRole role) {
        this.fName = fName;
        this.sName = sName;
        this.username = username;
        this.email = email;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public User(Long id, String fName, String sName, String username, String email, LocalDate dob, String phoneNumber, UserRole role) {
        this.id = id;
        this.fName = fName;
        this.sName = sName;
        this.username = username;
        this.email = email;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getfName() {
        return fName;
    }

    public String getsName() {
        return sName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserRole getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fName='" + fName + '\'' +
                ", sName='" + sName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                '}';
    }
}
