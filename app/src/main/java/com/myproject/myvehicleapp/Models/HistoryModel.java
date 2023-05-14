package com.myproject.myvehicleapp.Models;

import com.google.firebase.Timestamp;

public class HistoryModel {

    private String id; // ID of the history model
    private String recyclerTitle; // Title for the RecyclerView item
    private String collectionType; // Type of the collection (e.g., refueling, expense, service)
    private Timestamp collectionTimestamp; // Timestamp of the collection
    private RefuelingModel refuelingModel; // Refueling model associated with the history item
    private ExpenseModel expenseModel; // Expense model associated with the history item
    private ServiceModel serviceModel; // Service model associated with the history item

    public HistoryModel() {
        // Default constructor required for Firebase
    }

    // Getters and setters for the HistoryModel properties

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecyclerTitle() {
        return recyclerTitle;
    }

    public void setRecyclerTitle(String recyclerTitle) {
        this.recyclerTitle = recyclerTitle;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public Timestamp getCollectionTimestamp() {
        return collectionTimestamp;
    }

    public void setCollectionTimestamp(Timestamp collectionTimestamp) {
        this.collectionTimestamp = collectionTimestamp;
    }

    public RefuelingModel getRefuelingModel() {
        return refuelingModel;
    }

    public void setRefuelingModel(RefuelingModel refuelingModel) {
        this.refuelingModel = refuelingModel;
    }

    public ExpenseModel getExpenseModel() {
        return expenseModel;
    }

    public void setExpenseModel(ExpenseModel expenseModel) {
        this.expenseModel = expenseModel;
    }

    public ServiceModel getServiceModel() {
        return serviceModel;
    }

    public void setServiceModel(ServiceModel serviceModel) {
        this.serviceModel = serviceModel;
    }
}

