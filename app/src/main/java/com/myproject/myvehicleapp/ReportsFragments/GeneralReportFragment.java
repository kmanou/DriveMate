package com.myproject.myvehicleapp.ReportsFragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class GeneralReportFragment extends Fragment {
    private TextView mReportGeneralTitle;
    private TextView mTotalGeneralCostTextView;
    private TextView mByKmGeneralCostTextView;
    private TextView mByDayGeneralCostTextView;
    private TextView mTotalGeneralDistance;
    private TextView mDailyAverageGeneralDistance;
    private PieChart mGeneralCostPerMonthChart;

    public static GeneralReportFragment newInstance() {
        return new GeneralReportFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_general_report, container, false);

        // Initialize the TextViews and PieChart
        mReportGeneralTitle = view.findViewById(R.id.reportGeneralTitle);
        mTotalGeneralCostTextView = view.findViewById(R.id.totalGeneralCostTextView);
        mByKmGeneralCostTextView = view.findViewById(R.id.byKmGeneralCostTextView);
        mByDayGeneralCostTextView = view.findViewById(R.id.byDayGeneralCostTextView);
        mTotalGeneralDistance = view.findViewById(R.id.totalGeneralDistance);
        mDailyAverageGeneralDistance = view.findViewById(R.id.dailyAverageGeneralDistance);
        mGeneralCostPerMonthChart = view.findViewById(R.id.generalCostPerMonthChart);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Check if the user is logged in and fetch the total cost and cost per day
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // Fetch and display the total cost and cost per day from all collections
            fetchTotalCostAndCostPerDayFromAllCollections();

            // Fetch and display the total cost per collection
            fetchTotalCostPerCollection();
        } else {
            // Redirect to the LoginActivity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    // Fetch the total cost per collections
    private void fetchTotalCostPerCollection() {
        CollectionReference refuelingCollectionReference = Utility.getCollectionReferenceForRefueling();
        CollectionReference serviceCollectionReference = Utility.getCollectionReferenceForService();
        CollectionReference expenseCollectionReference = Utility.getCollectionReferenceForExpense();

        Task<QuerySnapshot> refuelingTask = refuelingCollectionReference.get();
        Task<QuerySnapshot> serviceTask = serviceCollectionReference.get();
        Task<QuerySnapshot> expenseTask = expenseCollectionReference.get();

        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(refuelingTask, serviceTask, expenseTask);

        allTasks.addOnSuccessListener(querySnapshots -> {
            double refuelingTotalCost = 0;
            double serviceTotalCost = 0;
            double expenseTotalCost = 0;

            // Process refueling costs
            QuerySnapshot refuelingSnapshot = querySnapshots.get(0);
            for (QueryDocumentSnapshot document : refuelingSnapshot) {
                Double cost = document.getDouble("refuelingTotalCost");
                if (cost != null) {
                    refuelingTotalCost += cost;
                }
            }

            // Process service costs
            QuerySnapshot serviceSnapshot = querySnapshots.get(1);
            for (QueryDocumentSnapshot document : serviceSnapshot) {
                Double cost = document.getDouble("serviceTotalCost");
                if (cost != null) {
                    serviceTotalCost += cost;
                }
            }

            // Process expense costs
            QuerySnapshot expenseSnapshot = querySnapshots.get(2);
            for (QueryDocumentSnapshot document : expenseSnapshot) {
                Double cost = document.getDouble("expenseTotalCost");
                if (cost != null) {
                    expenseTotalCost += cost;
                }
            }

            // Create pie chart entries
            List<PieEntry> entries = new ArrayList<>();
            if (refuelingTotalCost > 0) {
                entries.add(new PieEntry((float) refuelingTotalCost, "Refueling"));
            }
            if (serviceTotalCost > 0) {
                entries.add(new PieEntry((float) serviceTotalCost, "Service"));
            }
            if (expenseTotalCost > 0) {
                entries.add(new PieEntry((float) expenseTotalCost, "Expense"));
            }

            // Create pie chart dataset
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setValueTextSize(12f);

            // Create pie chart
            PieData data = new PieData(dataSet);
            mGeneralCostPerMonthChart.setData(data);
            mGeneralCostPerMonthChart.getDescription().setEnabled(false);
            mGeneralCostPerMonthChart.setDrawEntryLabels(false);
            mGeneralCostPerMonthChart.animateY(1000, Easing.EaseInOutQuad);
            mGeneralCostPerMonthChart.invalidate();

            // Set up the pie chart
            setupPieChart(refuelingTotalCost, serviceTotalCost, expenseTotalCost);
        });
    }

    //GFetch the total cost for all the collections
    private void fetchTotalCostAndCostPerDayFromAllCollections() {
        CollectionReference refuelingCollectionReference = Utility.getCollectionReferenceForRefueling();
        CollectionReference serviceCollectionReference = Utility.getCollectionReferenceForService();
        CollectionReference expenseCollectionReference = Utility.getCollectionReferenceForExpense();

        Task<QuerySnapshot> refuelingTask = refuelingCollectionReference.get();
        Task<QuerySnapshot> serviceTask = serviceCollectionReference.get();
        Task<QuerySnapshot> expenseTask = expenseCollectionReference.get();

        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(refuelingTask, serviceTask, expenseTask);

        allTasks.addOnSuccessListener(querySnapshots -> {
            double totalCost = 0;
            long minTimestamp = Long.MAX_VALUE;
            long maxTimestamp = Long.MIN_VALUE;
            double maxOdometer = 0;
            int totalEntries = 0;

            // Process refueling costs and timestamps
            QuerySnapshot refuelingSnapshot = querySnapshots.get(0);
            for (QueryDocumentSnapshot document : refuelingSnapshot) {
                Double cost = document.getDouble("refuelingTotalCost");
                Timestamp timestamp = document.getTimestamp("refuelingTimeStamp");
                Double odometer = document.getDouble("refuelingOdometer");
                totalEntries += refuelingSnapshot.size();
                if (cost != null) {
                    totalCost += cost;
                }
                if (odometer != null) {
                    maxOdometer = Math.max(maxOdometer, odometer);
                }
                if (timestamp != null) {
                    long millis = timestamp.toDate().getTime();
                    minTimestamp = Math.min(minTimestamp, millis);
                    maxTimestamp = Math.max(maxTimestamp, millis);
                }
            }

            // Process service costs and timestamps
            QuerySnapshot serviceSnapshot = querySnapshots.get(1);
            for (QueryDocumentSnapshot document : serviceSnapshot) {
                Double cost = document.getDouble("serviceTotalCost");
                Timestamp timestamp = document.getTimestamp("serviceTimeStamp");
                Double odometer = document.getDouble("serviceOdometer");
                totalEntries += serviceSnapshot.size();
                if (cost != null) {
                    totalCost += cost;
                }
                if (odometer != null) {
                    maxOdometer = Math.max(maxOdometer, odometer);
                }
                if (timestamp != null) {
                    long millis = timestamp.toDate().getTime();
                    minTimestamp = Math.min(minTimestamp, millis);
                    maxTimestamp = Math.max(maxTimestamp, millis);
                }
            }

            // Process expense costs and timestamps
            QuerySnapshot expenseSnapshot = querySnapshots.get(2);
            for (QueryDocumentSnapshot document : expenseSnapshot) {
                Double cost = document.getDouble("expenseTotalCost");
                Timestamp timestamp = document.getTimestamp("expenseTimeStamp");
                Double odometer = document.getDouble("expenseOdometer");
                totalEntries += expenseSnapshot.size();
                if (cost != null) {
                    totalCost += cost;
                }
                if (odometer != null) {
                    maxOdometer = Math.max(maxOdometer, odometer);
                }
                if (timestamp != null) {
                    long millis = timestamp.toDate().getTime();
                    minTimestamp = Math.min(minTimestamp, millis);
                    maxTimestamp = Math.max(maxTimestamp, millis);
                }
            }

            // Calculate and display the total cost
            updateTotalCost(totalCost);

            // Calculate and display the cost per day
            updateWithCostPerDay(totalCost, minTimestamp, maxTimestamp);

            // Calculate and display the cost per kilometer
            displayAvgCostPerKm(totalCost, maxOdometer);

            // Calculate and display the daily distance per kilometer
            updateDailyAverageGeneralDistance(minTimestamp, maxTimestamp,maxOdometer);

            // Display the Total Distance
            updateTotalGeneralDistance(maxOdometer);

            // Update UI with total number of entries
            updateReportGeneralTitle(minTimestamp, maxTimestamp);

        });
    }

    //Setup the pieChart
    private void setupPieChart(double refuelingCost, double serviceCost, double expenseCost) {
        // Create a list of pie entries
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) refuelingCost, "Refueling"));
        entries.add(new PieEntry((float) serviceCost, "Service"));
        entries.add(new PieEntry((float) expenseCost, "Expense"));

        // Create a dataset for the entries
        PieDataSet dataSet = new PieDataSet(entries, "Total Cost per Collection");

        // Set colors for the pie chart
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        // Set text size and colors for the pie chart values and labels
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(10f);

        // Create a pie data object with the dataset
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new DefaultValueFormatter(2));

        // Get a reference to the chart view
        PieChart chart = getView().findViewById(R.id.generalCostPerMonthChart);

        // Set chart data and properties
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.setDrawHoleEnabled(true);
        chart.setTransparentCircleRadius(0f);
        chart.setHoleRadius(40f);
        chart.animateY(1000, Easing.EaseInOutQuad);
        chart.invalidate();
    }

    // Calculate and display the cost per day
    private void updateWithCostPerDay(double totalCost, long minTimestamp, long maxTimestamp) {
        if (minTimestamp != Long.MAX_VALUE && maxTimestamp != Long.MIN_VALUE) {
            long totalDays = TimeUnit.MILLISECONDS.toDays(maxTimestamp - minTimestamp) + 1;
            double avgCostPerDay = totalCost / totalDays;
            mByDayGeneralCostTextView.setText(String.format("€%,.2f", avgCostPerDay));
        } else {
            mByDayGeneralCostTextView.setText("N/A");
        }
    }

    // Display the Total Distance
    private void updateTotalGeneralDistance(double maxOdometer) {
        mTotalGeneralDistance.setText(String.format("%,.1f km", maxOdometer));
    }

    // Calculate and display the daily distance per kilometer
    private void updateDailyAverageGeneralDistance(long minTimestamp, long maxTimestamp, double maxOdometer) {
        if (minTimestamp != Long.MAX_VALUE && maxTimestamp != Long.MIN_VALUE) {
            long totalDays = TimeUnit.MILLISECONDS.toDays(maxTimestamp - minTimestamp) + 1;
            double dailyAverageDistance = maxOdometer / totalDays;
            mDailyAverageGeneralDistance.setText(String.format("%,.1f km", dailyAverageDistance));
        } else {
            mDailyAverageGeneralDistance.setText("N/A");
        }
    }

    // Calculate and display the total cost
    private void updateTotalCost(double totalCost) {
        mTotalGeneralCostTextView.setText(String.format("€%,.2f", totalCost));
    }

    // Calculate and display the cost per kilometer
    private void displayAvgCostPerKm(double totalCost, double maxOdometer) {
        if (maxOdometer > 0) {
            double avgCostPerKm = totalCost / maxOdometer;
            mByKmGeneralCostTextView.setText(String.format("€%,.3f", avgCostPerKm));
        } else {
            mByKmGeneralCostTextView.setText("N/A");
        }
    }

    // Update the report general title with number of entries and date range
    // Update the report general title with number of entries and date range
    private void updateReportGeneralTitle(long minTimestamp, long maxTimestamp) {
        CollectionReference refuelingCollectionReference = Utility.getCollectionReferenceForRefueling();
        CollectionReference serviceCollectionReference = Utility.getCollectionReferenceForService();
        CollectionReference expenseCollectionReference = Utility.getCollectionReferenceForExpense();

        Task<QuerySnapshot> refuelingTask = refuelingCollectionReference.get();
        Task<QuerySnapshot> serviceTask = serviceCollectionReference.get();
        Task<QuerySnapshot> expenseTask = expenseCollectionReference.get();

        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(refuelingTask, serviceTask, expenseTask);

        allTasks.addOnSuccessListener(querySnapshots -> {
            int totalEntries = 0;

            // Process refueling entries
            QuerySnapshot refuelingSnapshot = querySnapshots.get(0);
            totalEntries += refuelingSnapshot.size();

            // Process service entries
            QuerySnapshot serviceSnapshot = querySnapshots.get(1);
            totalEntries += serviceSnapshot.size();

            // Process expense entries
            QuerySnapshot expenseSnapshot = querySnapshots.get(2);
            totalEntries += expenseSnapshot.size();

            DateFormat dateFormat = SimpleDateFormat.getDateInstance();
            String startDate = dateFormat.format(new Date(minTimestamp));
            String endDate = dateFormat.format(new Date(maxTimestamp));
            String title = totalEntries + " Entries (" + startDate + " - " + endDate + ")";
            mReportGeneralTitle.setText(title);
        });
    }
}