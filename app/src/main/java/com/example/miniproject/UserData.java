package com.example.miniproject;

public class UserData {
    String username,aadharId,phoneNumber,email;
    public UserData(){}
    public UserData(String username,String aadharId,String phoneNumber,String email)
    {
        this.username=username;
        this.aadharId=aadharId;
        this.phoneNumber=phoneNumber;
        this.email=email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAadharId() {

        return aadharId;
    }

    public void setAadharId(String aadharId) {
        this.aadharId = aadharId;
    }
}