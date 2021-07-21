package com.example.dropex.Common;

import com.example.dropex.Model.CustomerModel;

public class Common {
    public static final String CUSTOMER_INFO_REFERENCE = "Customers";
    public static CustomerModel currentCustomer;

    public static String buildWelcomeMessage() {
        //Review code here
        if (Common.currentCustomer != null) {
            return String.valueOf(new StringBuilder("Welcome")
                    .append(Common.currentCustomer.getFirstName())
                    .append(" ")
                    .append(Common.currentCustomer.getLastName().toString()));
        } else
            return "";

    }
}
