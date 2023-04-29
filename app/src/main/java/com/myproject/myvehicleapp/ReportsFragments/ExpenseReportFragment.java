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
import com.myproject.myvehicleapp.Utlities.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class ExpenseReportFragment extends Fragment {

    private TextView mTotalExpenseCostTextView;
    private TextView mByKmExpenseCostTextView;
    private TextView mByDayExpenseCostTextView;
    private TextView mReportExpenseTitle;

    private BarChart mExpenseCostPerMonthChart;
    public static ExpenseReportFragment newInstance(){
        return new ExpenseReportFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Define the design file
        View view = inflater.inflate(R.layout.fragment_expense_report,container,false);

        // Initialize the TextView
        mReportExpenseTitle = view.findViewById(R.id.reportExpenseTitle);

        mTotalExpenseCostTextView = view.findViewById(R.id.totalExpenseCostTextView);
        mByKmExpenseCostTextView = view.findViewById(R.id.byKmExpenseCostTextView);
        mByDayExpenseCostTextView = view.findViewById(R.id.byDayExpenseCostTextView);
        mExpenseCostPerMonthChart = view.findViewById(R.id.expenseCostPerMonthChart);

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
        CollectionReference myExpenseCollectionReference = Utility.getCollectionReferenceForExpense();

        myExpenseCollectionReference
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        double totalCost = 0;
                        double maxOdometer = 0;
                        long minTimestamp = Long.MAX_VALUE;
                        long maxTimestamp = Long.MIN_VALUE;

                        int numberOfEntries = 0;

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Double cost = document.getDouble("expenseTotalCost");
                            Double odometer = document.getDouble("expenseOdometer");
                            Timestamp expenseTimestamp = document.getTimestamp("expenseTimeStamp");

                            if (cost != null) {
                                totalCost += cost;
                            }
                            if (odometer != null) {
                                maxOdometer = Math.max(maxOdometer, odometer);
                            }
                            if (expenseTimestamp != null) {
                                long millis = expenseTimestamp.toDate().getTime();
                                minTimestamp = Math.min(minTimestamp, millis);
                                maxTimestamp = Math.max(maxTimestamp, millis);
                            }

                            numberOfEntries++;
                        }

                        // Calculate and display the values
                        updateReportExpenseTitle(numberOfEntries, minTimestamp, maxTimestamp);
                        displayTotalExpenseCost(totalCost);
                        displayAvgCostPerKm(totalCost, maxOdometer);
                        displayAvgCostPerDay(totalCost, minTimestamp, maxTimestamp);

                        // Calculate and display the cost per month chart
                        Map<Integer, Float> costPerMonth = calculateCostPerMonth(task.getResult().getDocuments());
                        setupCostPerMonthChart(costPerMonth);

                    } else {
                        mTotalExpenseCostTextView.setText("Error fetching data.");
                        mByKmExpenseCostTextView.setText("Error fetching data.");
                        mByDayExpenseCostTextView.setText("Error fetching data.");
                    }
                });
    }

    // Update the report expense title with number of entries and date range
    private void updateReportExpenseTitle(int numberOfEntries, long minTimestamp, long maxTimestamp) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        String startDate = dateFormat.format(new Date(minTimestamp));
        String endDate = dateFormat.format(new Date(maxTimestamp));
        String title = numberOfEntries + " Entries (" + startDate + " - " + endDate + ")";
        mReportExpenseTitle.setText(title);
    }

    // Display the total expense cost
    private void displayTotalExpenseCost(double totalCost) {
        mTotalExpenseCostTextView.setText(String.format("€%,.2f", totalCost));
    }

    // Calculate and display the average cost per kilometer
    private void displayAvgCostPerKm(double totalCost, double maxOdometer) {
        if (maxOdometer > 0) {
            double avgCostPerKm = totalCost / maxOdometer;
            mByKmExpenseCostTextView.setText(String.format("€%,.3f", avgCostPerKm));
        } else {
            mByKmExpenseCostTextView.setText("N/A");
        }
    }

    // Calculate and display the average cost per day
    private void displayAvgCostPerDay(double totalCost, long minTimestamp, long maxTimestamp) {
        if (minTimestamp != Long.MAX_VALUE && maxTimestamp != Long.MIN_VALUE) {
            long totalDays = TimeUnit.MILLISECONDS.toDays(maxTimestamp - minTimestamp) + 1;
            double avgCostPerDay = totalCost / totalDays;
            mByDayExpenseCostTextView.setText(String.format("€%,.2f", avgCostPerDay));
        } else {
            mByDayExpenseCostTextView.setText("N/A");
        }
    }

    private Map<Integer, Float> calculateCostPerMonth(List<DocumentSnapshot> documents) {
        Map<Integer, Float> costPerMonth = new HashMap<>();

        for (DocumentSnapshot document : documents) {
            Timestamp expenseTimestamp = document.getTimestamp("expenseTimeStamp");
            Double cost = document.getDouble("expenseTotalCost");
            if (expenseTimestamp != null && cost != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(expenseTimestamp.toDate());
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
        mExpenseCostPerMonthChart.setData(barData);
        mExpenseCostPerMonthChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        mExpenseCostPerMonthChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mExpenseCostPerMonthChart.getXAxis().setGranularity(1f);
        mExpenseCostPerMonthChart.getXAxis().setGranularityEnabled(true);
        mExpenseCostPerMonthChart.getAxisRight().setEnabled(false);
        mExpenseCostPerMonthChart.getDescription().setEnabled(false);
        mExpenseCostPerMonthChart.setFitBars(true);
        mExpenseCostPerMonthChart.invalidate();
    }


}