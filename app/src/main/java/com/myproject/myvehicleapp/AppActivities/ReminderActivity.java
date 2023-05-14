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
import com.myproject.myvehicleapp.Adapters.ReminderAdapter;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteReminderActivity;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.Models.ReminderModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

import java.util.List;

public class ReminderActivity extends AppCompatActivity {

    private FloatingActionButton addReminderBtn;
    private RecyclerView recyclerView;
    private ReminderAdapter reminderAdapter;
    private Toolbar mainToolbarReminder;
    private List<ReminderModel> reminderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        mainToolbarReminder = findViewById(R.id.mainToolbarReminderACT);
        setSupportActionBar(mainToolbarReminder);
        getSupportActionBar().setTitle("My Reminders");
        Tools.setSystemBarColor(this, R.color.red_800);

        addReminderBtn = findViewById(R.id.add_reminder_btn);
        recyclerView = findViewById(R.id.reminder_recycler_view_act);

        // Set a click listener for the "Add Reminder" button to open the AddEditDeleteReminderActivity
        addReminderBtn.setOnClickListener((v) -> startActivity(new Intent(ReminderActivity.this, AddEditDeleteReminderActivity.class)));

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is signed in, set up the recycler view
            setupRecyclerView();
        } else {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(ReminderActivity.this, LoginActivity.class));
            finish();
        }
    }

    // Method to set up the RecyclerView with reminder data
    void setupRecyclerView() {
        // Construct a query to retrieve reminder data from the Firestore database
        Query query = Utility.getCollectionReferenceForReminders().orderBy("reminderTimestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ReminderModel> options = new FirestoreRecyclerOptions.Builder<ReminderModel>()
                .setQuery(query, ReminderModel.class).build();

        // Set the layout manager and adapter for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderAdapter = new ReminderAdapter(options, this);
        recyclerView.setAdapter(reminderAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for changes in the Firestore data and update the adapter accordingly
        reminderAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for changes in the Firestore data
        reminderAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Notify the adapter to update the view in case of any changes
        reminderAdapter.notifyDataSetChanged();
    }
}
