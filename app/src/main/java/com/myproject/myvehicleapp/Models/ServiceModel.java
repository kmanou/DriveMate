package com.myproject.myvehicleapp.Models;

import com.google.firebase.Timestamp;

// This class serves as a data model for a Service object
public class ServiceModel {

    public String recyclerTitle;// The title for the Recycler view item
    public Timestamp serviceTimeStamp;// The timestamp when the service took place
    public Integer serviceOdometer;// The odometer reading at the time of service
    public String serviceTypeOfService;// The type of service performed
    public String servicePaymentMethod;// The payment method used for the service
    public Float serviceTotalCost;// The total cost of the service
    public String serviceNote;// Any additional notes related to the service

    // Default constructor
    public ServiceModel() {
    }

    // Getter and Setter methods for each field will go here...

    public String getRecyclerTitle() {
        return recyclerTitle;
    }

    public void setRecyclerTitle(String recyclerTitle) {
        this.recyclerTitle = recyclerTitle;
    }

    public Timestamp getServiceTimeStamp() {
        return serviceTimeStamp;
    }

    public void setServiceTimeStamp(Timestamp serviceTimeStamp) {
        this.serviceTimeStamp = serviceTimeStamp;
    }

    public Integer getServiceOdometer() {
        return serviceOdometer;
    }

    public void setServiceOdometer(Integer serviceOdometer) {
        this.serviceOdometer = serviceOdometer;
    }

    public String getServiceTypeOfService() {
        return serviceTypeOfService;
    }

    public void setServiceTypeOfService(String serviceTypeOfService) {
        this.serviceTypeOfService = serviceTypeOfService;
    }

    public String getServicePaymentMethod() {
        return servicePaymentMethod;
    }

    public void setServicePaymentMethod(String servicePaymentMethod) {
        this.servicePaymentMethod = servicePaymentMethod;
    }

    public Float getServiceTotalCost() {
        return serviceTotalCost;
    }

    public void setServiceTotalCost(Float serviceTotalCost) {
        this.serviceTotalCost = serviceTotalCost;
    }

    public String getServiceNote() {
        return serviceNote;
    }

    public void setServiceNote(String serviceNote) {
        this.serviceNote = serviceNote;
    }
}