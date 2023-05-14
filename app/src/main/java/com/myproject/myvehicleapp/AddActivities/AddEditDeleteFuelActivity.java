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
import com.myproject.myvehicleapp.Models.FuelModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

// This class handles the activity for adding, editing, and deleting a Fuel Type in the app
public class AddEditDeleteFuelActivity extends AppCompatActivity {

    // Define the UI elements
    TextInputEditText fuel;
    TextInputEditText type ;
    String fuelName;
    String fuelType;
    String docId;

    // Flag to determine if the activity is in edit mode
    boolean isEditMode = false;

    Toolbar toolbarFuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_fuel);

        // Initialize the toolbar and set it as the action bar for the activity
        toolbarFuel = findViewById(R.id.toolbar_fuel);
        setSupportActionBar(toolbarFuel);

        // Set the title of the action bar
        getSupportActionBar().setTitle("Add Fuel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        // Find the UI elements by their IDs
        fuel = findViewById(R.id.fuelNameTI);
        type = findViewById(R.id.fuelFuelTypeTI);

        //receive data
        fuelName = getIntent().getStringExtra("fuelName");
        fuelType = getIntent().getStringExtra("fuelType");
        docId = getIntent().getStringExtra("docId");

        // If a document ID was passed to the activity, then it is in edit mode
        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        // Set the UI elements to display the retrieved data
        fuel.setText(fuelName);
        type.setText(fuelType);
    }

    // Method to validate and save the fuel data
    void saveFuel(){
        // Get the user input from the UI elements
        String fuelName = fuel.getText().toString();
        String fuelType = type.getText().toString();

        // Validate the user input
        if(fuelName==null || fuelName.isEmpty() ){
            fuel.setError("Title is required");
            return;
        }

        // Create a new FuelModel object and set its properties
        FuelModel fuel = new FuelModel();
        fuel.setFuelName(fuelName);
        fuel.setFuelType(fuelType);

        // Save the FuelModel object to Firebase
        saveFuelToFirebase(fuel);
    }

    // Method to save a FuelModel object to Firebase
    void saveFuelToFirebase(FuelModel fuelModel){
        DocumentReference documentReference;
        // If the activity is in edit mode, update the existing document
        // Otherwise, create a new document
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForFuels().document(docId);
        }else{
            //create new note
            documentReference = Utility.getCollectionReferenceForFuels().document();
        }

        // Save the FuelModel object to Firebase
        documentReference.set(fuelModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(AddEditDeleteFuelActivity.this,"Fuel added successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteFuelActivity.this,"Failed while adding fuel");
                }
            }
        });

    }

    // Method to delete a fuel document from Firebase
    void deleteFuelFromFirebase(){
        // Get a reference to the document to delete
        DocumentReference documentReference = Utility.getCollectionReferenceForFuels().document(docId);

        // Delete the document from Firebase
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(AddEditDeleteFuelActivity.this,"Fuel deleted successfully");
                    finish();
                }else{
                    // Show a toast message if the deletion failed
                    Utility.showToast(AddEditDeleteFuelActivity.this,"Failed while deleting fuel");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.save_fuel_menu, menu);

        // Get references to the menu items
        MenuItem saveItem = menu.findItem(R.id.saveFuelsBtn);
        MenuItem deleteItem = menu.findItem(R.id.deleteFuelsBtn);

        // By default, only the save item is visible
        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        // If the activity is in edit mode, both the save and delete items are visible
        if(isEditMode){
            getSupportActionBar().setTitle("Edit your Fuel");
            saveItem.setVisible(true);
            deleteItem.setVisible(true);
        }

        return true;
    }

    // This method handles action bar item clicks
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.saveFuelsBtn:
                // Handle save button click
                saveFuel();
                return true;
            case R.id.deleteFuelsBtn:
                // Handle delete button click
                deleteFuelFromFirebase();
                return true;
            case android.R.id.home:
                // Handle home (back) button click
                finish();
                return true;
            default:
                // Handle all other action bar item clicks in the default manner
                return super.onOptionsItemSelected(item);
        }
    }
}