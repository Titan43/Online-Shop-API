package com.marketplace.user;

import java.time.LocalDate;

public class User {
    private Long id;
    private String fName;
    private String sName;
    private LocalDate dob;
    private String phoneNumber;

    public User(Long id, String fName, String sName, LocalDate dob, String phoneNumber) {
        this.id = id;
        this.fName = fName;
        this.sName = sName;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
    }

    public User(String fName, String sName, LocalDate dob, String phoneNumber) {
        this.fName = fName;
        this.sName = sName;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
    }

    public User() {
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
