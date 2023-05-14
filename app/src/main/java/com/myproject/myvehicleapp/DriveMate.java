package com.myproject.myvehicleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.myproject.myvehicleapp.MenuFragments.HistoryFragment;
import com.myproject.myvehicleapp.MenuFragments.MoreFragment;
import com.myproject.myvehicleapp.MenuFragments.RemindersFragment;
import com.myproject.myvehicleapp.MenuFragments.ReportsFragment;
import com.myproject.myvehicleapp.Utilities.ActionBottomDialogFragment;

public class DriveMate extends AppCompatActivity implements ActionBottomDialogFragment.ItemClickListener {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton myAddFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        bottomNavigationView = findViewById(R.id.mainMenuNavBar);
        myAddFab = findViewById(R.id.fab);

        // Set the background of the bottom navigation view to null
        bottomNavigationView.setBackground(null);
        // Disable the third menu item
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        // Replace the initial fragment with the HistoryFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new HistoryFragment()).commit();

        // Set up the action for the FAB click event
        ActionBottomDialogFragment actionBottomDialogFragment = ActionBottomDialogFragment.newInstance();
        myAddFab.setOnClickListener(view -> actionBottomDialogFragment.show(getSupportFragmentManager(), ActionBottomDialogFragment.TAG));

        // Set the selected item in the bottom navigation view to History
        bottomNavigationView.setSelectedItemId(R.id.History);

        // Set up the item selection listener for the bottom navigation view
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment temp = null;
            switch (item.getItemId()) {
                case R.id.History:
                    temp = new HistoryFragment();
                    break;
                case R.id.Reports:
                    temp = new ReportsFragment();
                    break;
                case R.id.Reminders:
                    temp = new RemindersFragment();
                    break;
                case R.id.More:
                    temp = new MoreFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, temp).commit();
            return true;
        });
    }

    @Override
    public void onItemClick(String item) {
        // Handle item click event
    }
}