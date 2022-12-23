package com.marketplace.user;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class User {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "user_sequence"
    )
    private Long id;
    private String fName;
    private String sName;
    private LocalDate dob;
    private String phoneNumber;
    @Enumerated(EnumType.ORDINAL)
    private UserRole role;

    public User() {}

    public User(Long id, String fName, String sName, LocalDate dob, String phoneNumber, UserRole role) {
        this.id = id;
        this.fName = fName;
        this.sName = sName;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public User(String fName, String sName, LocalDate dob, String phoneNumber, UserRole role) {
        this.fName = fName;
        this.sName = sName;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fName='" + fName + '\'' +
                ", sName='" + sName + '\'' +
                ", dob=" + dob +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
