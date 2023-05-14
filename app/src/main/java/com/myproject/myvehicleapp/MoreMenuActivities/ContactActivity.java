package com.myproject.myvehicleapp.MoreMenuActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;

public class ContactActivity extends AppCompatActivity {

    // Define objects for EditText and Button
    Button sendMailButton;
    TextInputEditText email, subject, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Initialize the toolbar
        initToolbar();

        // Get references to the EditText and Button views
        email = findViewById(R.id.contactEmail);
        subject = findViewById(R.id.contactSubject);
        message = findViewById(R.id.contactMessage);
        sendMailButton = findViewById(R.id.sendEmailBtn);

        // Attach a click listener to the sendMailButton
        sendMailButton.setOnClickListener(view -> {
            // Retrieve the entered email, subject, and message values
            String emailSend = email.getText().toString();
            String emailSubject = subject.getText().toString();
            String emailBody = message.getText().toString();

            // Create an Intent object with the action attribute set to ACTION_SEND
            Intent intent = new Intent(Intent.ACTION_SEND);

            // Add the email, subject, and message fields to the intent using putExtra function
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailSend});
            intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
            intent.putExtra(Intent.EXTRA_TEXT, emailBody);

            // Set the type of the intent to "message/rfc822" for email
            intent.setType("message/rfc822");

            // Start the activity with the intent and display a chooser dialog to select an email client
            startActivity(Intent.createChooser(intent, "Choose an Email client:"));
        });
    }

    // Initialize the toolbar and set its properties
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_contact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu resource file
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item clicks
        if (item.getItemId() == android.R.id.home) {
            // If the home button is clicked, finish the activity and return to the previous screen
            finish();
        } else {
            // If any other menu item is clicked, show a toast message with the title of the clicked item
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}