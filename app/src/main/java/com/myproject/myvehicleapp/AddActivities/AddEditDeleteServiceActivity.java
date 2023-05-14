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
import com.myproject.myvehicleapp.Models.ServiceModel;
import com.myproject.myvehicleapp.MoreMenuActivities.PaymentMethodActivity;

import com.myproject.myvehicleapp.MoreMenuActivities.TypeOfServiceActivity;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddEditDeleteServiceActivity extends AppCompatActivity {

    // Define all the UI elements
    TextInputEditText serviceDatePickerTI;
    TextInputEditText serviceTimePickerTI;
    TextInputEditText serviceOdometerTI;
    TextInputEditText serviceTypeOfServiceTI;
    TextInputEditText serviceTotalCostTI;
    TextInputEditText servicePaymentMethodTI;
    TextInputEditText serviceNoteTI;

    // Define all the data elements
    Timestamp serviceTimeStamp;
    Integer serviceOdometer;
    String serviceTypeOfService;
    Float serviceTotalCost;
    String servicePaymentMethod;
    String serviceNote;
    String docId;

    boolean isEditMode = false;
    Toolbar toolbarService;

    // Define the ActivityResultLaunchers for opening the PaymentMethodActivity and FuelActivity
    private ActivityResultLauncher<Intent> paymentMethodActivityResultLauncher;
    private ActivityResultLauncher<Intent> serviceActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_service);

        // Find all the UI elements by their IDs

        toolbarService = findViewById(R.id.toolbar_service);
        setSupportActionBar(toolbarService);
        getSupportActionBar().setTitle("Add Service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        serviceDatePickerTI = findViewById(R.id.serviceDatePickerTI);
        serviceTimePickerTI = findViewById(R.id.serviceTimePickerTI);
        serviceOdometerTI = findViewById(R.id.serviceOdometerTI);
        serviceTypeOfServiceTI = findViewById(R.id.serviceTypeOfServiceTI);
        serviceTotalCostTI = findViewById(R.id.serviceTotalCostTI);
        servicePaymentMethodTI = findViewById(R.id.servicePaymentMethodTI);
        serviceNoteTI = findViewById(R.id.serviceNoteTI);

        //receive data
        serviceTimeStamp = getIntent().getParcelableExtra("serviceTimeStamp");
        serviceOdometer = getIntent().getIntExtra("serviceOdometer",0);
        serviceTypeOfService = getIntent().getStringExtra("serviceTypeOfService");
        serviceTotalCost = getIntent().getFloatExtra("serviceTotalCost",0);
        servicePaymentMethod = getIntent().getStringExtra("servicePaymentMethod");
        serviceNote = getIntent().getStringExtra("serviceNote");
        docId = getIntent().getStringExtra("docId");

        // Check if we are in edit mode
        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        // Set the UI elements to display the retrieved data
        if (serviceTimeStamp != null) {
            Date date = serviceTimeStamp.toDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Set the text of the views to the formatted date and time strings
            serviceDatePickerTI.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date));
            serviceTimePickerTI.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));
        }

        serviceOdometerTI.setText(String.valueOf(serviceOdometer));
        serviceTypeOfServiceTI.setText(serviceTypeOfService);
        serviceTotalCostTI.setText(String.format("%.2f", serviceTotalCost));
        servicePaymentMethodTI.setText(servicePaymentMethod);
        serviceNoteTI.setText(serviceNote);

        // Register activity result launchers for the payment method and fuel type activities
        paymentMethodActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String selectedPaymentMethod = result.getData().getStringExtra("selectedPaymentMethod");
                        servicePaymentMethodTI.setText(selectedPaymentMethod);
                    }
                });

        serviceActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String selectedServiceType = result.getData().getStringExtra("selectedTypeOfService");
                        serviceTypeOfServiceTI.setText(selectedServiceType);
                    }
                });

        // Attach openPaymentMethodActivity and openFuelTypeActivity to the corresponding UI elements
        servicePaymentMethodTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPaymentMethodActivity();
            }
        });

        serviceTypeOfServiceTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openServiceTypeActivity();
            }
        });
    }

    // Method to open PaymentMethodActivity
    private void openPaymentMethodActivity() {
        Intent intent = new Intent(this, PaymentMethodActivity.class);
        intent.putExtra("selectMode", true);
        paymentMethodActivityResultLauncher.launch(intent);
    }

    // Method to open TypeOfServiceActivity
    private void openServiceTypeActivity() {
        Intent intent = new Intent(this, TypeOfServiceActivity.class);
        intent.putExtra("selectMode", true);
        serviceActivityResultLauncher.launch(intent);
    }

    // Method to save service data
    void saveService() {
        // Getting the entered values

        // Retrieve the refueling date from the user input
        String serviceDate = Objects.requireNonNull(serviceDatePickerTI.getText()).toString();
        // Retrieve the refueling time from the user input
        String serviceTime = Objects.requireNonNull(serviceTimePickerTI.getText()).toString();

        // Retrieve the refueling odometer reading from the user input
        String serviceOdometerStr = serviceOdometerTI.getText().toString();
        // If the user has not entered a value for the refueling odometer reading, display an error message
        if (serviceOdometerStr.isEmpty()) {
            serviceOdometerTI.setError("Odometer is required");
            return;
        }

        // Parse the string value of the refueling odometer reading to an integer
        int serviceOdometer;
        try {
            serviceOdometer = Integer.parseInt(serviceOdometerStr);
        } catch (NumberFormatException e) {
            // If the parsing fails, display an error message
            serviceOdometerTI.setError("Invalid odometer value");
            return;
        }

        // Retrieve the service type from the user input
        String serviceTypeOfService = serviceTypeOfServiceTI.getText().toString();

        String serviceTotalCostStr = serviceTotalCostTI.getText().toString();
        if (serviceTotalCostStr.isEmpty()) {
            serviceTotalCostTI.setError("Total cost is required");
            return;
        }

        // Parse the string value of the refueling total cost to a float
        float serviceTotalCost;
        try {
            serviceTotalCost = Float.parseFloat(serviceTotalCostStr);
        } catch (NumberFormatException e) {
            // If the parsing fails, display an error message
            serviceTotalCostTI.setError("Invalid total cost value");
            return;
        }

        String servicePaymentMethod = servicePaymentMethodTI.getText().toString();
        String serviceNote = serviceNoteTI.getText().toString();

        // Create a Date object from the refuelingDate and refuelingTime strings
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date dateTime = null;
        try {
            dateTime = dateTimeFormat.parse(serviceDate + " " + serviceTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Convert the Date object to a Timestamp object
        Timestamp timestamp = new Timestamp(new Date(dateTime.getTime()));

        // Create serviceModel object and set its properties
        ServiceModel serviceModel = new ServiceModel();

        serviceModel.setRecyclerTitle("Service");
        serviceModel.setServiceTimeStamp(timestamp);
        serviceModel.setServiceOdometer(serviceOdometer);
        serviceModel.setServiceTypeOfService(serviceTypeOfService);
        serviceModel.setServiceTotalCost(serviceTotalCost);
        serviceModel.setServicePaymentMethod(servicePaymentMethod);
        serviceModel.setServiceNote(serviceNote);

        // Save serviceModel to Firebase
        saveServiceToFirebase(serviceModel);
    }

    void saveServiceToFirebase(ServiceModel serviceModel){
        DocumentReference documentReference;
        if(isEditMode){
            //update the vehicle
            documentReference = Utility.getCollectionReferenceForService().document(docId);
        }else{
            //create new vehicle
            documentReference = Utility.getCollectionReferenceForService().document();
        }

        documentReference.set(serviceModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //vehicle is added
                    Utility.showToast(AddEditDeleteServiceActivity.this,"Service added successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteServiceActivity.this,"Failed while adding service");
                }
            }
        });
    }

    // Method to delete service from Firebase
    void deleteServiceFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForService().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(AddEditDeleteServiceActivity.this,"Service deleted successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteServiceActivity.this,"Failed while deleting Service");
                }
            }
        });
    }

    // Override onCreateOptionsMenu to inflate custom menu and set visibility of save and delete buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu and handle visibility of menu items based on edit mode
        getMenuInflater().inflate(R.menu.save_service_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.saveServiceBtn);
        MenuItem deleteItem = menu.findItem(R.id.deleteServiceBtn);

        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        // Set visibility of save and delete buttons based on the mode
        if(isEditMode){
            getSupportActionBar().setTitle("Edit your service");
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
            case R.id.saveServiceBtn:
                // Handle save button click
                saveService();
                return true;
            case R.id.deleteServiceBtn:
                // Handle delete icon click
                deleteServiceFromFirebase();
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
                serviceDatePickerTI.setText(dateFormat.format(calendar.getTime()));
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
                serviceTimePickerTI.setText(timeFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }
}