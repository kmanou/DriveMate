package com.myproject.myvehicleapp.AddActivities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.myproject.myvehicleapp.Models.PaymentMethodModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;


// This is an Activity class to add, edit or delete payment methods
public class AddEditDeletePaymentMethodActivity extends AppCompatActivity {

    // Declare UI elements and variables
    TextInputEditText paymentMethodNameTI;
    String paymentMethodName, docId;
    boolean isEditMode = false;
    Toolbar toolbarPaymentMethod;

    // The onCreate method is called when the activity is being created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_payment_method);

        // Initialize toolbar and set it as the app bar for the activity
        toolbarPaymentMethod = findViewById(R.id.toolbar_payment_method);
        setSupportActionBar(toolbarPaymentMethod);

        // Set toolbar title and enable the up button
        getSupportActionBar().setTitle("Add Payment Method");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        // Initialize payment method name input field
        paymentMethodNameTI = findViewById(R.id.paymentMethodNameTI);

        // Receive data from the previous activity
        paymentMethodName = getIntent().getStringExtra("paymentMethod");
        docId = getIntent().getStringExtra("docId");

        // Check if it is edit mode
        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        // Set payment method name to the input field
        paymentMethodNameTI.setText(paymentMethodName);
    }

    // Method to save payment method
    void savePaymentMethod(){
        String paymentMethodName = paymentMethodNameTI.getText().toString();

        // Check if payment method name is provided
        if(paymentMethodName==null || paymentMethodName.isEmpty() ){
            paymentMethodNameTI.setError("Payment method is required");
            return;
        }

        // Create a new payment method model
        PaymentMethodModel paymentMethodModel = new PaymentMethodModel();
        paymentMethodModel.setPaymentMethod(paymentMethodName);

        // Save payment method to Firebase
        savePaymentMethodToFirebase(paymentMethodModel);
    }

    // Method to save payment method to Firebase
    void savePaymentMethodToFirebase(PaymentMethodModel paymentMethodModel){
        DocumentReference documentReference;

        // Check if it is edit mode
        if(isEditMode){
            // If it is edit mode, update the existing payment method
            documentReference = Utility.getCollectionReferenceForPaymentMethod().document(docId);
        }else{
            // If it is not edit mode, create a new payment method
            documentReference = Utility.getCollectionReferenceForPaymentMethod().document();
        }

        // Add or update payment method in Firebase
        documentReference.set(paymentMethodModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // If the task is successful, show a success message and finish the activity
                    Utility.showToast(AddEditDeletePaymentMethodActivity.this,"Payment Method added successfully");
                    finish();
                }else{
                    // If the task is not successful, show an error message
                    Utility.showToast(AddEditDeletePaymentMethodActivity.this,"Failed while adding Payment Method");
                }
            }
        });
    }

    // Method to delete payment method from Firebase
    void deletePaymentMethodFromFirebase(){
        DocumentReference documentReference;

        // Get the document reference for the payment method to be deleted
        documentReference = Utility.getCollectionReferenceForPaymentMethod().document(docId);

        // Delete the payment method from Firebase
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // If the task is successful, show a success message and finish the activity
                    Utility.showToast(AddEditDeletePaymentMethodActivity.this,"Payment Method deleted successfully");
                    finish();
                }else{
                    // If the task is not successful, show an error message
                    Utility.showToast(AddEditDeletePaymentMethodActivity.this,"Failed while deleting Payment Method");
                }
            }
        });
    }

    // This method initializes the contents of the Activity's standard options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.save_payment_method_menu, menu);

        // Find save and delete menu items
        MenuItem saveItem = menu.findItem(R.id.savePaymentMethodBtn);
        MenuItem deleteItem = menu.findItem(R.id.deletePaymentMethodBtn);

        // By default, only show the save item
        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        // In edit mode, both save and delete items should be visible
        if(isEditMode){
            getSupportActionBar().setTitle("Edit your Payment Method");
            saveItem.setVisible(true);
            deleteItem.setVisible(true);
        }

        return true;
    }

    // This method is called whenever an item in your options menu is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();

        switch (id) {
            case R.id.savePaymentMethodBtn:
                // Handle save button click
                savePaymentMethod();
                return true;
            case R.id.deletePaymentMethodBtn:
                // Handle delete button click
                deletePaymentMethodFromFirebase();
                return true;
            case android.R.id.home:
                // Handle up button click
                finish();
                return true;
            default:
                // Let the system handle all other item clicks
                return super.onOptionsItemSelected(item);
        }
    }
}

