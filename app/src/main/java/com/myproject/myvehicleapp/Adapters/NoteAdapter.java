package com.myproject.myvehicleapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteNoteActivity;
import com.myproject.myvehicleapp.Models.NoteModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Utility;

// Adapter class to handle the notes in the Firestore database
public class NoteAdapter extends FirestoreRecyclerAdapter<NoteModel, NoteAdapter.NoteViewHolder> {
    // Variable to store application context
    Context context;

    // Constructor for the NoteAdapter
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<NoteModel> options, Context context) {
        // Call the superclass constructor (FirestoreRecyclerAdapter)
        super(options);
        // Set the context
        this.context = context;
    }

    // This function binds the data to the view holder
    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull NoteModel noteModel) {
        // Set the title of the note
        holder.titleTextView.setText(noteModel.title);
        // Set the content of the note
        holder.contentTextView.setText(noteModel.content);
        // Set the timestamp of the note
        holder.timestampTextView.setText(Utility.timestampToString(noteModel.timestamp));

        // Set an OnClickListener for the itemView
        holder.itemView.setOnClickListener((v)->{
            // Create a new intent for the AddEditDeleteNoteActivity
            Intent intent = new Intent(context, AddEditDeleteNoteActivity.class);
            // Add the title and content to the intent extras
            intent.putExtra("title",noteModel.title);
            intent.putExtra("content",noteModel.content);
            // Get the document ID and add it to the intent extras
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            // Start the activity
            context.startActivity(intent);
        });
    }

    // This function inflates the view and returns the NoteViewHolder
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view from the XML layout file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note,parent,false);
        // Return a new NoteViewHolder
        return new NoteViewHolder(view);
    }

    // ViewHolder class to hold the views for each note
    class NoteViewHolder extends RecyclerView.ViewHolder{
        // TextViews to display the note's title, content, and timestamp
        TextView titleTextView,contentTextView,timestampTextView;

        // Constructor for the NoteViewHolder
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews
            titleTextView = itemView.findViewById(R.id.note_title_text_view);
            contentTextView = itemView.findViewById(R.id.note_content_text_view);
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view);
        }
    }
}
