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

    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    //set the notes collection in firebase
    public static CollectionReference getCollectionReferenceForNotes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_notes");
    }

    public static CollectionReference getCollectionReferenceForFuels(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_fuels");
    }

    //set the vehicles collection in firebase
    public static CollectionReference getCollectionReferenceForVehicles(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_vehicles");
    }

    public static CollectionReference getCollectionReferenceForPaymentMethod(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_payment_methods");
    }

    public static CollectionReference getCollectionReferenceForTypeOfExpense(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_type_of_expense");
    }

    public static CollectionReference getCollectionReferenceForTypeOfIncome(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_type_of_income");
    }

    public static CollectionReference getCollectionReferenceForTypeOfService() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_type_of_service");
    }

    public static CollectionReference getCollectionReferenceForRefueling(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_refueling");
    }

    public static CollectionReference getCollectionReferenceForService(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_service");
    }

    public static CollectionReference getCollectionReferenceForExpense(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_expense");
    }

    public static CollectionReference getCollectionReferenceForReminders(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_reminders");
    }
/*
    public static CollectionReference getCollectionReferenceForRefueling() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vehicles")
                .document(currentUser.getUid()).collection("my_vehicles")
                .document().collection("my_refueling");
    }
*/
/*
    public static void scheduleAlarm(Context context, String reminderId, String reminderTitle, String reminderDescription, Timestamp reminderTimestamp) {
        // Create the alarm intent
        Intent alarmIntent = new Intent(context, ReminderAlarmReceiver.class);
        alarmIntent.putExtra("reminderId", reminderId);
        alarmIntent.putExtra("reminderTitle", reminderTitle);
        alarmIntent.putExtra("reminderDescription", reminderDescription);

        // Create a PendingIntent for the alarm
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderId.hashCode(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_IMMUTABLE);

        // Schedule the alarm using AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTimestamp.toDate().getTime(), pendingIntent);
        }
    }
    */

    public static void scheduleAlarm(Context context, String docId, String reminderTitle, String reminderDescription, Timestamp reminderTimestamp) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderAlarmReceiver.class);

        intent.putExtra("docId", docId);
        intent.putExtra("reminderTitle", reminderTitle);
        intent.putExtra("reminderDescription", reminderDescription);

        int alarmId = docId.hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            long triggerAtMillis = reminderTimestamp.toDate().getTime();
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); // Add this line
            intent.putExtra("soundUri", soundUri.toString()); // Add this line

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }
    }






    public static void cancelAlarm(Context context, String reminderId) {
        // Create the alarm intent
        Intent alarmIntent = new Intent(context, ReminderAlarmReceiver.class);

        // Create a PendingIntent for the alarm
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderId.hashCode(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_IMMUTABLE);

        // Cancel the alarm using AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("dd/MM/yyyy").format(timestamp.toDate());
    }
}
