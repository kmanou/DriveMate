package com.myproject.myvehicleapp.AddActivities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.myproject.myvehicleapp.Models.TypeOfExpenseModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Tools;
import com.myproject.myvehicleapp.Utilities.Utility;


public class AddEditDeleteTypeOfExpenseActivity extends AppCompatActivity {

    TextInputEditText typeOfExpenseNameTI;
    String typeOfExpenseName, docId;
    boolean isEditMode = false;
    Toolbar toolbarTypeOfExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete_type_of_expense);

        toolbarTypeOfExpense = findViewById(R.id.toolbar_type_of_expense);
        setSupportActionBar(toolbarTypeOfExpense);

        getSupportActionBar().setTitle("Add Type of Expense");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.red_800);

        typeOfExpenseNameTI = findViewById(R.id.typeOfExpenseNameTI);

        //receive data
        typeOfExpenseName = getIntent().getStringExtra("typeOfExpense");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        // Set the type of expense name to the TextInputEditText
        typeOfExpenseNameTI.setText(typeOfExpenseName);
    }

    // Method to save type of expense
    void saveTypeOfExpense(){
        String typeOfExpenseName = typeOfExpenseNameTI.getText().toString();

        if(typeOfExpenseName==null || typeOfExpenseName.isEmpty() ){
            // Display an error if the type of expense name is empty
            typeOfExpenseNameTI.setError("Type of Expense is required");
            return;
        }

        // Create TypeOfExpenseModel object and set its properties
        TypeOfExpenseModel typeOfExpenseModel = new TypeOfExpenseModel();
        typeOfExpenseModel.setTypeOfExpense(typeOfExpenseName);

        // Save typeOfExpenseModel to Firebase
        saveTypeOfExpenseToFirebase(typeOfExpenseModel);
    }

    void saveTypeOfExpenseToFirebase(TypeOfExpenseModel typeOfExpenseModel){
        DocumentReference documentReference;
        if(isEditMode){
            // Update existing type of expense
            documentReference = Utility.getCollectionReferenceForTypeOfExpense().document(docId);
        }else{
            // Create new type of expense
            documentReference = Utility.getCollectionReferenceForTypeOfExpense().document();
        }

        documentReference.set(typeOfExpenseModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // Type of expense is added or updated successfully
                    Utility.showToast(AddEditDeleteTypeOfExpenseActivity.this,"Type of Expense added successfully");
                    finish();
                }else{
                    // Failed to add or update the type of expense
                    Utility.showToast(AddEditDeleteTypeOfExpenseActivity.this,"Failed while adding Type of Expense");
                }
            }
        });
    }

    // Method to delete type of expense from Firebase
    void deleteTypeOfExpenseFromFirebase(){
        DocumentReference documentReference = Utility.getCollectionReferenceForTypeOfExpense().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(AddEditDeleteTypeOfExpenseActivity.this,"Type of Expense deleted successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteTypeOfExpenseActivity.this,"Failed while deleting Type of Expense");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu and handle visibility of save and delete buttons based on edit mode
        getMenuInflater().inflate(R.menu.save_type_of_expense_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.saveTypeOfExpenseBtn);
        MenuItem deleteItem = menu.findItem(R.id.deleteTypeOfExpenseBtn);

        saveItem.setVisible(true);
        deleteItem.setVisible(false);

        // If in edit mode, show the delete button and set the activity title
        if(isEditMode){
            getSupportActionBar().setTitle("Edit your Type of Expense");
            saveItem.setVisible(true);
            deleteItem.setVisible(true);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.saveTypeOfExpenseBtn:
                // Handle save button click
                saveTypeOfExpense();
                return true;
            case R.id.deleteTypeOfExpenseBtn:
                // Handle delete icon click
                deleteTypeOfExpenseFromFirebase();
                return true;
            case android.R.id.home:
                // Handle home (back) button click
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}