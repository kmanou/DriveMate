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

// This class is a FragmentStateAdapter that handles the fragments for the reporting feature.
public class ReportAdapter extends FragmentStateAdapter {

    // Constructor for ReportAdapter that receives the FragmentManager and Lifecycle.
    // These parameters are passed to the super constructor of FragmentStateAdapter.
    public ReportAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    // This method creates the Fragment corresponding to the specified position.
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;

        // Depending on the position, instantiate the corresponding Fragment.
        // Currently, there are four fragments: GeneralReportFragment, RefuelingReportFragment,
        // ExpenseReportFragment, and ServiceReportFragment.
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
                return null; // Return null if position is out of range.
        }

        // Return the instantiated fragment.
        return fragment;
    }

    // This method returns the total number of fragments.
    // Currently, there are four fragments, so it returns 4.
    @Override
    public int getItemCount() {
        return 4;
    }
}

