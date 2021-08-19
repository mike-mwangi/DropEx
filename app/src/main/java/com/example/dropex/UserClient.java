package com.example.dropex;

import android.app.Application;

import com.example.dropex.Model.CustomerModel;

public class UserClient extends Application {
    CustomerModel customer=null;

    public CustomerModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
    }
}
