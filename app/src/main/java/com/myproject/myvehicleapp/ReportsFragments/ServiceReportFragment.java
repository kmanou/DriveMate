package com.myproject.myvehicleapp.ReportsFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myproject.myvehicleapp.R;


public class ServiceReportFragment extends Fragment {

    public static ServiceReportFragment newInstance(){
        return new ServiceReportFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Define the design file
        View view = inflater.inflate(R.layout.fragment_service_report,container,false);

        return view;
    }
}