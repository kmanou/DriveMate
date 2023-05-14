package com.myproject.myvehicleapp.Utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {

    // Retrieves the collection reference for notes
    public static CollectionReference getCollectionReferenceForNotes() {
        return FirebaseFirestore.getInstance()
                .collection("vehicles")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("my_notes");
    }

    // Retrieves the collection reference for fuels
    public static CollectionReference getCollectionReferenceForFuels() {
        return FirebaseFirestore.getInstance()
                .collection("vehicles")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("my_fuels");
    }

    // Retrieves the collection reference for vehicles
    public static CollectionReference getCollectionReferenceForVehicles() {
        return FirebaseFirestore.getInstance()
                .collection("vehicles")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("my_vehicles");
    }

    // Retrieves the collection reference for payment methods
    public static CollectionReference getCollectionReferenceForPaymentMethod() {
        return FirebaseFirestore.getInstance()
                .collection("vehicles")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("my_payment_methods");
    }

    // Retrieves the collection reference for types of expense
    public static CollectionReference getCollectionReferenceForTypeOfExpense() {
        return FirebaseFirestore.getInstance()
                .collection("vehicles")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("my_type_of_expense");
    }

    // Retrieves the collection reference for types of income
    public static CollectionReference getCollectionReferenceForTypeOfIncome() {
        return FirebaseFirestore.getInstance()
                .collection("vehicles")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("my_type_of_income");
    }

    // Retrieves the collection reference for types of service
    public static CollectionReference getCollectionReferenceForTypeOfService() {
        return FirebaseFirestore.getInstance()
                .collection("vehicles")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("my_type_of_service");
    }

    // Retrieves the collection reference for refueling
    public static CollectionReference getCollectionReferenceForRefueling() {
        return FirebaseFirestore.getInstance()
                .collection("vehicles")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("my_refueling");
    }

    // Retrieves the collection reference for service
    public static CollectionReference getCollectionReferenceForService() {
        return FirebaseFirestore.getInstance()
                .collection("vehicles")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("my_service");
    }

    // Retrieves the collection reference for expenses
    public static CollectionReference getCollectionReferenceForExpense() {
        return FirebaseFirestore.getInstance()
                .collection("vehicles")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("my_expense");
    }

    // Retrieves the collection reference for reminders
    public static CollectionReference getCollectionReferenceForReminders() {
        return FirebaseFirestore.getInstance()
                .collection("vehicles")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("my_reminders");
    }

    // Displays a short-duration toast message
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Converts a timestamp to a formatted string
    public static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("dd/MM/yyyy").format(timestamp.toDate());
    }

    // Schedules an alarm for a reminder
    public static void scheduleAlarm(Context context, String docId, String reminderTitle,
                                     String reminderDescription, Timestamp reminderTimestamp) {
        // Create the alarm intent
        Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        intent.putExtra("docId", docId);
        intent.putExtra("reminderTitle", reminderTitle);
        intent.putExtra("reminderDescription", reminderDescription);
        int alarmId = docId.hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            long triggerAtMillis = reminderTimestamp.toDate().getTime();
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            intent.putExtra("soundUri", soundUri.toString());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }
    }

    // Cancels an alarm for a reminder
    public static void cancelAlarm(Context context, String reminderId) {
        Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderId.hashCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}