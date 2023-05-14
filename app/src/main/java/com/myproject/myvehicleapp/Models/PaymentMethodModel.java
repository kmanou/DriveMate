package com.myproject.myvehicleapp.Models;

// This class serves as a data model for a PaymentMethod object
public class PaymentMethodModel {

    // The payment method description
    public String paymentMethod;

    // Default constructor
    public PaymentMethodModel() {
    }

    // Getter method for the paymentMethod field
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
