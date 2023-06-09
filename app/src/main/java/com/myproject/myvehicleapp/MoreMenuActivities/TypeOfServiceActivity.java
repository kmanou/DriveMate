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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.myproject.myvehicleapp.Adapters.TypeOfServiceAdapter;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteTypeOfServiceActivity;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.Models.TypeOfServiceModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

public class TypeOfServiceActivity extends AppCompatActivity {

    FloatingActionButton addTypeOfServiceBtn;
    RecyclerView recyclerTypeOfServiceView;
    TypeOfServiceAdapter typeOfServiceAdapter;
    Toolbar mainToolbarTypeOfService;

    private boolean selectMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_of_service);

        mainToolbarTypeOfService = findViewById(R.id.mainToolbarTypeOfService);

        setSupportActionBar(mainToolbarTypeOfService);
        getSupportActionBar().setTitle("Type of Service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        addTypeOfServiceBtn = findViewById(R.id.add_type_of_service_btn);
        recyclerTypeOfServiceView = findViewById(R.id.recycler_type_of_service_view);

        // Handle click event for the "Add Type of Service" button
        addTypeOfServiceBtn.setOnClickListener((v) -> startActivity(new Intent(TypeOfServiceActivity.this, AddEditDeleteTypeOfServiceActivity.class)));

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is signed in, set up the recycler view
            setupRecyclerView();
        } else {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(TypeOfServiceActivity.this, LoginActivity.class));
            finish();
        }

        // Get the select mode flag from the intent
        selectMode = getIntent().getBooleanExtra("selectMode", false);
    }

    void setupRecyclerView() {
        // Query the collection and order the results by "typeOfService" in descending order
        Query query = Utility.getCollectionReferenceForTypeOfService().orderBy("typeOfService", Query.Direction.DESCENDING);

        // Configure the options for the FirestoreRecyclerAdapter
        FirestoreRecyclerOptions<TypeOfServiceModel> options = new FirestoreRecyclerOptions.Builder<TypeOfServiceModel>()
                .setQuery(query, TypeOfServiceModel.class).build();

        recyclerTypeOfServiceView.setLayoutManager(new LinearLayoutManager(this));

        // Create the adapter for the recycler view
        typeOfServiceAdapter = new TypeOfServiceAdapter(options, this, (TypeOfServiceModel, docId) -> {
            if (selectMode) {
                // If in select mode, return the selected type of service to the calling activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedTypeOfService", TypeOfServiceModel.typeOfService);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                // Handle service type item click here
                // For example, you can start a new activity or show a toast
                Toast.makeText(TypeOfServiceActivity.this,
                        "Clicked on: " + TypeOfServiceModel.typeOfService, Toast.LENGTH_SHORT).show();
            }
        }, selectMode);

        recyclerTypeOfServiceView.setAdapter(typeOfServiceAdapter);
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
        // Start listening for changes in the FirestoreRecyclerAdapter
        typeOfServiceAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for changes in the FirestoreRecyclerAdapter
        typeOfServiceAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Notify the adapter that the data has changed (if necessary)
        typeOfServiceAdapter.notifyDataSetChanged();
    }
}