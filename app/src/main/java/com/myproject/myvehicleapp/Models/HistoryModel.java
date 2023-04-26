package com.myproject.myvehicleapp.Models;

import com.google.firebase.Timestamp;

public class HistoryModel {

    private String id;
    private String recyclerTitle;
    private String collectionType;
    private Timestamp collectionTimestamp;
    private RefuelingModel refuelingModel;
    private ExpenseModel expenseModel;
    private ServiceModel serviceModel;


    public HistoryModel() {
    }

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
