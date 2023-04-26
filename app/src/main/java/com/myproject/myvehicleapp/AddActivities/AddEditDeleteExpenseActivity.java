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
import com.myproject.myvehicleapp.Models.ExpenseModel;
import com.myproject.myvehicleapp.MoreMenuActivities.PaymentMethodActivity;
import com.myproject.myvehicleapp.MoreMenuActivities.TypeOfExpenseActivity;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utlities.Tools;
import com.myproject.myvehicleapp.Utlities.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddEditDeleteExpenseActivity extends AppCompatActivity {

    // Define all the UI elements
    TextInputEditText expenseDatePickerTI;
    TextInputEditText expenseTimePickerTI;
    TextInputEditText expenseOdometerTI;
    TextInputEditText expenseTypeOfExpenseTI;
    TextInputEditText expenseTotalCostTI;
    TextInputEditText expensePaymentMethodTI;
    TextInputEditText expenseNoteTI;

    // Define all the data elements
    Timestamp expenseTimeStamp;
    Integer expenseOdometer;
    String expenseTypeOfExpense;
    Float expenseTotalCost;
    String expensePaymentMethod;
    String expenseNote;
    String docId;

    boolean isEditMode = false;
    Toolbar toolbarExpense;

    // Define the ActivityResultLaunchers for opening the PaymentMethodActivity and FuelActivity
    private ActivityResultLauncher<Intent> paymentMethodActivityResultLauncher;
    private ActivityResultLauncher<Intent> expenseActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_expense);

        // Find all the UI elements by their IDs

        toolbarExpense = findViewById(R.id.toolbar_expense);
        setSupportActionBar(toolbarExpense);
        getSupportActionBar().setTitle("Add Expense");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        expenseDatePickerTI = findViewById(R.id.expenseDatePickerTI);
        expenseTimePickerTI = findViewById(R.id.expenseTimePickerTI);
        expenseOdometerTI = findViewById(R.id.expenseOdometerTI);
        expenseTypeOfExpenseTI = findViewById(R.id.expenseTypeOfExpenseTI);
        expenseTotalCostTI = findViewById(R.id.expenseTotalCostTI);
        expensePaymentMethodTI = findViewById(R.id.expensePaymentMethodTI);
        expenseNoteTI = findViewById(R.id.expenseNoteTI);

        //receive data
        expenseTimeStamp = getIntent().getParcelableExtra("expenseTimeStamp");
        expenseOdometer = getIntent().getIntExtra("expenseOdometer", 0);
        expenseTypeOfExpense = getIntent().getStringExtra("expenseTypeOfExpense");
        expenseTotalCost = getIntent().getFloatExtra("expenseTotalCost", 0);
        expensePaymentMethod = getIntent().getStringExtra("expensePaymentMethod");
        expenseNote = getIntent().getStringExtra("expenseNote");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        // Set the UI elements to display the retrieved data
        if (expenseTimeStamp != null) {
            Date date = expenseTimeStamp.toDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Set the text of the views to the formatted date and time strings
            expenseDatePickerTI.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date));
            expenseTimePickerTI.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));
        }

        expenseOdometerTI.setText(String.valueOf(expenseOdometer));
        expenseTypeOfExpenseTI.setText(expenseTypeOfExpense);
        expenseTotalCostTI.setText(String.format("%.2f", expenseTotalCost));
        expensePaymentMethodTI.setText(expensePaymentMethod);
        expenseNoteTI.setText(expenseNote);

        // Register activity result launchers for the payment method and fuel type activities
        paymentMethodActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String selectedPaymentMethod = result.getData().getStringExtra("selectedPaymentMethod");
                        expensePaymentMethodTI.setText(selectedPaymentMethod);
                    }
                });

        expenseActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String selectedExpenseType = result.getData().getStringExtra("selectedTypeOfExpense");
                        expenseTypeOfExpenseTI.setText(selectedExpenseType);
                    }
                });

        // Attach openPaymentMethodActivity and openFuelTypeActivity to the corresponding UI elements
        expensePaymentMethodTI.setOnClickListener(v -> openPaymentMethodActivity());
        expenseTypeOfExpenseTI.setOnClickListener(v -> openExpenseTypeActivity());
    }

    private void openPaymentMethodActivity() {
        Intent intent = new Intent(this, PaymentMethodActivity.class);
        intent.putExtra("selectMode", true);
        paymentMethodActivityResultLauncher.launch(intent);
    }

    private void openExpenseTypeActivity() {
        Intent intent = new Intent(this, TypeOfExpenseActivity.class);
        intent.putExtra("selectMode", true);
        expenseActivityResultLauncher.launch(intent);
    }

    void saveExpense() {
        String expenseDate = Objects.requireNonNull(expenseDatePickerTI.getText()).toString();
        String expenseTime = Objects.requireNonNull(expenseTimePickerTI.getText()).toString();

        String expenseOdometerStr = expenseOdometerTI.getText().toString();
        if (expenseOdometerStr.isEmpty()) {
            expenseOdometerTI.setError("Odometer is required");
            return;
        }

        Integer expenseOdometer;
        try {
            expenseOdometer = Integer.parseInt(expenseOdometerStr);
        } catch (NumberFormatException e) {
            expenseOdometerTI.setError("Invalid odometer value");
            return;
        }

        String expenseTypeOfExpense = expenseTypeOfExpenseTI.getText().toString();

        String expenseTotalCostStr = expenseTotalCostTI.getText().toString();
        if (expenseTotalCostStr.isEmpty()) {
            expenseTotalCostTI.setError("Total cost is required");
            return;
        }

        Float expenseTotalCost;
        try {
            expenseTotalCost = Float.parseFloat(expenseTotalCostStr);
        } catch (NumberFormatException e) {
            expenseTotalCostTI.setError("Invalid total cost value");
            return;
        }

        String expensePaymentMethod = expensePaymentMethodTI.getText().toString();

        String expenseNote = expenseNoteTI.getText().toString();


        // Create a Date object from the refuelingDate and refuelingTime strings
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date dateTime = null;
        try {
            dateTime = dateTimeFormat.parse(expenseDate + " " + expenseTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Convert the Date object to a Timestamp object
        Timestamp timestamp = new Timestamp(new Date(dateTime.getTime()));

        // Create expenseModel object and set its properties
        ExpenseModel expenseModel = new ExpenseModel();

        expenseModel.setRecyclerTitle("Expense");
        expenseModel.setExpenseTimeStamp(timestamp);
        expenseModel.setExpenseOdometer(expenseOdometer);
        expenseModel.setExpenseTypeOfExpense(expenseTypeOfExpense);
        expenseModel.setExpenseTotalCost(expenseTotalCost);
        expenseModel.setExpensePaymentMethod(expensePaymentMethod);
        expenseModel.setExpenseNote(expenseNote);

        // Save expenseModel to Firebase
        saveExpenseToFirebase(expenseModel);
    }

    void saveExpenseToFirebase(ExpenseModel expenseModel) {
        DocumentReference documentReference;
        if (isEditMode) {
            //update the vehicle
            documentReference = Utility.getCollectionReferenceForExpense().document(docId);
        } else {
            //create new vehicle
            documentReference = Utility.getCollectionReferenceForExpense().document();
        }

        documentReference.set(expenseModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //vehicle is added
                    Utility.showToast(AddEditDeleteExpenseActivity.this, "Expense added successfully");
                    finish();
                } else {
                    Utility.showToast(AddEditDeleteExpenseActivity.this, "Failed while adding expense");
                }
            }
        });
    }

    void deleteExpenseFromFirebase() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForExpense().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //note is deleted
                    Utility.showToast(AddEditDeleteExpenseActivity.this, "Expense deleted successfully");
                    finish();
                } else {
                    Utility.showToast(AddEditDeleteExpenseActivity.this, "Failed while deleting Expense");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_expense_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.saveExpenseBtn);
        MenuItem deleteItem = menu.findItem(R.id.deleteExpenseBtn);

        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        if (isEditMode) {
            getSupportActionBar().setTitle("Edit your expense");
            saveItem.setVisible(true);
            deleteItem.setVisible(true);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.saveExpenseBtn:
                // Handle edit icon click
                saveExpense();
                return true;
            case R.id.deleteExpenseBtn:
                // Handle delete icon click
                deleteExpenseFromFirebase();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
                expenseDatePickerTI.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

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
                expenseTimePickerTI.setText(timeFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }
}