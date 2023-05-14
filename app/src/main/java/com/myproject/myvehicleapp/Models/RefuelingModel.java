package com.myproject.myvehicleapp.Models;

import com.google.firebase.Timestamp;

// This class serves as a data model for a Refueling object
public class RefuelingModel {


    public String recyclerTitle; //The title for the Recycler view item
    public Integer refuelingOdometer; //The odometer reading at the time of refueling
    public String refuelingFuelType; //The type of fuel used for refueling
    public Float refuelingPricePerLitre; //The price per litre of the fuel used
    public Float refuelingTotalCost; //The total cost of the refueling
    public Float refuelingFuelLitres; //The quantity of fuel in litres bought during refueling
    public String refuelingPaymentMethod; //The payment method used for the refueling
    public String refuelingNote; //Any additional notes related to the refueling
    public Timestamp refuelingTimestamp; //The timestamp when the refueling took place

    // Default constructor
    public RefuelingModel() {
    }

    // Getter and Setter methods for each field

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
