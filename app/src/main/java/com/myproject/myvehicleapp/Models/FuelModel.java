package com.myproject.myvehicleapp.Models;

public class FuelModel {

    public String fuelName; // Name of the fuel
    public String fuelType; // Type of the fuel

    public FuelModel() {
        // Default constructor required for Firebase
    }

    // Getters and setters for the FuelModel properties

    public String getFuelName() {
        return fuelName;
    }

    public void setFuelName(String fuelName) {
        this.fuelName = fuelName;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
}

