package com.myproject.myvehicleapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myproject.myvehicleapp.ReportsFragments.ExpenseReportFragment;
import com.myproject.myvehicleapp.ReportsFragments.GeneralReportFragment;
import com.myproject.myvehicleapp.ReportsFragments.RefuelingReportFragment;
import com.myproject.myvehicleapp.ReportsFragments.ServiceReportFragment;

public class ReportAdapter extends FragmentStateAdapter {

    public ReportAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;

        switch (position){
            case 0:
                fragment = GeneralReportFragment.newInstance();
                break;
            case 1:
                fragment = RefuelingReportFragment.newInstance();
                break;
            case 2:
                fragment = ExpenseReportFragment.newInstance();
                break;
            case 3:
                fragment = ServiceReportFragment.newInstance();
                break;
            default:
                return null;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
