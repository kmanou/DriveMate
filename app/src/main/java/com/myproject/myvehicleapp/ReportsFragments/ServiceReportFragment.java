package com.myproject.myvehicleapp.ReportsFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utlities.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ServiceReportFragment extends Fragment {

    private TextView mTotalServiceCostTextView;
    private TextView mByKmServiceCostTextView;
    private TextView mByDayServiceCostTextView;
    private TextView mReportServiceTitle;

    public static ServiceReportFragment newInstance(){
        return new ServiceReportFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Define the design file
        View view = inflater.inflate(R.layout.fragment_service_report,container,false);

        // Initialize the TextView
        mReportServiceTitle = view.findViewById(R.id.reportServiceTitle);

        mTotalServiceCostTextView = view.findViewById(R.id.totalServiceCostTextView);
        mByKmServiceCostTextView = view.findViewById(R.id.byKmServiceCostTextView);
        mByDayServiceCostTextView = view.findViewById(R.id.byDayServiceCostTextView);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        // Check if the user is logged in and fetch the total cost
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // Fetch and display the total cost
            fetchTotalCost();
        } else {
            // Redirect to the LoginActivity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    private void fetchTotalCost() {
        CollectionReference myServiceCollectionReference = Utility.getCollectionReferenceForService();

        myServiceCollectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double totalCost = 0;
                            double maxOdometer = 0;
                            long minTimestamp = Long.MAX_VALUE;
                            long maxTimestamp = Long.MIN_VALUE;

                            int numberOfEntries = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Double cost = document.getDouble("serviceTotalCost");
                                Double odometer = document.getDouble("serviceOdometer");
                                Timestamp serviceTimestamp = document.getTimestamp("serviceTimeStamp");

                                if (cost != null) {
                                    totalCost += cost;
                                }
                                if (odometer != null) {
                                    maxOdometer = Math.max(maxOdometer, odometer);
                                }
                                if (serviceTimestamp != null) {
                                    long millis = serviceTimestamp.toDate().getTime();
                                    minTimestamp = Math.min(minTimestamp, millis);
                                    maxTimestamp = Math.max(maxTimestamp, millis);
                                }

                                numberOfEntries++;
                            }

                            // Calculate and display the values
                            updateReportServiceTitle(numberOfEntries, minTimestamp, maxTimestamp);
                            displayTotalServiceCost(totalCost);
                            displayAvgCostPerKm(totalCost, maxOdometer);
                            displayAvgCostPerDay(totalCost, minTimestamp, maxTimestamp);

                        } else {
                            mTotalServiceCostTextView.setText("Error fetching data.");
                            mByKmServiceCostTextView.setText("Error fetching data.");
                            mByDayServiceCostTextView.setText("Error fetching data.");
                        }
                    }
                });
    }

    // Update the report service title with number of entries and date range
    private void updateReportServiceTitle(int numberOfEntries, long minTimestamp, long maxTimestamp) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        String startDate = dateFormat.format(new Date(minTimestamp));
        String endDate = dateFormat.format(new Date(maxTimestamp));
        String title = numberOfEntries + " Entries (" + startDate + " - " + endDate + ")";
        mReportServiceTitle.setText(title);
    }

    // Display the total service cost
    private void displayTotalServiceCost(double totalCost) {
        mTotalServiceCostTextView.setText(String.format("€%,.2f", totalCost));
    }

    // Calculate and display the average cost per kilometer
    private void displayAvgCostPerKm(double totalCost, double maxOdometer) {
        if (maxOdometer > 0) {
            double avgCostPerKm = totalCost / maxOdometer;
            mByKmServiceCostTextView.setText(String.format("€%,.3f", avgCostPerKm));
        } else {
            mByKmServiceCostTextView.setText("N/A");
        }
    }

    // Calculate and display the average cost per day
    private void displayAvgCostPerDay(double totalCost, long minTimestamp, long maxTimestamp) {
        if (minTimestamp != Long.MAX_VALUE && maxTimestamp != Long.MIN_VALUE) {
            long totalDays = TimeUnit.MILLISECONDS.toDays(maxTimestamp - minTimestamp) + 1;
            double avgCostPerDay = totalCost / totalDays;
            mByDayServiceCostTextView.setText(String.format("€%,.2f", avgCostPerDay));
        } else {
            mByDayServiceCostTextView.setText("N/A");
        }
    }
}
