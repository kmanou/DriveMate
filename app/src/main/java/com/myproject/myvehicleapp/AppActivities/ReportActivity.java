package com.myproject.myvehicleapp.AppActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.myproject.myvehicleapp.Adapters.ReportAdapter;
import com.myproject.myvehicleapp.R;

public class ReportActivity extends AppCompatActivity {

    private TabLayout tabLayoutReports;
    private ViewPager2 viewPagerReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tabLayoutReports = findViewById(R.id.tabLayoutReports);
        viewPagerReports = findViewById(R.id.viewPagerReports);

        // Create an instance of the ReportAdapter and set it as the adapter for the ViewPager2
        ReportAdapter adapter = new ReportAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerReports.setAdapter(adapter);

        // Set up the TabLayout and connect it with the ViewPager2 using TabLayoutMediator
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayoutReports, viewPagerReports,
                true, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                // Set the text for each tab based on the position
                switch (position) {
                    case 0:
                        tab.setText("General");
                        break;
                    case 1:
                        tab.setText("Refueling");
                        break;
                    case 2:
                        tab.setText("Expense");
                        break;
                    case 3:
                        tab.setText("Service");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
    }
}
