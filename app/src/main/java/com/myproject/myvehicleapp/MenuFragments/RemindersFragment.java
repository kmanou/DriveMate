package com.myproject.myvehicleapp.MenuFragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.myproject.myvehicleapp.Adapters.ReminderAdapter;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.Models.ReminderModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.ReminderAlarmReceiver;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

import java.util.Date;


public class RemindersFragment extends Fragment {

    private RecyclerView reminderRecyclerView;
    private ReminderAdapter reminderAdapter;
    private Toolbar mainToolbarReminder;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);

        // Set up the toolbar
        mainToolbarReminder = view.findViewById(R.id.mainToolbarReminderFRG);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mainToolbarReminder);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Reminders");
        Tools.setSystemBarColor(getActivity(), R.color.red_800);

        // Set up the RecyclerView
        reminderRecyclerView = view.findViewById(R.id.reminder_recycler_view_frg);

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is signed in, set up the recycler view
            setupRecyclerView();
            // Set up alarm manager and pending intent
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(requireActivity(), ReminderAlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(requireActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        }

        return view;
    }

    void setupRecyclerView() {
        // Set up the Firestore query
        Query query = Utility.getCollectionReferenceForReminders().orderBy("reminderTimestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ReminderModel> options = new FirestoreRecyclerOptions.Builder<ReminderModel>()
                .setQuery(query, ReminderModel.class).build();

        // Set up the RecyclerView layout manager, adapter, and adapter
        reminderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reminderAdapter = new ReminderAdapter(options, getActivity());
        reminderRecyclerView.setAdapter(reminderAdapter);
    }

    public RemindersFragment() {
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

        // Start the alarm manager
        startAlarm();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (reminderAdapter != null) {
            reminderAdapter.stopListening();
        }

        // Stop the alarm manager
        stopAlarm();
    }

    private void startAlarm() {
        // Retrieve the next reminder from Firestore and set up the alarm
        Utility.getCollectionReferenceForReminders().orderBy("reminderTimestamp", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("reminderTimestamp", new Date())
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        ReminderModel reminder = documentSnapshot.toObject(ReminderModel.class);
                        if (reminder != null) {
                            // Set the alarm trigger time
                            long triggerTime = reminder.getReminderTimestamp().toDate().getTime();

                            // Create the alarm intent
                            Intent alarmIntent = new Intent(getActivity(), ReminderAlarmReceiver.class);
                            alarmIntent.putExtra("reminderId", documentSnapshot.getId());
                            alarmIntent.putExtra("reminderTitle", reminder.getReminderTitle());
                            alarmIntent.putExtra("reminderDescription", reminder.getReminderDescription());

                            // Add the system alarm sound URI to the intent
                            Uri alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                            alarmIntent.putExtra("soundUri", alarmSoundUri.toString());

                            // Create a pending intent for the alarm
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                            // Set the alarm using the alarm manager
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                        }
                    }
                });
    }

    private void stopAlarm() {
        // Cancel the alarm using the alarm manager
        alarmManager.cancel(alarmIntent);
    }

}