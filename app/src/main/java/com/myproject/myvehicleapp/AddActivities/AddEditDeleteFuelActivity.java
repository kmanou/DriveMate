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


public class AddEditDeleteFuelActivity extends AppCompatActivity {

    TextInputEditText fuel, type;
    String fuelName, fuelType, docId;
    boolean isEditMode = false;
    Toolbar toolbarFuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_fuel);

        toolbarFuel = findViewById(R.id.toolbar_fuel);
        setSupportActionBar(toolbarFuel);

        getSupportActionBar().setTitle("Add Fuel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        fuel = findViewById(R.id.fuelNameTI);
        type = findViewById(R.id.fuelFuelTypeTI);

        //receive data
        fuelName = getIntent().getStringExtra("fuelName");
        fuelType = getIntent().getStringExtra("fuelType");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        fuel.setText(fuelName);
        type.setText(fuelType);

    }

    void saveFuel(){
        String fuelName = fuel.getText().toString();
        String fuelType = type.getText().toString();

        if(fuelName==null || fuelName.isEmpty() ){
            fuel.setError("Title is required");
            return;
        }
        FuelModel fuel = new FuelModel();

        fuel.setFuelName(fuelName);
        fuel.setFuelType(fuelType);

        saveFuelToFirebase(fuel);

    }

    void saveFuelToFirebase(FuelModel fuelModel){
        DocumentReference documentReference;
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForFuels().document(docId);
        }else{
            //create new note
            documentReference = Utility.getCollectionReferenceForFuels().document();
        }



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

    void deleteFuelFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForFuels().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(AddEditDeleteFuelActivity.this,"Fuel deleted successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteFuelActivity.this,"Failed while deleting fuel");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_fuel_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.saveFuelsBtn);
        MenuItem deleteItem = menu.findItem(R.id.deleteFuelsBtn);

        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        if(isEditMode){
            getSupportActionBar().setTitle("Edit your Fuel");
            saveItem.setVisible(true);
            deleteItem.setVisible(true);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.saveFuelsBtn:
                // Handle edit icon click
                saveFuel();
                return true;
            case R.id.deleteFuelsBtn:
                // Handle delete icon click
                deleteFuelFromFirebase();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}