package com.myproject.myvehicleapp.AddActivities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.myproject.myvehicleapp.Models.RefuelingModel;
import com.myproject.myvehicleapp.MoreMenuActivities.FuelActivity;
import com.myproject.myvehicleapp.MoreMenuActivities.PaymentMethodActivity;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddEditDeleteRefuelingActivity extends AppCompatActivity {

    // Define all the UI elements
    TextInputEditText refuelingDatePickerTI;
    TextInputEditText refuelingTimePickerTI;
    TextInputEditText refuelingOdometerTI;
    TextInputEditText refuelingFuelTypeTI;
    TextInputEditText refuelingPricePerLitreTI;
    TextInputEditText refuelingTotalCostTI;
    TextInputEditText refuelingFuelLitresTI;
    TextInputEditText refuelingPaymentMethodTI;
    TextInputEditText refuelingNoteTI;

    // Define all the data elements
    String recyclerTitleC;
    Timestamp refuelingTimeStamp;

    Integer refuelingOdometer;
    String refuelingFuelType;
    Float refuelingPricePerLitre;
    Float refuelingTotalCost;
    Float refuelingFuelLitres;
    String refuelingPaymentMethod;
    String refuelingNote;
    String docId;

    boolean isEditMode = false;
    Toolbar toolbarRefueling;

    // Define the ActivityResultLaunchers for opening the PaymentMethodActivity and FuelActivity
    private ActivityResultLauncher<Intent> paymentMethodActivityResultLauncher;
    private ActivityResultLauncher<Intent> fuelActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_refueling);

        // Find all the UI elements by their IDs
        toolbarRefueling = findViewById(R.id.toolbar_refueling);
        setSupportActionBar(toolbarRefueling);
        getSupportActionBar().setTitle("Refueling");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        refuelingDatePickerTI = findViewById(R.id.refuelingDatePickerTI);
        refuelingTimePickerTI = findViewById(R.id.refuelingTimePickerTI);
        refuelingOdometerTI = findViewById(R.id.refuelingOdometerTI);
        refuelingFuelTypeTI = findViewById(R.id.refuelingFuelTypeTI);
        refuelingPricePerLitreTI = findViewById(R.id.refuelingPricePerLitreTI);
        refuelingTotalCostTI = findViewById(R.id.refuelingTotalCostTI);
        refuelingFuelLitresTI = findViewById(R.id.refuelingFuelLitresTI);
        refuelingPaymentMethodTI = findViewById(R.id.refuelingPaymentMethodTI);
        refuelingNoteTI = findViewById(R.id.refuelingNoteTI);

        //receive data
        recyclerTitleC = getIntent().getStringExtra("recyclerTitle");
        refuelingTimeStamp = getIntent().getParcelableExtra("refuelingTimestamp");
        refuelingOdometer = getIntent().getIntExtra("refuelingOdometer",0);
        refuelingFuelType = getIntent().getStringExtra("refuelingFuelType");
        refuelingPricePerLitre = getIntent().getFloatExtra("refuelingPricePerLitre",0);
        refuelingTotalCost = getIntent().getFloatExtra("refuelingTotalCost",0);
        refuelingFuelLitres = getIntent().getFloatExtra("refuelingFuelLitres",0);
        refuelingPaymentMethod = getIntent().getStringExtra("refuelingPaymentMethod");
        refuelingNote = getIntent().getStringExtra("refuelingNote");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        // Set the UI elements to display the retrieved data

        if (refuelingTimeStamp != null) {
            Date date = refuelingTimeStamp.toDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Set the text of the views to the formatted date and time strings
            refuelingDatePickerTI.setText(new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(date));
            refuelingTimePickerTI.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));
        }

        refuelingOdometerTI.setText(String.valueOf(refuelingOdometer));
        refuelingFuelTypeTI.setText(refuelingFuelType);
        refuelingPricePerLitreTI.setText(String.format("%.2f", refuelingPricePerLitre));
        refuelingTotalCostTI.setText(String.format("%.2f", refuelingTotalCost));
        refuelingFuelLitresTI.setText(String.format("%.2f", refuelingFuelLitres));
        refuelingPaymentMethodTI.setText(refuelingPaymentMethod);
        refuelingNoteTI.setText(refuelingNote);

        // Register activity result launchers for the payment method and fuel type activities
        // These will be used to open other activities for result
        paymentMethodActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String selectedPaymentMethod = result.getData().getStringExtra("selectedPaymentMethod");
                        refuelingPaymentMethodTI.setText(selectedPaymentMethod);
                    }
                });

        fuelActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String selectedFuelType = result.getData().getStringExtra("selectedFuelName");
                        refuelingFuelTypeTI.setText(selectedFuelType);
                    }
                });

        // Attach openPaymentMethodActivity and openFuelTypeActivity to the corresponding UI elements
        // Set onClick listeners to EditTexts to open respective activities
        refuelingPaymentMethodTI.setOnClickListener(v -> openPaymentMethodActivity());
        refuelingFuelTypeTI.setOnClickListener(v -> openFuelTypeActivity());

        // Auto calculate price per litre, fuel litres, and total cost when focus is gained
        refuelingPricePerLitreTI.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                autoCalculate();
            }
        });

        refuelingFuelLitresTI.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                autoCalculate();
            }
        });

        refuelingTotalCostTI.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                autoCalculate();
            }
        });
    }

    // Method to open PaymentMethodActivity
    private void openPaymentMethodActivity() {
        Intent intent = new Intent(this, PaymentMethodActivity.class);
        intent.putExtra("selectMode", true);
        paymentMethodActivityResultLauncher.launch(intent);
    }

    // Method to open FuelTypeActivity
    private void openFuelTypeActivity() {
        Intent intent = new Intent(this, FuelActivity.class);
        intent.putExtra("selectMode", true);
        fuelActivityResultLauncher.launch(intent);
    }

    // Method to save refueling data
    void saveRefueling() {
        // Getting the entered values

        // Retrieve the refueling date from the user input
        String refuelingDate = Objects.requireNonNull(refuelingDatePickerTI.getText()).toString();
        // Retrieve the refueling time from the user input
        String refuelingTime = Objects.requireNonNull(refuelingTimePickerTI.getText()).toString();

        // Retrieve the refueling odometer reading from the user input
        String refuelingOdometerStr = Objects.requireNonNull(refuelingOdometerTI.getText()).toString();
        // If the user has not entered a value for the refueling odometer reading, display an error message
        if (refuelingOdometerStr.isEmpty()) {
            refuelingOdometerTI.setError("Odometer is required");
            return;
        }

        // Parse the string value of the refueling odometer reading to an integer
        int refuelingOdometer;
        try {
            refuelingOdometer = Integer.parseInt(refuelingOdometerStr);
        } catch (NumberFormatException e) {
            // If the parsing fails, display an error message
            refuelingOdometerTI.setError("Invalid odometer value");
            return;
        }

        // Retrieve the refueling fuel type from the user input
        String refuelingFuelType = Objects.requireNonNull(refuelingFuelTypeTI.getText()).toString();

        // Retrieve the refueling price per litre from the user input
        String refuelingPricePerLitreStr = Objects.requireNonNull(refuelingPricePerLitreTI.getText()).toString();
        // If the user has not entered a value for the refueling price per litre, display an error message
        if (refuelingPricePerLitreStr.isEmpty()) {
            refuelingPricePerLitreTI.setError("Price per litre is required");
            return;
        }

        // Parse the string value of the refueling price per litre to a float
        float refuelingPricePerLitre;
        try {
            refuelingPricePerLitre = Float.parseFloat(refuelingPricePerLitreStr);
        } catch (NumberFormatException e) {
            // If the parsing fails, display an error message
            refuelingPricePerLitreTI.setError("Invalid price per litre value");
            return;
        }

        // Retrieve the refueling total cost from the user input
        String refuelingTotalCostStr = Objects.requireNonNull(refuelingTotalCostTI.getText()).toString();
        // If the user has not entered a value for the refueling total cost, display an error message
        if (refuelingTotalCostStr.isEmpty()) {
            refuelingTotalCostTI.setError("Total cost is required");
            return;
        }

        // Parse the string value of the refueling total cost to a float
        float refuelingTotalCost;
        try {
            refuelingTotalCost = Float.parseFloat(refuelingTotalCostStr);
        } catch (NumberFormatException e) {
            // If the parsing fails, display an error message
            refuelingTotalCostTI.setError("Invalid total cost value");
            return;
        }

        // Retrieve the refueling fuel litres from the user input
        String refuelingFuelLitresStr = Objects.requireNonNull(refuelingFuelLitresTI.getText()).toString();
        // If the user has not entered a value for the refueling fuel litres, display an error message
        if (refuelingFuelLitresStr.isEmpty()) {
            refuelingFuelLitresTI.setError("Fuel litres is required");
            return;
        }
        // Parse the string value of the refueling fuel litres to a float
        float refuelingFuelLitres;
        try {
            refuelingFuelLitres = Float.parseFloat(refuelingFuelLitresStr);
        } catch (NumberFormatException e) {
            // If the parsing fails, display an error message
            refuelingFuelLitresTI.setError("Invalid fuel litres value");
            return;
        }

        // Retrieve the refueling payment method from the user input
        String refuelingPaymentMethod = Objects.requireNonNull(refuelingPaymentMethodTI.getText()).toString();

        // Retrieve the refueling note from the user input
        String refuelingNote = Objects.requireNonNull(refuelingNoteTI.getText()).toString();

        // // Parse the refueling date and time into a Timestamp object
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date dateTime = null;
        try {
            dateTime = dateTimeFormat.parse(refuelingDate + " " + refuelingTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Convert the Date object to a Timestamp object
        Timestamp timestamp = new Timestamp(new Date(dateTime.getTime()));


        // Create RefuelingModel object and set its properties
        RefuelingModel refuelingModel = new RefuelingModel();

        refuelingModel.setRecyclerTitle("Refueling");
        refuelingModel.setRefuelingTimestamp(timestamp);
        refuelingModel.setRefuelingOdometer(refuelingOdometer);
        refuelingModel.setRefuelingFuelType(refuelingFuelType);
        refuelingModel.setRefuelingFuelLitres(refuelingFuelLitres);
        refuelingModel.setRefuelingPricePerLitre(refuelingPricePerLitre);
        refuelingModel.setRefuelingTotalCost(refuelingTotalCost);
        refuelingModel.setRefuelingPaymentMethod(refuelingPaymentMethod);
        refuelingModel.setRefuelingNote(refuelingNote);

        // Save RefuelingModel to Firebase
        saveRefuelingToFirebase(refuelingModel);
    }

    // Method to save refueling object to Firebase
    void saveRefuelingToFirebase(RefuelingModel refuelingModel){
        DocumentReference documentReference;
        if(isEditMode){
            // If in edit mode, update the existing vehicle document
            documentReference = Utility.getCollectionReferenceForRefueling().document(docId);
        }else{
            // If not in edit mode, create a new vehicle document
            documentReference = Utility.getCollectionReferenceForRefueling().document();
        }

        // Set the document to the refueling object
        documentReference.set(refuelingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //vehicle is added
                    Utility.showToast(AddEditDeleteRefuelingActivity.this,"Refueling added successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteRefuelingActivity.this,"Failed while adding refueling");
                }
            }
        });

    }

    // Method to delete a refueling from Firebase
    void deleteRefuelingFromFirebase(){

        // Get a reference to the document to be deleted
        DocumentReference documentReference = Utility.getCollectionReferenceForRefueling().document(docId);

        // Delete the document
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // If the deletion is successful, show a success message and finish the activity
                    Utility.showToast(AddEditDeleteRefuelingActivity.this,"Refueling deleted successfully");
                    finish();
                }else{
                    // If the deletion fails, show an error message
                    Utility.showToast(AddEditDeleteRefuelingActivity.this,"Failed while deleting refueling");
                }
            }
        });
    }

    // Override onCreateOptionsMenu to inflate custom menu and set visibility of save and delete buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu and handle visibility of menu items based on edit mode
        getMenuInflater().inflate(R.menu.save_refueling_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.saveRefuelingBtn);
        MenuItem deleteItem = menu.findItem(R.id.deleteRefuelingBtn);

        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        // Set visibility of save and delete buttons based on the mode
        if(isEditMode){
            getSupportActionBar().setTitle("Edit your Refueling");
            saveItem.setVisible(true);
            deleteItem.setVisible(true);
        }
        return true;
    }

    // Override onOptionsItemSelected to handle button click events
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Perform different actions based on the clicked item's ID
        switch (id) {
            case R.id.saveRefuelingBtn:
                // Handle save button click
                saveRefueling();
                return true;
            case R.id.deleteRefuelingBtn:
                // Handle delete icon click
                deleteRefuelingFromFirebase();
                return true;
            case android.R.id.home:
                // Handle home (back) button click
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Method to show date picker dialog
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
                refuelingDatePickerTI.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // Method to show time picker dialog
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
                refuelingTimePickerTI.setText(timeFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    // Method to automatically calculate total cost or fuel litres
    private void autoCalculate() {
        String value1 = refuelingPricePerLitreTI.getText().toString();
        String value2 = refuelingFuelLitresTI.getText().toString();
        String value3 = refuelingTotalCostTI.getText().toString();

        if (!value1.isEmpty() && !value2.isEmpty()) {
            float result = Float.parseFloat(value1) * Float.parseFloat(value2);
            refuelingTotalCostTI.setText(String.valueOf(roundToThreeDecimals(result)));
        } else if (!value1.isEmpty() && !value3.isEmpty()) {
            float value1Float = Float.parseFloat(value1);
            float value3Float = Float.parseFloat(value3);
            if (value3Float != 0) {
                float result = value3Float / value1Float;
                refuelingFuelLitresTI.setText(String.valueOf(roundToThreeDecimals(result)));
            } else {
                refuelingFuelLitresTI.setText("");
            }
        } else if (!value2.isEmpty() && !value3.isEmpty()) {
            float value2Float = Float.parseFloat(value2);
            float value3Float = Float.parseFloat(value3);
            if (value2Float != 0) {
                float result = value3Float / value2Float;
                refuelingTotalCostTI.setText(String.valueOf(roundToThreeDecimals(result)));
            } else {
                refuelingTotalCostTI.setText("");
            }
        }
    }

    // Method to round a float number to three decimals
    public static float roundToThreeDecimals(float number) {
        return (float) (Math.round(number * 1000) / 1000.0);
    }
}
