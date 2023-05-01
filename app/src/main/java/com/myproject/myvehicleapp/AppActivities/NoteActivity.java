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
import com.myproject.myvehicleapp.MainActivity;
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

        addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(NoteActivity.this, AddEditDeleteNoteActivity.class)) );

        setupRecyclerView();
    }

    void setupRecyclerView(){
        Query query  = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NoteModel> options = new FirestoreRecyclerOptions.Builder<NoteModel>()
                .setQuery(query,NoteModel.class).build();
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options,this);
        noteRecyclerView.setAdapter(noteAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Navigate back to the specific activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}