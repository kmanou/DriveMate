package com.myproject.myvehicleapp.MenuFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.auth.FirebaseAuth;
import com.myproject.myvehicleapp.AppActivities.NoteActivity;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.MoreMenuActivities.AboutActivity;
import com.myproject.myvehicleapp.MoreMenuActivities.ContactActivity;
import com.myproject.myvehicleapp.MoreMenuActivities.FuelActivity;
import com.myproject.myvehicleapp.MoreMenuActivities.PaymentMethodActivity;
import com.myproject.myvehicleapp.MoreMenuActivities.TypeOfExpenseActivity;
import com.myproject.myvehicleapp.MoreMenuActivities.TypeOfServiceActivity;
import com.myproject.myvehicleapp.MoreMenuActivities.VehicleActivity;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;


public class MoreFragment extends Fragment {

    private Toolbar mainToolbarMore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        // Set up the toolbar
        mainToolbarMore = view.findViewById(R.id.mainToolbarMore);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mainToolbarMore);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("More Menu");
        Tools.setSystemBarColor(getActivity(), R.color.red_800);

        // Set up click listeners for each menu item
        view.findViewById(R.id.layoutMoreContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the ContactActivity
                Intent intent = new Intent(view.getContext(), ContactActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.layoutMoreAbout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the AboutActivity
                Intent intent = new Intent(view.getContext(), AboutActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.layoutMoreNotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the NoteActivity
                Intent intent = new Intent(view.getContext(), NoteActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.layoutMoreFuel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the FuelActivity
                Intent intent = new Intent(view.getContext(), FuelActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.layoutMoreVehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the VehicleActivity
                Intent intent = new Intent(view.getContext(), VehicleActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.layoutMoreTypesOfService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the TypeOfServiceActivity
                Intent intent = new Intent(view.getContext(), TypeOfServiceActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.layoutMoreTypesOfExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the TypeOfExpenseActivity
                Intent intent = new Intent(view.getContext(), TypeOfExpenseActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.layoutMorePaymentMethods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the PaymentMethodActivity
                Intent intent = new Intent(view.getContext(), PaymentMethodActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.layoutMoreLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform logout by signing out the user
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    public MoreFragment() {
        // Required empty public constructor
    }
}
