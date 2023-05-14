package com.myproject.myvehicleapp.AddActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.myproject.myvehicleapp.Models.NoteModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

// This class handles the activity for adding, editing, and deleting a Note in the app
public class AddEditDeleteNoteActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    String title,content,docId;
    boolean isEditMode = false;
    Toolbar toolbarNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_note);

        // Initialize toolbar and set it as the action bar
        toolbarNotes = findViewById(R.id.toolbar_notes);
        setSupportActionBar(toolbarNotes);
        getSupportActionBar().setTitle("Add Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);

        //receive data
        title = getIntent().getStringExtra("title");
        content= getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        // Check if in edit mode
        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        // Set received data to EditText views
        titleEditText.setText(title);
        contentEditText.setText(content);

    }

    // Method to validate and save note
    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        // Check if note title is provided
        if(noteTitle==null || noteTitle.isEmpty() ){
            titleEditText.setError("Title is required");
            return;
        }
        // Create note model and set its fields
        NoteModel noteModel = new NoteModel();
        noteModel.setTitle(noteTitle);
        noteModel.setContent(noteContent);
        noteModel.setTimestamp(Timestamp.now());

        // Save note to Firebase
        saveNoteToFirebase(noteModel);

    }

    // Method to save note to Firebase
    void saveNoteToFirebase(NoteModel noteModel){
        DocumentReference documentReference;
        // Check if in edit mode and get the corresponding document reference
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        // Save note to Firebase
        documentReference.set(noteModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(AddEditDeleteNoteActivity.this,"Note added successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteNoteActivity.this,"Failed while adding note");
                }
            }
        });

    }

    // Method to delete note from Firebase
    void deleteNoteFromFirebase(){
        DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(AddEditDeleteNoteActivity.this,"Note deleted successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteNoteActivity.this,"Failed while deleting note");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu and handle visibility of menu items based on edit mode
        getMenuInflater().inflate(R.menu.save_note_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.saveNotesBtn);
        MenuItem deleteItem = menu.findItem(R.id.deleteNotesBtn);

        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        if(isEditMode){
            getSupportActionBar().setTitle("Edit your Note");
            saveItem.setVisible(true);
            deleteItem.setVisible(true);
        }

        return true;
    }

    // Handle item selection in the options menu
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.saveNotesBtn:
                // Handle savr icon click
                saveNote();
                return true;
            case R.id.deleteNotesBtn:
                // Handle delete icon click
                deleteNoteFromFirebase();
                return true;
            case android.R.id.home:
                // Handle back button click
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}