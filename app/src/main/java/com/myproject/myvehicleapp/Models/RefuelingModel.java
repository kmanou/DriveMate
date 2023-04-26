package com.myproject.myvehicleapp.Models;

import com.google.firebase.Timestamp;

public class RefuelingModel {

    public String recyclerTitle;
    public Integer refuelingOdometer;
    public String refuelingFuelType;
    public Float refuelingPricePerLitre;
    public Float refuelingTotalCost;
    public Float refuelingFuelLitres;
    public String refuelingPaymentMethod;
    public String refuelingNote;
    public Timestamp refuelingTimestamp;

    public RefuelingModel() {
    }

    public String getRecyclerTitle() {
        return recyclerTitle;
    }

    public void setRecyclerTitle(String recyclerTitle) {
        this.recyclerTitle = recyclerTitle;
    }

    public Integer getRefuelingOdometer() {
        return refuelingOdometer;
    }

    public void setRefuelingOdometer(Integer refuelingOdometer) {
        this.refuelingOdometer = refuelingOdometer;
    }

    public String getRefuelingFuelType() {
        return refuelingFuelType;
    }

    public void setRefuelingFuelType(String refuelingFuelType) {
        this.refuelingFuelType = refuelingFuelType;
    }

    public Float getRefuelingPricePerLitre() {
        return refuelingPricePerLitre;
    }

    public void setRefuelingPricePerLitre(Float refuelingPricePerLitre) {
        this.refuelingPricePerLitre = refuelingPricePerLitre;
    }

    public Float getRefuelingTotalCost() {
        return refuelingTotalCost;
    }

    public void setRefuelingTotalCost(Float refuelingTotalCost) {
        this.refuelingTotalCost = refuelingTotalCost;
    }

    public Float getRefuelingFuelLitres() {
        return refuelingFuelLitres;
    }

    public void setRefuelingFuelLitres(Float refuelingFuelLitres) {
        this.refuelingFuelLitres = refuelingFuelLitres;
    }

    public String getRefuelingPaymentMethod() {
        return refuelingPaymentMethod;
    }

    public void setRefuelingPaymentMethod(String refuelingPaymentMethod) {
        this.refuelingPaymentMethod = refuelingPaymentMethod;
    }

    public String getRefuelingNote() {
        return refuelingNote;
    }

    public void setRefuelingNote(String refuelingNote) {
        this.refuelingNote = refuelingNote;
    }

    public Timestamp getRefuelingTimestamp() {
        return refuelingTimestamp;
    }

    public void setRefuelingTimestamp(Timestamp refuelingTimestamp) {
        this.refuelingTimestamp = refuelingTimestamp;
    }
}
