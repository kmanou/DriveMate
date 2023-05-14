package com.myproject.myvehicleapp.AddActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.myproject.myvehicleapp.Models.ReminderModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddEditDeleteReminderActivity extends AppCompatActivity {

    // Define all the UI elements
    TextInputEditText reminderTitleTI;
    TextInputEditText reminderDatePickerTI;
    TextInputEditText reminderTimePickerTI;
    TextInputEditText reminderDescriptionTI;

    // Define all the data elements
    String reminderTitle;
    Timestamp reminderTimeStamp;
    String reminderDescription;
    String docId;

    boolean isEditMode = false;
    Toolbar toolbarReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_reminder);

        // Find all the UI elements by their IDs
        toolbarReminder = findViewById(R.id.toolbar_reminder);
        setSupportActionBar(toolbarReminder);
        getSupportActionBar().setTitle("Reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        reminderTitleTI = findViewById(R.id.reminderTitleInputTI);
        reminderDatePickerTI = findViewById(R.id.reminderDatePickerTI);
        reminderTimePickerTI = findViewById(R.id.reminderTimePickerTI);
        reminderDescriptionTI = findViewById(R.id.reminderDescriptionInputTI);

        //receive data
        reminderTitle = getIntent().getStringExtra("reminderTitle");
        reminderTimeStamp = getIntent().getParcelableExtra("reminderTimestamp");
        reminderDescription = getIntent().getStringExtra("reminderDescription");
        docId = getIntent().getStringExtra("docId");

        // Check if we are in edit mode
        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        // Set the UI elements to display the retrieved data
        if (reminderTimeStamp != null) {
            Date date = reminderTimeStamp.toDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Set the text of the views to the formatted date and time strings
            reminderDatePickerTI.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date));
            reminderTimePickerTI.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));
        }

        reminderTitleTI.setText(reminderTitle);
        reminderDescriptionTI.setText(reminderDescription);
    }

    // Save or update the reminder to Firebase
    void saveReminder() {
        // Validate input and create ReminderModel object
        String reminderDate = Objects.requireNonNull(reminderDatePickerTI.getText()).toString();
        String reminderTime = Objects.requireNonNull(reminderTimePickerTI.getText()).toString();

        String reminderTitleStr = Objects.requireNonNull(reminderTitleTI.getText()).toString();
        if (reminderTitleStr.isEmpty()) {
            reminderTitleTI.setError("Title is required");
            return;
        }

        String reminderDescriptionStr = Objects.requireNonNull(reminderDescriptionTI.getText()).toString();

        // Create a Date object from the refuelingDate and refuelingTime strings
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date dateTime = null;
        try {
            dateTime = dateTimeFormat.parse(reminderDate + " " + reminderTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Convert the Date object to a Timestamp object
        Timestamp timestamp = new Timestamp(new Date(dateTime.getTime()));

        // Create ReminderModel object and set its properties
        ReminderModel reminderModel = new ReminderModel();

        reminderModel.setReminderTitle(reminderTitleStr);
        reminderModel.setReminderTimestamp(timestamp);
        reminderModel.setReminderDescription(reminderDescriptionStr);

        //Save ReminderModel to Firebase
        saveReminderToFirebase(reminderModel);
    }

    // Save or update the reminder to Firebase
    void saveReminderToFirebase(ReminderModel reminderModel){
        DocumentReference documentReference;
        if(isEditMode){
            //update the reminder
            documentReference = Utility.getCollectionReferenceForReminders().document(docId);
            reminderModel.setAlarmEnabled(true);
        }else{
            //create new reminder
            documentReference = Utility.getCollectionReferenceForReminders().document();
        }


        documentReference.set(reminderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //reminder is added
                    Utility.showToast(AddEditDeleteReminderActivity.this,"Reminder added successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteReminderActivity.this,"Failed while adding reminder");
                }
            }
        });
    }

    // Delete the reminder from Firebase
    void deleteReminderFromFirebase(){
        DocumentReference documentReference = Utility.getCollectionReferenceForReminders().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //reminder is deleted
                    Utility.showToast(AddEditDeleteReminderActivity.this,"Reminder deleted successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteReminderActivity.this,"Failed while deleting reminder");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu and handle visibility of items
        getMenuInflater().inflate(R.menu.save_reminder_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.saveReminderBtn);
        MenuItem deleteItem = menu.findItem(R.id.deleteReminderBtn);

        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        if(isEditMode){
            getSupportActionBar().setTitle("Edit your Reminder");
            saveItem.setVisible(true);
            deleteItem.setVisible(true);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Handle item selections in the menu
        switch (id) {
            case R.id.saveReminderBtn:
                // Handle edit icon click
                saveReminder();
                return true;
            case R.id.deleteReminderBtn:
                // Handle delete icon click
                deleteReminderFromFirebase();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Show a date picker dialog
    public void showDatePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Save the selected date to a Calendar object
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Format the selected date and display it in the date EditText
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                reminderDatePickerTI.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // Show a time picker dialog
    public void showTimePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Save the selected time to a Calendar object
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                // Format the selected time and display it in the time EditText
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                reminderTimePickerTI.setText(timeFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }
}