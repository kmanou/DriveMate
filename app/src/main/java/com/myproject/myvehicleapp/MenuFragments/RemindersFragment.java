package com.myproject.myvehicleapp.MenuFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.myproject.myvehicleapp.Adapters.ReminderAdapter;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.Models.HistoryViewModel;
import com.myproject.myvehicleapp.Models.ReminderModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utlities.Tools;
import com.myproject.myvehicleapp.Utlities.Utility;


public class RemindersFragment extends Fragment {

    private RecyclerView reminderRecyclerView;
    private ReminderAdapter reminderAdapter;
    private Toolbar mainToolbarReminder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);

        mainToolbarReminder = view.findViewById(R.id.mainToolbarReminderFRG);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mainToolbarReminder);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Reminders");
        Tools.setSystemBarColor(getActivity(), R.color.red_800);

        reminderRecyclerView = view.findViewById(R.id.reminder_recycler_view_frg);

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is signed in, set up the recycler view
            setupRecyclerView();
        } else {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        }
        return view;
    }

    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForReminders().orderBy("reminderTimestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ReminderModel> options = new FirestoreRecyclerOptions.Builder<ReminderModel>()
                .setQuery(query, ReminderModel.class).build();
        reminderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reminderAdapter = new ReminderAdapter(options, getActivity());
        reminderRecyclerView.setAdapter(reminderAdapter);
    }

    public RemindersFragment(){
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        reminderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (reminderAdapter != null) {
            reminderAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (reminderAdapter != null) {
            reminderAdapter.stopListening();
        }
    }
}