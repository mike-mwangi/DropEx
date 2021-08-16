package com.example.driverapplication.Model;

public class DriverModel {

    private String firstName, lastName, phoneNumber, email, city;

    public DriverModel() {}

    public DriverModel(String firstName, String lastName, String phoneNumber, String email, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.city = city;
//        this.userImageUrl = userImageUrl;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

//    public String getUserImageUrl() {
//        return userImageUrl;
//    }

//    public void setUserImageUrl(String userImageUrl) {
//        this.userImageUrl = userImageUrl;
//    }


}