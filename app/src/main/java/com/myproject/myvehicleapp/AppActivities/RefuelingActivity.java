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
import com.myproject.myvehicleapp.Adapters.RefuelingAdapter;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteRefuelingActivity;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.Models.RefuelingModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

public class RefuelingActivity extends AppCompatActivity {

    FloatingActionButton addRefuelingBtn;
    RecyclerView recyclerRefuelingView;
    RefuelingAdapter refuelingAdapter;
    Toolbar mainToolbarRefueling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refueling);

        mainToolbarRefueling = findViewById(R.id.mainToolbarRefueling);

        setSupportActionBar(mainToolbarRefueling);
        getSupportActionBar().setTitle("My Refuels");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        addRefuelingBtn = findViewById(R.id.add_refueling_btn);
        recyclerRefuelingView = findViewById(R.id.refueling_recycler_view);

        addRefuelingBtn.setOnClickListener((v) -> startActivity(new Intent(RefuelingActivity.this, AddEditDeleteRefuelingActivity.class)));

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is signed in, set up the recycler view
            setupRecyclerView();
        } else {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(RefuelingActivity.this, LoginActivity.class));
            finish();
        }
    }

    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForRefueling().limit(10).orderBy("refuelingTimestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<RefuelingModel> options = new FirestoreRecyclerOptions.Builder<RefuelingModel>()
                .setQuery(query, RefuelingModel.class).build();
        recyclerRefuelingView.setLayoutManager(new LinearLayoutManager(this));
        refuelingAdapter = new RefuelingAdapter(options, this);
        recyclerRefuelingView.setAdapter(refuelingAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refuelingAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        refuelingAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refuelingAdapter.notifyDataSetChanged();
    }
}