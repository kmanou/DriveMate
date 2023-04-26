package com.myproject.myvehicleapp.Models;

import com.google.firebase.Timestamp;

public class ServiceModel {

    public String recyclerTitle;
    public Timestamp serviceTimeStamp;
    public Integer serviceOdometer;
    public String serviceTypeOfService;
    public String servicePaymentMethod;
    public Float serviceTotalCost;
    public String serviceNote;

    public ServiceModel() {
    }

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