package com.myproject.myvehicleapp.Models;

// This class serves as a data model for a Vehicle object
public class VehicleModel {


    public String vehicle;// The vehicle type (e.g., car, motorcycle)
    public String vehicleName;// The name of the vehicle
    public String manufacturer;// The manufacturer of the vehicle
    public String model; // The model of the vehicle
    public String licencePlate;// The license plate of the vehicle
    public String year;// The manufacturing year of the vehicle
    public String fuelType;// The fuel type of the vehicle (e.g., gasoline, diesel, electric)
    public String fuelCapacity; // The fuel capacity of the vehicle
    public String chassisNumber;// The chassis number of the vehicle
    public String identificationVin;// The Vehicle Identification Number (VIN) of the vehicle
    public String vehicleNotes;// Any additional notes related to the vehicle

    // Default constructor
    public VehicleModel() {
    }

    // Getter and Setter methods for each field will go here

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(String fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getIdentificationVin() {
        return identificationVin;
    }

    public void setIdentificationVin(String identificationVin) {
        this.identificationVin = identificationVin;
    }

    public String getVehicleNotes() {
        return vehicleNotes;
    }

    public void setVehicleNotes(String vehicleNotes) {
        this.vehicleNotes = vehicleNotes;
    }

}
