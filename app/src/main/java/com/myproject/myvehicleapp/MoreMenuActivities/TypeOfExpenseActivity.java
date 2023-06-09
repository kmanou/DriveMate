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
import com.myproject.myvehicleapp.Adapters.TypeOfExpenseAdapter;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteTypeOfExpenseActivity;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.Models.TypeOfExpenseModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

public class TypeOfExpenseActivity extends AppCompatActivity {

    FloatingActionButton addTypeOfExpenseBtn;
    RecyclerView recyclerTypeOfExpenseView;
    TypeOfExpenseAdapter typeOfExpenseAdapter;
    Toolbar mainToolbarTypeOfExpense;

    private boolean selectMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_of_expense);

        // Initialize the toolbar
        mainToolbarTypeOfExpense = findViewById(R.id.mainToolbarTypeOfExpense);
        setSupportActionBar(mainToolbarTypeOfExpense);
        getSupportActionBar().setTitle("Type of Expense");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        // Get references to UI elements
        addTypeOfExpenseBtn = findViewById(R.id.add_type_of_expense_btn);
        recyclerTypeOfExpenseView = findViewById(R.id.recycler_type_of_expense_view);

        // Set click listener for the "Add Type of Expense" button
        addTypeOfExpenseBtn.setOnClickListener((v) -> startActivity(new Intent(TypeOfExpenseActivity.this, AddEditDeleteTypeOfExpenseActivity.class)));

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is signed in, set up the recycler view
            setupRecyclerView();
        } else {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(TypeOfExpenseActivity.this, LoginActivity.class));
            finish();
        }

        // Check if the activity is in select mode
        selectMode = getIntent().getBooleanExtra("selectMode", false);
    }

    // Set up the RecyclerView and adapter
    void setupRecyclerView() {
        // Create a Firestore query to fetch the type of expense data
        Query query = Utility.getCollectionReferenceForTypeOfExpense().orderBy("typeOfExpense", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<TypeOfExpenseModel> options = new FirestoreRecyclerOptions.Builder<TypeOfExpenseModel>()
                .setQuery(query, TypeOfExpenseModel.class).build();
        recyclerTypeOfExpenseView.setLayoutManager(new LinearLayoutManager(this));

        // Create an instance of the TypeOfExpenseAdapter and attach it to the RecyclerView
        typeOfExpenseAdapter = new TypeOfExpenseAdapter(options, this, (TypeOfExpenseModel, docId) -> {
            if (selectMode) {
                // If in select mode, return the selected type of expense to the calling activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedTypeOfExpense", TypeOfExpenseModel.typeOfExpense);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                // If not in select mode, handle the item click as desired
                Toast.makeText(TypeOfExpenseActivity.this,
                        "Clicked on: " + TypeOfExpenseModel.typeOfExpense, Toast.LENGTH_SHORT).show();
            }
        }, selectMode);

        recyclerTypeOfExpenseView.setAdapter(typeOfExpenseAdapter);
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
        // Start listening for Firestore updates
        typeOfExpenseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for Firestore updates
        typeOfExpenseAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Notify the adapter that the data set has changed
        typeOfExpenseAdapter.notifyDataSetChanged();
    }
}
