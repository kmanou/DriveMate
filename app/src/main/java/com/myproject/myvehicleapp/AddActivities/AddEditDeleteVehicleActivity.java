package com.myproject.myvehicleapp.AddActivities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.myproject.myvehicleapp.Models.VehicleModel;
import com.myproject.myvehicleapp.MoreMenuActivities.FuelActivity;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

public class AddEditDeleteVehicleActivity extends AppCompatActivity {

    // Define all the UI elements
    TextInputEditText vehicleTI;
    TextInputEditText vehicleNameTI;
    TextInputEditText manufacturerTI;
    TextInputEditText modelTI;
    TextInputEditText licencePlateTI;
    TextInputEditText yearTI;
    TextInputEditText fuelTypeTI;
    TextInputEditText fuelCapacityTI;
    TextInputEditText chassisNumberTI;
    TextInputEditText identificationVinTI;
    TextInputEditText vehicleNotesTI;

    // Define all the data elements
    String vehicle;
    String vehicleName;
    String manufacturer;
    String model;
    String licencePlate;
    String year;
    String fuelType;
    String fuelCapacity;
    String chassisNumber;
    String identificationVin;
    String vehicleNotes;
    String docId;

    boolean isEditMode = false;
    Toolbar toolbarVehicle;

    // Define the ActivityResultLaunchers for opening the FuelActivity
    private ActivityResultLauncher<Intent> fuelActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_vehicle);


        toolbarVehicle = findViewById(R.id.toolbar_vehicle);
        setSupportActionBar(toolbarVehicle);

        getSupportActionBar().setTitle("Add Vehicle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        // Find all the TextInputEditText views
        vehicleTI = findViewById(R.id.vehicleTI);
        vehicleNameTI = findViewById(R.id.vehicleNameTI);
        manufacturerTI = findViewById(R.id.manufacturerTI);
        modelTI = findViewById(R.id.modelTI);
        licencePlateTI = findViewById(R.id.licencePlateTI);
        yearTI = findViewById(R.id.yearTI);
        fuelTypeTI = findViewById(R.id.fuelTypeTI);
        fuelCapacityTI = findViewById(R.id.fuelCapacityTI);
        chassisNumberTI = findViewById(R.id.chassisNumberTI);
        identificationVinTI = findViewById(R.id.identificationVinTI);
        vehicleNotesTI = findViewById(R.id.vehicleNotesTI);

        //receive data
        vehicle = getIntent().getStringExtra("vehicle");
        vehicleName = getIntent().getStringExtra("vehicleName");
        manufacturer = getIntent().getStringExtra("manufacturer");
        model = getIntent().getStringExtra("vehicleModel");
        licencePlate = getIntent().getStringExtra("licencePlate");
        year = getIntent().getStringExtra("vehicleYear");
        fuelType = getIntent().getStringExtra("fuelType");
        fuelCapacity = getIntent().getStringExtra("fuelCapacity");
        chassisNumber = getIntent().getStringExtra("chassisNumber");
        identificationVin = getIntent().getStringExtra("identificationVin");
        vehicleNotes = getIntent().getStringExtra("vehicleNote");
        docId = getIntent().getStringExtra("docId");

        // Check if the activity is in edit mode
        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        // Set the retrieved data to the corresponding TextInputEditText views
        vehicleTI.setText(vehicle);
        vehicleNameTI.setText(vehicleName);
        manufacturerTI.setText(manufacturer);
        modelTI.setText(model);
        licencePlateTI.setText(licencePlate);
        yearTI.setText(year);
        fuelTypeTI.setText(fuelType);
        fuelCapacityTI.setText(fuelCapacity);
        chassisNumberTI.setText(chassisNumber);
        identificationVinTI.setText(identificationVin);
        vehicleNotesTI.setText(vehicleNotes);

        // Register activity result launcher for the fuel type activity
        fuelActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String selectedFuelType = result.getData().getStringExtra("selectedFuelName");
                        fuelTypeTI.setText(selectedFuelType);
                    }
                });


        //Attach openFuelTypeActivity to the corresponding UI elements
        fuelTypeTI.setOnClickListener(v -> openFuelTypeActivity());
    }

    private void openFuelTypeActivity() {

        // Create an intent to open the FuelActivity
        Intent intent = new Intent(this, FuelActivity.class);
        intent.putExtra("selectMode", true);

        // Launch the activity and receive the result using the fuelActivityResultLauncher
        fuelActivityResultLauncher.launch(intent);
    }

    void saveVehicle(){

        // Retrieve the entered values from the TextInputEditText views
        String vehicleVehicle = vehicleTI.getText().toString();
        String vehicleNameVehicle = vehicleNameTI.getText().toString();
        String vehicleManufacturer = manufacturerTI.getText().toString();
        String vehicleModel = modelTI.getText().toString();
        String vehicleLicencePlate = licencePlateTI.getText().toString();
        String vehicleYear = yearTI.getText().toString();
        String vehicleFuelType = fuelTypeTI.getText().toString();
        String vehicleFuelCapacity = fuelCapacityTI.getText().toString();
        String vehicleChassisNumber = chassisNumberTI.getText().toString();
        String vehicleIdentificationVin = identificationVinTI.getText().toString();
        String vehicleNotes = vehicleNotesTI.getText().toString();


        // Validate the required fields
        if(vehicleVehicle==null || vehicleVehicle.isEmpty() ){
            vehicleTI.setError("Vehicle is required");
            return;
        }

        // Create VehicleModel object and set its properties
        VehicleModel vehicle = new VehicleModel();

        vehicle.setVehicle(vehicleVehicle);
        vehicle.setVehicleName(vehicleNameVehicle);
        vehicle.setManufacturer(vehicleManufacturer);
        vehicle.setModel(vehicleModel);
        vehicle.setLicencePlate(vehicleLicencePlate);
        vehicle.setYear(vehicleYear);
        vehicle.setFuelType(vehicleFuelType);
        vehicle.setFuelCapacity(vehicleFuelCapacity);
        vehicle.setChassisNumber(vehicleChassisNumber);
        vehicle.setIdentificationVin(vehicleIdentificationVin);
        vehicle.setVehicleNotes(vehicleNotes);

        // Save VehicleModel to Firebase
        saveVehicleToFirebase(vehicle);

    }
    // Method to save vehicle object to Firebase
    void saveVehicleToFirebase(VehicleModel vehicleModel){
        DocumentReference documentReference;
        if(isEditMode){
            //update the vehicle
            documentReference = Utility.getCollectionReferenceForVehicles().document(docId);
        }else{
            //create new vehicle
            documentReference = Utility.getCollectionReferenceForVehicles().document();
        }

        // Set the document to the vehicle object
        documentReference.set(vehicleModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //vehicle is added
                    Utility.showToast(AddEditDeleteVehicleActivity.this,"Vehicle added successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteVehicleActivity.this,"Failed while adding Vehicle");
                }
            }
        });

    }

    // Method to delete a refueling from Firebase
    void deleteVehicleFromFirebase(){

        // Get a reference to the document to be deleted
        DocumentReference documentReference = Utility.getCollectionReferenceForVehicles().document(docId);

        // Delete the document
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(AddEditDeleteVehicleActivity.this,"Vehicle deleted successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteVehicleActivity.this,"Failed while deleting Vehicle");
                }
            }
        });
    }

    // Override onCreateOptionsMenu to inflate custom menu and set visibility of save and delete buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu and handle visibility of menu items based on edit mode
        getMenuInflater().inflate(R.menu.save_vehicle_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.saveVehicleBtn);
        MenuItem deleteItem = menu.findItem(R.id.deleteVehicleBtn);

        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        // Set visibility of save and delete buttons based on the mode
        if(isEditMode){
            getSupportActionBar().setTitle("Edit your Vehicle");
            saveItem.setVisible(true);
            deleteItem.setVisible(true);
        }

        return true;
    }

    // Override onOptionsItemSelected to handle button click events
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.saveVehicleBtn:
                // Handle edit icon click
                saveVehicle();
                return true;
            case R.id.deleteVehicleBtn:
                // Handle delete icon click
                deleteVehicleFromFirebase();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}