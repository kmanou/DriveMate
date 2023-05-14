package com.myproject.myvehicleapp.AppActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.myproject.myvehicleapp.Adapters.NoteAdapter;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteNoteActivity;
import com.myproject.myvehicleapp.DriveMate;
import com.myproject.myvehicleapp.Models.NoteModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;


public class NoteActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView noteRecyclerView;
    NoteAdapter noteAdapter;
    Toolbar mainToolbarNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mainToolbarNotes = findViewById(R.id.mainToolbarNotes);
        setSupportActionBar(mainToolbarNotes);
        getSupportActionBar().setTitle("My Notes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        addNoteBtn = findViewById(R.id.add_note_btn);
        noteRecyclerView = findViewById(R.id.note_recycler_view);

        // Set a click listener for the "Add Note" button to open the AddEditDeleteNoteActivity
        addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(NoteActivity.this, AddEditDeleteNoteActivity.class)) );

        // Set up the RecyclerView to display the notes
        setupRecyclerView();
    }

    // Method to set up the RecyclerView with note data
    void setupRecyclerView(){
        // Construct a query to retrieve note data from the Firestore database
        Query query  = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NoteModel> options = new FirestoreRecyclerOptions.Builder<NoteModel>()
                .setQuery(query,NoteModel.class).build();

        // Set the layout manager and adapter for the RecyclerView
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options,this);
        noteRecyclerView.setAdapter(noteAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Navigate back to the specific activity
            Intent intent = new Intent(this, DriveMate.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for changes in the Firestore data and update the adapter accordingly
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for changes in the Firestore data
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Notify the adapter to update the view in case of any changes
        noteAdapter.notifyDataSetChanged();
    }
}
