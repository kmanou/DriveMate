package com.myproject.myvehicleapp.Models;

import com.google.firebase.Timestamp;

public class NoteModel {
    public String title; // The title of the note
    public String content; // The content of the note
    public Timestamp timestamp; // The timestamp indicating when the note was created or last modified

    // Default constructor
    public NoteModel() {
    }

    // Getters and setters for the NoteModel properties
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
