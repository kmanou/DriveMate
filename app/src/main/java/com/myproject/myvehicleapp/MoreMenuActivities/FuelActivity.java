package com.myproject.myvehicleapp.MoreMenuActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.myproject.myvehicleapp.Adapters.FuelAdapter;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteFuelActivity;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.Models.FuelModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utlities.Tools;
import com.myproject.myvehicleapp.Utlities.Utility;

public class FuelActivity extends AppCompatActivity {

    FloatingActionButton addFuelBtn;
    RecyclerView recyclerFuelView;
    FuelAdapter fuelAdapter;
    Toolbar mainToolbarFuel;

    private boolean selectMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);

        mainToolbarFuel = findViewById(R.id.mainToolbarFuels);

        setSupportActionBar(mainToolbarFuel);
        getSupportActionBar().setTitle("Fuel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        addFuelBtn = findViewById(R.id.add_fuel_btn);
        recyclerFuelView = findViewById(R.id.recycler_fuel_view);

        addFuelBtn.setOnClickListener((v)-> startActivity(new Intent(FuelActivity.this, AddEditDeleteFuelActivity.class)) );

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is signed in, set up the recycler view
            setupRecyclerView();
        } else {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(FuelActivity.this, LoginActivity.class));
            finish();
        }

        selectMode = getIntent().getBooleanExtra("selectMode", false);
    }

    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForFuels().orderBy("fuelName", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FuelModel> options = new FirestoreRecyclerOptions.Builder<FuelModel>()
                .setQuery(query, FuelModel.class).build();
        recyclerFuelView.setLayoutManager(new LinearLayoutManager(this));

        fuelAdapter = new FuelAdapter(options, this, (fuelModel, docId) -> {
            if (selectMode) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedFuelName", fuelModel.fuelName);
                //resultIntent.putExtra("selectedFuelType", fuelModel.fuelType);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                // Handle fuel item click here
                // For example, you can start a new activity or show a toast
                Toast.makeText(FuelActivity.this, "Clicked on: " + fuelModel.fuelName, Toast.LENGTH_SHORT).show();
            }
        }, selectMode);
        recyclerFuelView.setAdapter(fuelAdapter);
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
        fuelAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fuelAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fuelAdapter.notifyDataSetChanged();
    }
}