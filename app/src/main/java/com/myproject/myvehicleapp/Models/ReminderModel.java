package com.myproject.myvehicleapp.Models;

import com.google.firebase.Timestamp;

public class ReminderModel {
    public String reminderTitle;
    public String reminderDescription;
    public Timestamp reminderTimestamp;
    private boolean alarmEnabled;

    public ReminderModel() {
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public String getReminderDescription() {
        return reminderDescription;
    }

    public void setReminderDescription(String reminderDescription) {
        this.reminderDescription = reminderDescription;
    }

    public Timestamp getReminderTimestamp() {
        return reminderTimestamp;
    }

    public void setReminderTimestamp(Timestamp reminderTimestamp) {
        this.reminderTimestamp = reminderTimestamp;
    }

    public boolean isAlarmEnabled() {
        return alarmEnabled;
    }

    public void setAlarmEnabled(boolean alarmEnabled) {
        this.alarmEnabled = alarmEnabled;
    }

}
