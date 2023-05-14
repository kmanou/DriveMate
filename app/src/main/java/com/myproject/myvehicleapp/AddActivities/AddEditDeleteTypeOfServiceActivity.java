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
import com.myproject.myvehicleapp.Models.TypeOfServiceModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

public class AddEditDeleteTypeOfServiceActivity extends AppCompatActivity {

    TextInputEditText typeOfServiceNameTI;
    String typeOfServiceName, docId;
    boolean isEditMode = false;
    Toolbar toolbarTypeOfService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_type_of_service);

        toolbarTypeOfService = findViewById(R.id.toolbar_type_of_service);
        setSupportActionBar(toolbarTypeOfService);

        getSupportActionBar().setTitle("Add Type of Service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        typeOfServiceNameTI = findViewById(R.id.typeOfServiceNameTI);

        // Receive data
        typeOfServiceName = getIntent().getStringExtra("typeOfService");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        typeOfServiceNameTI.setText(typeOfServiceName);
    }

    // Method to save the type of service
    void saveTypeOfService(){
        String typeOfServiceName = typeOfServiceNameTI.getText().toString();

        if(typeOfServiceName==null || typeOfServiceName.isEmpty() ){
            typeOfServiceNameTI.setError("Type of Service is required");
            return;
        }

        // Create a TypeOfServiceModel object and set its properties
        TypeOfServiceModel typeOfServiceModel = new TypeOfServiceModel();
        typeOfServiceModel.setTypeOfService(typeOfServiceName);

        // Save the typeOfServiceModel to Firebase
        saveTypeOfServiceToFirebase(typeOfServiceModel);
    }

    // Method to save the type of service to Firebase
    void saveTypeOfServiceToFirebase(TypeOfServiceModel typeOfServiceModel){
        DocumentReference documentReference;
        if(isEditMode){
            // Update the type of service
            documentReference = Utility.getCollectionReferenceForTypeOfService().document(docId);
        }else{
            // Create a new type of service
            documentReference = Utility.getCollectionReferenceForTypeOfService().document();
        }

        documentReference.set(typeOfServiceModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // Type of service is added
                    Utility.showToast(AddEditDeleteTypeOfServiceActivity.this,"Type of Service added successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteTypeOfServiceActivity.this,"Failed while adding Type of Service");
                }
            }
        });
    }

    // Method to delete the type of service from Firebase
    void deleteTypeOfServiceFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForTypeOfService().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // Type of service is deleted
                    Utility.showToast(AddEditDeleteTypeOfServiceActivity.this,"Type of Service deleted successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteTypeOfServiceActivity.this,"Failed while deleting Type of Service");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_type_of_service_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.saveTypeOfServiceBtn);
        MenuItem deleteItem = menu.findItem(R.id.deleteTypeOfServiceBtn);

        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        if(isEditMode){
            // If in edit mode, show the delete button and
            getSupportActionBar().setTitle("Edit your Type of Service");
            saveItem.setVisible(true);
            deleteItem.setVisible(true);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.saveTypeOfServiceBtn:
                // Handle save icon click
                saveTypeOfService();
                return true;
            case R.id.deleteTypeOfServiceBtn:
                // Handle delete icon click
                deleteTypeOfServiceFromFirebase();
                return true;
            case android.R.id.home:
                // Handle home (back) button click
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}