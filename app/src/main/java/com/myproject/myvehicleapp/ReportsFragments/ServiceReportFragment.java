package com.myproject.myvehicleapp.ReportsFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utlities.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class ServiceReportFragment extends Fragment {

    private TextView mTotalServiceCostTextView;
    private TextView mByKmServiceCostTextView;
    private TextView mByDayServiceCostTextView;
    private TextView mReportServiceTitle;

    private BarChart mServiceCostPerMonthChart;
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
        mServiceCostPerMonthChart = view.findViewById(R.id.serviceCostPerMonthChart);

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
                .addOnCompleteListener(task -> {
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

                        // Calculate and display the cost per month chart
                        Map<Integer, Float> costPerMonth = calculateCostPerMonth(task.getResult().getDocuments());
                        setupCostPerMonthChart(costPerMonth);

                    } else {
                        mTotalServiceCostTextView.setText("Error fetching data.");
                        mByKmServiceCostTextView.setText("Error fetching data.");
                        mByDayServiceCostTextView.setText("Error fetching data.");
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

    private Map<Integer, Float> calculateCostPerMonth(List<DocumentSnapshot> documents) {
        Map<Integer, Float> costPerMonth = new HashMap<>();

        for (DocumentSnapshot document : documents) {
            Timestamp serviceTimestamp = document.getTimestamp("serviceTimeStamp");
            Double cost = document.getDouble("serviceTotalCost");
            if (serviceTimestamp != null && cost != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(serviceTimestamp.toDate());
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                int monthKey = year * 100 + month;

                costPerMonth.put(monthKey, costPerMonth.getOrDefault(monthKey, 0f) + cost.floatValue());
            }
        }

        return costPerMonth;
    }

    private String getMonthLabel(int monthKey) {
        int year = monthKey / 100;
        int month = monthKey % 100;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void setupCostPerMonthChart(Map<Integer, Float> costPerMonth) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        // Sort the costPerMonth map by monthKey
        List<Map.Entry<Integer, Float>> sortedEntries = new ArrayList<>(costPerMonth.entrySet());
        sortedEntries.sort((o1, o2) -> Integer.compare(o1.getKey(), o2.getKey()));

        int index = 0;
        for (Map.Entry<Integer, Float> entry : sortedEntries) {
            entries.add(new BarEntry(index, entry.getValue()));
            labels.add(getMonthLabel(entry.getKey()));
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Cost per Month");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        mServiceCostPerMonthChart.setData(barData);
        mServiceCostPerMonthChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        mServiceCostPerMonthChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mServiceCostPerMonthChart.getXAxis().setGranularity(1f);
        mServiceCostPerMonthChart.getXAxis().setGranularityEnabled(true);
        mServiceCostPerMonthChart.getAxisRight().setEnabled(false);
        mServiceCostPerMonthChart.getDescription().setEnabled(false);
        mServiceCostPerMonthChart.setFitBars(true);
        mServiceCostPerMonthChart.invalidate();
    }


}
