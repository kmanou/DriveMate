package com.myproject.myvehicleapp.AppActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.myproject.myvehicleapp.Adapters.ServiceAdapter;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteServiceActivity;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.Models.ServiceModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

public class ServiceActivity extends AppCompatActivity {

    FloatingActionButton addServiceBtn;
    RecyclerView recyclerServiceView;
    ServiceAdapter serviceAdapter;
    Toolbar mainToolbarService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        mainToolbarService = findViewById(R.id.mainToolbarService);

        setSupportActionBar(mainToolbarService);
        getSupportActionBar().setTitle("My Services");
        Tools.setSystemBarColor(this, R.color.red_800);

        addServiceBtn = findViewById(R.id.add_service_btn);
        recyclerServiceView = findViewById(R.id.service_recycler_view);

        addServiceBtn.setOnClickListener((v) -> startActivity(new Intent(ServiceActivity.this, AddEditDeleteServiceActivity.class)));

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is signed in, set up the recycler view
            setupRecyclerView();
        } else {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(ServiceActivity.this, LoginActivity.class));
            finish();
        }
    }

    void setupRecyclerView() {
        // Retrieve the service data from Firestore and configure the RecyclerView
        Query query = Utility.getCollectionReferenceForService().limit(10).orderBy("serviceTimeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ServiceModel> options = new FirestoreRecyclerOptions.Builder<ServiceModel>()
                .setQuery(query, ServiceModel.class).build();
        recyclerServiceView.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(options, this);
        recyclerServiceView.setAdapter(serviceAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for changes in the Firestore data
        serviceAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for changes in the Firestore data
        serviceAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Notify the adapter that the data has changed (in case of updates)
        serviceAdapter.notifyDataSetChanged();
    }
}
