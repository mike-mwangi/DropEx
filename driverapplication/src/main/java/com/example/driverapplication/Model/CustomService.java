package com.example.driverapplication.Model;

import com.graphhopper.directions.api.client.model.Service;

public class CustomService extends Service {
    private int position;
    private String deliveryNote;
    private String customerName;
    private String customerPhoneNumber;
    private String assignedDriverID;
    private String status;
    private String itemImage;
    private String itemDeliveryVerificationCode;
    private int customSize;
    private Boolean delivered;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CustomService(int position, String deliveryNote, String customerName, String customerPhoneNumber, String assignedDriverID, String status, String itemImage, String itemDeliveryVerificationCode, int customSize, Boolean delivered) {
        this.position = position;
        this.deliveryNote = deliveryNote;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.assignedDriverID = assignedDriverID;
        this.status = status;
        this.itemImage = itemImage;
        this.itemDeliveryVerificationCode = itemDeliveryVerificationCode;
        this.customSize = customSize;
        this.delivered = delivered;
    }



    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }


    public CustomService() {
    }


    public int getCustomSize() {
        return customSize;
    }

    public void setCustomSize(int customSize) {
        this.customSize = customSize;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getAssignedDriverID() {
        return assignedDriverID;
    }

    public void setAssignedDriverID(String assignedDriverID) {
        this.assignedDriverID = assignedDriverID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemDeliveryVerificationCode() {
        return itemDeliveryVerificationCode;
    }

    public void setItemDeliveryVerificationCode(String itemDeliveryVerificationCode) {
        this.itemDeliveryVerificationCode = itemDeliveryVerificationCode;
    }
}
