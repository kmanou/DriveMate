package com.myproject.myvehicleapp.Models;

import com.google.firebase.Timestamp;

public class ExpenseModel {

    public String recyclerTitle;
    public Timestamp expenseTimeStamp;
    public Integer expenseOdometer;
    public String expenseTypeOfExpense;
    public String expensePaymentMethod;
    public Float expenseTotalCost;
    public String expenseNote;

    public ExpenseModel() {
    }

    public String getRecyclerTitle() {
        return recyclerTitle;
    }

    public void setRecyclerTitle(String recyclerTitle) {
        this.recyclerTitle = recyclerTitle;
    }

    public Timestamp getExpenseTimeStamp() {
        return expenseTimeStamp;
    }

    public void setExpenseTimeStamp(Timestamp expenseTimeStamp) {
        this.expenseTimeStamp = expenseTimeStamp;
    }

    public Integer getExpenseOdometer() {
        return expenseOdometer;
    }

    public void setExpenseOdometer(Integer expenseOdometer) {
        this.expenseOdometer = expenseOdometer;
    }

    public String getExpenseTypeOfExpense() {
        return expenseTypeOfExpense;
    }

    public void setExpenseTypeOfExpense(String expenseTypeOfExpense) {
        this.expenseTypeOfExpense = expenseTypeOfExpense;
    }

    public String getExpensePaymentMethod() {
        return expensePaymentMethod;
    }

    public void setExpensePaymentMethod(String expensePaymentMethod) {
        this.expensePaymentMethod = expensePaymentMethod;
    }

    public Float getExpenseTotalCost() {
        return expenseTotalCost;
    }

    public void setExpenseTotalCost(Float expenseTotalCost) {
        this.expenseTotalCost = expenseTotalCost;
    }

    public String getExpenseNote() {
        return expenseNote;
    }

    public void setExpenseNote(String expenseNote) {
        this.expenseNote = expenseNote;
    }
}