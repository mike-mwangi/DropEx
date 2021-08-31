package com.example.dropex.Model;

public class DriverModel {


    private String firstName, lastName, phoneNumber, email, city,userImageUrl;
    private CustomVehicle vehicle;
    private PostedJob currentJob;

    public PostedJob getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(PostedJob currentJob) {
        this.currentJob = currentJob;
    }

    public CustomVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(CustomVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public DriverModel() {}

    public DriverModel(String firstName, String lastName, String phoneNumber, String email, String city, String userImageUrl, CustomVehicle vehicle, PostedJob currentJob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.city = city;
        this.userImageUrl = userImageUrl;
        this.vehicle = vehicle;
        this.currentJob = currentJob;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
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
