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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RefuelingReportFragment extends Fragment {

    private TextView mReportRefuelingTitle;
    private TextView mTotalRefuelingCostTextView;
    private TextView mByKmRefuelingCostTextView;
    private TextView mByDayRefuelingCostTextView;
    private TextView mReportTotalVolumeRefuelingTitleTextView;
    private TextView mGeneralAverageRefuelingFuelTextView;

    private BarChart mRefuelingCostPerMonthChart;
    public static RefuelingReportFragment newInstance(){
        return new RefuelingReportFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Define the design file
        View view = inflater.inflate(R.layout.fragment_refueling_report,container,false);

        // Initialize the TextView
        mReportRefuelingTitle = view.findViewById(R.id.reportRefuelingTitle);

        mTotalRefuelingCostTextView = view.findViewById(R.id.totalRefuelingCostTextView);
        mByKmRefuelingCostTextView = view.findViewById(R.id.byKmRefuelingCostTextView);
        mByDayRefuelingCostTextView = view.findViewById(R.id.byDayRefuelingCostTextView);
        mRefuelingCostPerMonthChart = view.findViewById(R.id.refuelingCostPerMonthChart);
        mReportTotalVolumeRefuelingTitleTextView = view.findViewById(R.id.totalVolumeRefuelingFuelTextView);
        mGeneralAverageRefuelingFuelTextView = view.findViewById(R.id.generalAverageRefuelingFuelTextView);

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
        CollectionReference myRefuelingCollectionReference = Utility.getCollectionReferenceForRefueling();

        myRefuelingCollectionReference
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        double totalCost = 0;
                        double maxOdometer = 0;
                        long minTimestamp = Long.MAX_VALUE;
                        long maxTimestamp = Long.MIN_VALUE;

                        int numberOfEntries = 0;
                        double totalLitres = 0;

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Double cost = document.getDouble("refuelingTotalCost");
                            Double odometer = document.getDouble("refuelingOdometer");
                            Timestamp refuelingTimestamp = document.getTimestamp("refuelingTimestamp");
                            Double litres = document.getDouble("refuelingFuelLitres");

                            if (litres != null) {
                                totalLitres += litres;
                            }

                            if (cost != null) {
                                totalCost += cost;
                            }
                            if (odometer != null) {
                                maxOdometer = Math.max(maxOdometer, odometer);
                            }
                            if (refuelingTimestamp != null) {
                                long millis = refuelingTimestamp.toDate().getTime();
                                minTimestamp = Math.min(minTimestamp, millis);
                                maxTimestamp = Math.max(maxTimestamp, millis);
                            }

                            numberOfEntries++;
                        }

                        // Calculate and display the values
                        updateReportRefuelingTitle(numberOfEntries, minTimestamp, maxTimestamp);
                        displayTotalRefuelingCost(totalCost);
                        displayAvgCostPerKm(totalCost, maxOdometer);
                        displayAvgCostPerDay(totalCost, minTimestamp, maxTimestamp);
                        displayTotalVolumeRefueling(totalLitres);
                        displayAvgLitersPer100Km(totalLitres, maxOdometer);


                        // Calculate and display the cost per month chart
                        Map<Integer, Float> costPerMonth = calculateCostPerMonth(task.getResult().getDocuments());
                        setupCostPerMonthChart(costPerMonth);

                    } else {
                        mTotalRefuelingCostTextView.setText("Error fetching data.");
                        mByKmRefuelingCostTextView.setText("Error fetching data.");
                        mByDayRefuelingCostTextView.setText("Error fetching data.");
                        mReportTotalVolumeRefuelingTitleTextView.setText("Error fetching data.");
                    }
                });
    }

    // Update the report refueling title with number of entries and date range
    private void updateReportRefuelingTitle(int numberOfEntries, long minTimestamp, long maxTimestamp) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        String startDate = dateFormat.format(new Date(minTimestamp));
        String endDate = dateFormat.format(new Date(maxTimestamp));
        String title = numberOfEntries + " Entries (" + startDate + " - " + endDate + ")";
        mReportRefuelingTitle.setText(title);
    }

    // Display the total refueling cost
    private void displayTotalRefuelingCost(double totalCost) {
        mTotalRefuelingCostTextView.setText(String.format("€%,.2f", totalCost));
    }

    private void displayTotalVolumeRefueling(double totalLitres) {
        mReportTotalVolumeRefuelingTitleTextView.setText(String.format("Total Litres: %,.2f", totalLitres));
    }

    private void displayAvgLitersPer100Km(double totalLitres, double maxOdometer) {
        if (maxOdometer > 0) {
            double avgLitersPer100Km = (totalLitres / maxOdometer) * 100;
            mGeneralAverageRefuelingFuelTextView.setText(String.format("L/100km: %,.2f", avgLitersPer100Km));
        } else {
            mGeneralAverageRefuelingFuelTextView.setText("N/A");
        }
    }

    // Calculate and display the average cost per kilometer

    private void displayAvgCostPerKm(double totalCost, double maxOdometer) {
        if (maxOdometer > 0) {
            double avgCostPerKm = totalCost / maxOdometer;
            mByKmRefuelingCostTextView.setText(String.format("€%,.3f", avgCostPerKm));
        } else {
            mByKmRefuelingCostTextView.setText("N/A");
        }
    }

    // Calculate and display the average cost per day
    private void displayAvgCostPerDay(double totalCost, long minTimestamp, long maxTimestamp) {
        if (minTimestamp != Long.MAX_VALUE && maxTimestamp != Long.MIN_VALUE) {
            long totalDays = TimeUnit.MILLISECONDS.toDays(maxTimestamp - minTimestamp) + 1;
            double avgCostPerDay = totalCost / totalDays;
            mByDayRefuelingCostTextView.setText(String.format("€%,.2f", avgCostPerDay));
        } else {
            mByDayRefuelingCostTextView.setText("N/A");
        }
    }

    // Calculate and display the cost per month
    private Map<Integer, Float> calculateCostPerMonth(List<DocumentSnapshot> documents) {
        Map<Integer, Float> costPerMonth = new HashMap<>();

        for (DocumentSnapshot document : documents) {
            Timestamp refuelingTimestamp = document.getTimestamp("refuelingTimestamp");
            Double cost = document.getDouble("refuelingTotalCost");
            if (refuelingTimestamp != null && cost != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(refuelingTimestamp.toDate());
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
        sortedEntries.sort(Comparator.comparingInt(Map.Entry::getKey));

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
        mRefuelingCostPerMonthChart.setData(barData);
        mRefuelingCostPerMonthChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        mRefuelingCostPerMonthChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mRefuelingCostPerMonthChart.getXAxis().setGranularity(1f);
        mRefuelingCostPerMonthChart.getXAxis().setGranularityEnabled(true);
        mRefuelingCostPerMonthChart.getAxisRight().setEnabled(false);
        mRefuelingCostPerMonthChart.getDescription().setEnabled(false);
        mRefuelingCostPerMonthChart.setFitBars(true);
        mRefuelingCostPerMonthChart.invalidate();
    }
}