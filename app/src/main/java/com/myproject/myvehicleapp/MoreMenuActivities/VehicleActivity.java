package com.myproject.myvehicleapp.MoreMenuActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.myproject.myvehicleapp.Adapters.VehicleAdapter;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteVehicleActivity;
import com.myproject.myvehicleapp.Models.VehicleModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

public class VehicleActivity extends AppCompatActivity {

    FloatingActionButton addVehicleBtn;
    RecyclerView recyclerVehicleView;
    VehicleAdapter vehicleAdapter;
    Toolbar mainToolbarVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the activity
        setContentView(R.layout.activity_vehicle);

        // Initialize the toolbar
        mainToolbarVehicle = findViewById(R.id.mainToolbarVehicle);
        setSupportActionBar(mainToolbarVehicle);
        getSupportActionBar().setTitle("My Vehicles");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        // Initialize the FloatingActionButton and RecyclerView
        addVehicleBtn = findViewById(R.id.add_vehicle_btn);
        recyclerVehicleView = findViewById(R.id.vehicle_recycler_view);

        // Set a click listener for the FloatingActionButton to add a new vehicle
        addVehicleBtn.setOnClickListener((v) -> startActivity(new Intent(VehicleActivity.this, AddEditDeleteVehicleActivity.class)));

        // Set up the RecyclerView to display the list of vehicles
        setupRecyclerView();
    }

    // Set up the RecyclerView and its adapter
    void setupRecyclerView() {
        // Query the vehicles collection and order the results by the "vehicle" field in descending order
        Query query = Utility.getCollectionReferenceForVehicles().orderBy("vehicle", Query.Direction.DESCENDING);

        // Configure the options for the FirestoreRecyclerAdapter
        FirestoreRecyclerOptions<VehicleModel> options = new FirestoreRecyclerOptions.Builder<VehicleModel>()
                .setQuery(query, VehicleModel.class).build();

        // Set the layout manager and adapter for the RecyclerView
        recyclerVehicleView.setLayoutManager(new LinearLayoutManager(this));
        vehicleAdapter = new VehicleAdapter(options, this);
        recyclerVehicleView.setAdapter(vehicleAdapter);
    }

    // Handle menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    // Start listening for Firestore changes when the activity starts
    @Override
    protected void onStart() {
        super.onStart();
        vehicleAdapter.startListening();
    }

    // Stop listening for Firestore changes when the activity stops
    @Override
    protected void onStop() {
        super.onStop();
        vehicleAdapter.stopListening();
    }

    // Notify the adapter that the data has changed (if necessary) when the activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        vehicleAdapter.notifyDataSetChanged();
    }
}