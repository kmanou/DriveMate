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
import com.myproject.myvehicleapp.Utlities.Tools;
import com.myproject.myvehicleapp.Utlities.Utility;


public class AddEditDeletePaymentMethodActivity extends AppCompatActivity {

    TextInputEditText paymentMethodNameTI;
    String paymentMethodName, docId;
    boolean isEditMode = false;
    Toolbar toolbarPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_payment_method);

        toolbarPaymentMethod = findViewById(R.id.toolbar_payment_method);
        setSupportActionBar(toolbarPaymentMethod);

        getSupportActionBar().setTitle("Add Payment Method");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        paymentMethodNameTI = findViewById(R.id.paymentMethodNameTI);

        //receive data
        paymentMethodName = getIntent().getStringExtra("paymentMethod");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        paymentMethodNameTI.setText(paymentMethodName);
    }

    void savePaymentMethod(){
        String paymentMethodName = paymentMethodNameTI.getText().toString();

        if(paymentMethodName==null || paymentMethodName.isEmpty() ){
            paymentMethodNameTI.setError("Payment method is required");
            return;
        }
        PaymentMethodModel paymentMethodModel = new PaymentMethodModel();

        paymentMethodModel.setPaymentMethod(paymentMethodName);

        savePaymentMethodToFirebase(paymentMethodModel);

    }

    void savePaymentMethodToFirebase(PaymentMethodModel paymentMethodModel){
        DocumentReference documentReference;
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForPaymentMethod().document(docId);
        }else{
            //create new note
            documentReference = Utility.getCollectionReferenceForPaymentMethod().document();
        }



        documentReference.set(paymentMethodModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(AddEditDeletePaymentMethodActivity.this,"Payment Method added successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeletePaymentMethodActivity.this,"Failed while adding Payment Method");
                }
            }
        });

    }

    void deletePaymentMethodFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForPaymentMethod().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(AddEditDeletePaymentMethodActivity.this,"Payment Method deleted successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeletePaymentMethodActivity.this,"Failed while deleting Payment Method");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_payment_method_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.savePaymentMethodBtn);
        MenuItem deleteItem = menu.findItem(R.id.deletePaymentMethodBtn);

        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        if(isEditMode){
            getSupportActionBar().setTitle("Edit your Payment Method");
            saveItem.setVisible(true);
            deleteItem.setVisible(true);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.savePaymentMethodBtn:
                // Handle edit icon click
                savePaymentMethod();
                return true;
            case R.id.deletePaymentMethodBtn:
                // Handle delete icon click
                deletePaymentMethodFromFirebase();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}