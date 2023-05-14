package com.myproject.myvehicleapp.MoreMenuActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

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

        // Initialize the toolbar
        mainToolbarFuel = findViewById(R.id.mainToolbarFuels);
        setSupportActionBar(mainToolbarFuel);
        getSupportActionBar().setTitle("Fuel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        // Get references to the FloatingActionButton and RecyclerView
        addFuelBtn = findViewById(R.id.add_fuel_btn);
        recyclerFuelView = findViewById(R.id.recycler_fuel_view);

        // Set a click listener on the addFuelBtn to open the AddEditDeleteFuelActivity
        addFuelBtn.setOnClickListener((v) -> startActivity(new Intent(FuelActivity.this, AddEditDeleteFuelActivity.class)));

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

        // Check if the activity is launched in select mode
        selectMode = getIntent().getBooleanExtra("selectMode", false);
    }

    // Set up the RecyclerView and Firebase query
    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForFuels().orderBy("fuelName", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FuelModel> options = new FirestoreRecyclerOptions.Builder<FuelModel>()
                .setQuery(query, FuelModel.class).build();
        recyclerFuelView.setLayoutManager(new LinearLayoutManager(this));

        // Create an instance of the FuelAdapter and set it as the adapter for the RecyclerView
        fuelAdapter = new FuelAdapter(options, this, (fuelModel, docId) -> {
            if (selectMode) {
                // Handle fuel item selection in select mode
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedFuelName", fuelModel.fuelName);
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
        // Handle menu item clicks
        if (item.getItemId() == android.R.id.home) {
            // If the home button is clicked, finish the activity and return to the previous screen
            finish();
        } else {
            // If any other menu item is clicked, show a toast message with the title of the clicked item
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for changes in the FirestoreRecyclerAdapter
        fuelAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for changes in the FirestoreRecyclerAdapter
        fuelAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Notify the adapter that the data set has changed
        fuelAdapter.notifyDataSetChanged();
    }
}
