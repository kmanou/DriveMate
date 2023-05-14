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

import com.myproject.myvehicleapp.Adapters.PaymentMethodAdapter;
import com.myproject.myvehicleapp.AddActivities.AddEditDeletePaymentMethodActivity;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.Models.PaymentMethodModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

public class PaymentMethodActivity extends AppCompatActivity {

    FloatingActionButton addPaymentMethodBtn;
    RecyclerView recyclerPaymentMethodView;
    PaymentMethodAdapter paymentMethodAdapter;
    Toolbar mainToolbarPaymentMethod;

    private boolean selectMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        // Initialize the toolbar
        mainToolbarPaymentMethod = findViewById(R.id.mainToolbarPaymentMethods);
        setSupportActionBar(mainToolbarPaymentMethod);
        getSupportActionBar().setTitle("Payment Method");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        // Get references to the FloatingActionButton and RecyclerView
        addPaymentMethodBtn = findViewById(R.id.add_payment_method_btn);
        recyclerPaymentMethodView = findViewById(R.id.recycler_payment_method_view);

        // Set a click listener on the addPaymentMethodBtn to open the AddEditDeletePaymentMethodActivity
        addPaymentMethodBtn.setOnClickListener((v) -> startActivity(new Intent(PaymentMethodActivity.this, AddEditDeletePaymentMethodActivity.class)));

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is signed in, set up the recycler view
            setupRecyclerView();
        } else {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(PaymentMethodActivity.this, LoginActivity.class));
            finish();
        }

        // Check if the activity is launched in select mode
        selectMode = getIntent().getBooleanExtra("selectMode", false);
    }

    // Set up the RecyclerView and Firebase query
    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForPaymentMethod().orderBy("paymentMethod", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PaymentMethodModel> options = new FirestoreRecyclerOptions.Builder<PaymentMethodModel>()
                .setQuery(query, PaymentMethodModel.class).build();
        recyclerPaymentMethodView.setLayoutManager(new LinearLayoutManager(this));

        // Create an instance of the PaymentMethodAdapter and set it as the adapter for the RecyclerView
        paymentMethodAdapter = new PaymentMethodAdapter(options, this, (paymentMethodModel, docId) -> {
            if (selectMode) {
                // Handle payment method selection in select mode
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedPaymentMethod", paymentMethodModel.paymentMethod);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                // Handle payment method item click here
                // For example, you can start a new activity or show a toast
                Toast.makeText(PaymentMethodActivity.this, "Clicked on: " + paymentMethodModel.paymentMethod, Toast.LENGTH_SHORT).show();
            }
        }, selectMode);

        recyclerPaymentMethodView.setAdapter(paymentMethodAdapter);
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
        paymentMethodAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for changes in the FirestoreRecyclerAdapter
        paymentMethodAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Notify the adapter that the data set has changed
        paymentMethodAdapter.notifyDataSetChanged();
    }
}