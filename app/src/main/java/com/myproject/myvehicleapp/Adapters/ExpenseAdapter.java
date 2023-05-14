package com.myproject.myvehicleapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteExpenseActivity;
import com.myproject.myvehicleapp.Models.ExpenseModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Utility;

// This class handles the display of expenses in a RecyclerView
public class ExpenseAdapter extends FirestoreRecyclerAdapter<ExpenseModel, ExpenseAdapter.ExpenseViewHolder> {
    Context context;

    // Constructor takes Firestore options and a Context
    public ExpenseAdapter(@NonNull FirestoreRecyclerOptions<ExpenseModel> options, Context context) {
        super(options);
        this.context = context;
    }

    // onBindViewHolder is called by RecyclerView to display the data at the specified position
    @Override
    protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull ExpenseModel expenseModel) {
        // Setting expense details to their respective views
        holder.expenseDateTime.setText(Utility.timestampToString(expenseModel.expenseTimeStamp));
        holder.expenseOdometer.setText(String.valueOf(expenseModel.expenseOdometer));
        holder.expenseTypeOfExpense.setText(String.valueOf(expenseModel.expenseTypeOfExpense));
        holder.expenseTotalPrice.setText(String.valueOf(expenseModel.expenseTotalCost));

        // Setting on click listener for the edit button
        holder.editBtn.setOnClickListener((v)->{
            // Creating an intent for AddEditDeleteExpenseActivity and adding extras
            Intent intent = new Intent(context, AddEditDeleteExpenseActivity.class);
            intent.putExtra("expenseTimeStamp",expenseModel.expenseTimeStamp);
            intent.putExtra("expenseOdometer",expenseModel.expenseOdometer);
            intent.putExtra("expenseTypeOfExpense",expenseModel.expenseTypeOfExpense);
            intent.putExtra("expenseTotalCost",expenseModel.expenseTotalCost);
            intent.putExtra("expensePaymentMethod",expenseModel.expensePaymentMethod);
            intent.putExtra("expenseNote",expenseModel.expenseNote);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);

            // Starting the new Activity
            context.startActivity(intent);
        });
    }

    // This method is called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_expense_item,parent,false);
        return new ExpenseViewHolder(view);
    }

    // This class provides a reference to the views for each data item
    class ExpenseViewHolder extends RecyclerView.ViewHolder{
        TextView recyclerTypeTitle;
        TextView expenseDateTime;
        TextView expenseOdometer;
        TextView expenseTypeOfExpense;
        TextView expenseTotalPrice;
        ImageView editBtn;

        // Method to toggle the visibility of a View
        public void toggleVisibility(View view) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }

        // ViewHolder's constructor takes the root view of the item
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            LinearLayout linearLayout, revealExpenseCard;
            linearLayout = itemView.findViewById(R.id.expanded_expense_menu);
            revealExpenseCard = itemView.findViewById(R.id.revealExpense);
            linearLayout.setVisibility(View.GONE);

            // Initializing views
            editBtn = itemView.findViewById(R.id.expense_edit_btn);
            recyclerTypeTitle = itemView.findViewById(R.id.refuelingRecyclerTypeTitleTVItem);
            expenseDateTime = itemView.findViewById(R.id.expenseDateTVItem);
            expenseOdometer = itemView.findViewById(R.id.expenseOdometerCounterTVItem);
            expenseTypeOfExpense = itemView.findViewById(R.id.expenseTypeOfExpenseTVItem);
            expenseTotalPrice = itemView.findViewById(R.id.expenseTotalPriceTVItem);

            // Setting
            // Setting onClickListener to the card which when clicked, reveals or hides the expanded menu
            revealExpenseCard.setOnClickListener(view -> {
                // Toggle the visibility of the expanded menu
                toggleVisibility(linearLayout);
                // Notify the adapter that underlying data has been changed and any View
                // reflecting the data set should refresh itself
                notifyDataSetChanged();
            });
        }
    }
}
