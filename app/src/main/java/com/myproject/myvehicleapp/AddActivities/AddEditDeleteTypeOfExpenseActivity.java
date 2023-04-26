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
import com.myproject.myvehicleapp.Utlities.Tools;
import com.myproject.myvehicleapp.Utlities.Utility;


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

        typeOfExpenseNameTI.setText(typeOfExpenseName);
    }

    void saveTypeOfExpense(){
        String typeOfExpenseName = typeOfExpenseNameTI.getText().toString();

        if(typeOfExpenseName==null || typeOfExpenseName.isEmpty() ){
            typeOfExpenseNameTI.setError("Type of Expense is required");
            return;
        }

        TypeOfExpenseModel typeOfExpenseModel = new TypeOfExpenseModel();
        typeOfExpenseModel.setTypeOfExpense(typeOfExpenseName);

        saveTypeOfExpenseToFirebase(typeOfExpenseModel);
    }

    void saveTypeOfExpenseToFirebase(TypeOfExpenseModel typeOfExpenseModel){
        DocumentReference documentReference;
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForTypeOfExpense().document(docId);
        }else{
            //create new note
            documentReference = Utility.getCollectionReferenceForTypeOfExpense().document();
        }

        documentReference.set(typeOfExpenseModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(AddEditDeleteTypeOfExpenseActivity.this,"Type of Expense added successfully");
                    finish();
                }else{
                    Utility.showToast(AddEditDeleteTypeOfExpenseActivity.this,"Failed while adding Type of Expense");
                }
            }
        });
    }

    void deleteTypeOfExpenseFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForTypeOfExpense().document(docId);
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
        getMenuInflater().inflate(R.menu.save_type_of_expense_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.saveTypeOfExpenseBtn);
        MenuItem deleteItem = menu.findItem(R.id.deleteTypeOfExpenseBtn);

        saveItem.setVisible(true);
        deleteItem.setVisible(false);

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
                // Handle edit icon click
                saveTypeOfExpense();
                return true;
            case R.id.deleteTypeOfExpenseBtn:
                // Handle delete icon click
                deleteTypeOfExpenseFromFirebase();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}