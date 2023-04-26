package com.myproject.myvehicleapp.Utlities;

import android.content.Context;
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
    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("dd/MM/yyyy").format(timestamp.toDate());
    }
}
