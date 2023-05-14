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
import com.myproject.myvehicleapp.Adapters.ExpenseAdapter;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteExpenseActivity;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.Models.ExpenseModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

public class ExpenseActivity extends AppCompatActivity {

    FloatingActionButton addExpenseBtn;
    RecyclerView recyclerExpenseView;
    ExpenseAdapter expenseAdapter;
    Toolbar mainToolbarExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        mainToolbarExpense = findViewById(R.id.mainToolbarExpense);
        setSupportActionBar(mainToolbarExpense);
        getSupportActionBar().setTitle("My Expenses");
        Tools.setSystemBarColor(this, R.color.red_800);

        addExpenseBtn = findViewById(R.id.add_expense_btn);
        recyclerExpenseView = findViewById(R.id.expense_recycler_view);

        // Set a click listener for the "Add Expense" button to open the AddEditDeleteExpenseActivity
        addExpenseBtn.setOnClickListener((v) -> startActivity(new Intent(ExpenseActivity.this, AddEditDeleteExpenseActivity.class)));

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is signed in, set up the recycler view
            setupRecyclerView();
        } else {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(ExpenseActivity.this, LoginActivity.class));
            finish();
        }
    }

    // Method to set up the recycler view with expense data
    void setupRecyclerView() {
        // Construct a query to retrieve expense data from the Firestore database
        Query query = Utility.getCollectionReferenceForExpense().limit(10).orderBy("expenseTimeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ExpenseModel> options = new FirestoreRecyclerOptions.Builder<ExpenseModel>()
                .setQuery(query, ExpenseModel.class).build();

        // Set the layout manager and adapter for the recycler view
        recyclerExpenseView.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(options, this);
        recyclerExpenseView.setAdapter(expenseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for changes in the Firestore data and update the adapter accordingly
        expenseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for changes in the Firestore data
        expenseAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Notify the adapter to update the view in case of any changes
        expenseAdapter.notifyDataSetChanged();
    }
}