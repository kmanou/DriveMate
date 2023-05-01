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

        mainToolbarPaymentMethod = findViewById(R.id.mainToolbarPaymentMethods);
        setSupportActionBar(mainToolbarPaymentMethod);
        getSupportActionBar().setTitle("Payment Method");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        addPaymentMethodBtn = findViewById(R.id.add_payment_method_btn);
        recyclerPaymentMethodView = findViewById(R.id.recycler_payment_method_view);

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

        selectMode = getIntent().getBooleanExtra("selectMode", false);
    }

    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForPaymentMethod().orderBy("paymentMethod", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PaymentMethodModel> options = new FirestoreRecyclerOptions.Builder<PaymentMethodModel>()
                .setQuery(query, PaymentMethodModel.class).build();
        recyclerPaymentMethodView.setLayoutManager(new LinearLayoutManager(this));

        paymentMethodAdapter = new PaymentMethodAdapter(options, this, (PaymentMethodModel, docId) -> {
            if (selectMode) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedPaymentMethod", PaymentMethodModel.paymentMethod);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                // Handle fuel item click here
                // For example, you can start a new activity or show a toast
                Toast.makeText(PaymentMethodActivity.this,
                        "Clicked on: " + PaymentMethodModel.paymentMethod, Toast.LENGTH_SHORT).show();
            }
        }, selectMode);

        recyclerPaymentMethodView.setAdapter(paymentMethodAdapter);
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
        paymentMethodAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        paymentMethodAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        paymentMethodAdapter.notifyDataSetChanged();
    }
}