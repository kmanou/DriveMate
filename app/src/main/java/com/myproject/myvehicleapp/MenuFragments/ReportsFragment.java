package com.myproject.myvehicleapp.MenuFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.myproject.myvehicleapp.Adapters.ReportAdapter;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;


public class ReportsFragment extends Fragment {


    private TabLayout tabLayoutReports;
    private ViewPager2 viewPagerReports;
    private Toolbar mainToolbarReport;


    public ReportsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        mainToolbarReport = view.findViewById(R.id.mainToolbarReport);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mainToolbarReport);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Reports");
        Tools.setSystemBarColor(getActivity(), R.color.red_800);


        tabLayoutReports = view.findViewById(R.id.tabLayoutReports);
        viewPagerReports = view.findViewById(R.id.viewPagerReports);

        ReportAdapter adapter = new ReportAdapter(getChildFragmentManager(), getLifecycle());

        viewPagerReports.setAdapter(adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayoutReports, viewPagerReports,
                true, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

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
        return view;
    }
}