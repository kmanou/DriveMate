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
import com.myproject.myvehicleapp.Adapters.NoteAdapter;
import com.myproject.myvehicleapp.Adapters.VehicleAdapter;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteVehicleActivity;
import com.myproject.myvehicleapp.MainActivity;
import com.myproject.myvehicleapp.Models.NoteModel;
import com.myproject.myvehicleapp.Models.VehicleModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utlities.Tools;
import com.myproject.myvehicleapp.Utlities.Utility;

public class VehicleActivity extends AppCompatActivity {

    FloatingActionButton addVehicleBtn;
    RecyclerView recyclerVehicleView;
    VehicleAdapter vehicleAdapter;
    Toolbar mainToolbarVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vehicle);

        mainToolbarVehicle = findViewById(R.id.mainToolbarVehicle);

        setSupportActionBar(mainToolbarVehicle);
        getSupportActionBar().setTitle("My Vehicles");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        addVehicleBtn = findViewById(R.id.add_vehicle_btn);
        recyclerVehicleView = findViewById(R.id.vehicle_recycler_view);

        addVehicleBtn.setOnClickListener((v)-> startActivity(new Intent(VehicleActivity.this, AddEditDeleteVehicleActivity.class)) );

        setupRecyclerView();
    }


    void setupRecyclerView(){
        Query query  = Utility.getCollectionReferenceForVehicles().orderBy("vehicle",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<VehicleModel> options = new FirestoreRecyclerOptions.Builder<VehicleModel>()
                .setQuery(query, VehicleModel.class).build();
        recyclerVehicleView.setLayoutManager(new LinearLayoutManager(this));
        vehicleAdapter = new VehicleAdapter(options,this);
        recyclerVehicleView.setAdapter(vehicleAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        vehicleAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        vehicleAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vehicleAdapter.notifyDataSetChanged();
    }
}