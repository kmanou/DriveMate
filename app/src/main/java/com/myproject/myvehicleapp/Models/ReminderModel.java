package com.myproject.myvehicleapp.Models;

import com.google.firebase.Timestamp;

// This class serves as a data model for a Reminder object
public class ReminderModel {

    public String reminderTitle;// The title of the reminder
    public String reminderDescription;// The description of the reminder
    public Timestamp reminderTimestamp;// The timestamp when the reminder is set to trigger
    private boolean alarmEnabled;// Boolean indicating if the alarm for the reminder is enabled or not

    // Default constructor
    public ReminderModel() {
    }

    // Getter method for the reminderTitle field
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
