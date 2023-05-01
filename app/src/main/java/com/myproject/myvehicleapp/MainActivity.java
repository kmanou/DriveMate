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

public class MainActivity extends AppCompatActivity implements ActionBottomDialogFragment.ItemClickListener{


    BottomNavigationView bottomNavigationView;
    FloatingActionButton myAddFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.mainMenuNavBar);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,new HistoryFragment()).commit();

        myAddFab = findViewById(R.id.fab);
        ActionBottomDialogFragment actionBottomDialogFragment = ActionBottomDialogFragment.newInstance();
        myAddFab.setOnClickListener( view -> actionBottomDialogFragment.show(getSupportFragmentManager(), ActionBottomDialogFragment.TAG));

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainMenuNavBar);
        bottomNavigationView.setSelectedItemId(R.id.History);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId())
                {
                    case R.id.History: temp = new HistoryFragment();
                        break;
                    case R.id.Reports : temp = new ReportsFragment();
                        break;
                    case R.id.Reminders : temp = new RemindersFragment();
                        break;
                    case R.id.More : temp = new MoreFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,temp).commit();
                return true;
            }
        });
    }

    @Override
    public void onItemClick(String item) {

    }
}