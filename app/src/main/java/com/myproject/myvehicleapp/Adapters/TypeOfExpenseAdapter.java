package com.myproject.myvehicleapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteTypeOfExpenseActivity;
import com.myproject.myvehicleapp.Models.TypeOfExpenseModel;
import com.myproject.myvehicleapp.Models.TypeOfExpenseModel;
import com.myproject.myvehicleapp.Models.TypeOfExpenseModel;
import com.myproject.myvehicleapp.R;

// This class is an adapter for the Firestore recycler view that displays types of expenses.
public class TypeOfExpenseAdapter extends FirestoreRecyclerAdapter<TypeOfExpenseModel, TypeOfExpenseAdapter.TypeOfExpenseViewHolder> {

    // The current context.
    Context context;
    // Listener for type of expense item clicks.
    private OnTypeOfExpenseMethodItemClickListener onTypeOfExpenseMethodItemClickListener;
    // A flag to indicate if the adapter is in select mode.
    private boolean selectMode;

    // Interface for handling clicks on type of expense items.
    public interface OnTypeOfExpenseMethodItemClickListener{
        void onTypeOfExpenseMethodItemClickListener(TypeOfExpenseModel typeOfExpenseModel, String docId);
    }

    // Constructor for the adapter.
    public TypeOfExpenseAdapter(@NonNull FirestoreRecyclerOptions<TypeOfExpenseModel> options, Context context,
                                TypeOfExpenseAdapter.OnTypeOfExpenseMethodItemClickListener onTypeOfExpenseMethodItemClickListener,
                                boolean selectMode) {
        super(options);
        this.context = context;
        this.onTypeOfExpenseMethodItemClickListener = onTypeOfExpenseMethodItemClickListener;
        this.selectMode = selectMode;
    }

    // This method binds the view holder with data.
    @Override
    protected void onBindViewHolder(@NonNull TypeOfExpenseAdapter.TypeOfExpenseViewHolder holder, int position,
                                    @NonNull TypeOfExpenseModel typeOfExpenseModel) {
        // Set the text of the expense type.
        holder.typeOfExpense.setText(typeOfExpenseModel.typeOfExpense);

        // Set an onClickListener for the item view.
        holder.itemView.setOnClickListener(v -> {
            // Get the document ID of the current type of expense.
            String docId = this.getSnapshots().getSnapshot(position).getId();
            // Check if the adapter is in select mode.
            if (selectMode) {
                // If yes, return the selected type of expense to the calling activity.
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedTypeOfExpense", typeOfExpenseModel.typeOfExpense);
                ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                ((Activity) context).finish();
            } else {
                // If not, handle the click event normally.
                onTypeOfExpenseMethodItemClickListener.onTypeOfExpenseMethodItemClickListener(typeOfExpenseModel, docId);
            }
        });

        // Set an onClickListener for the edit button.
        holder.editTypeOfExpenseButton.setOnClickListener((v)->{
            // When clicked, start the AddEditDeleteTypeOfExpenseActivity with the current type of expense.
            Intent intent = new Intent(context, AddEditDeleteTypeOfExpenseActivity.class);
            intent.putExtra("typeOfExpense", typeOfExpenseModel.typeOfExpense);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    // This method creates a new view holder.
    @NonNull
    @Override
    public TypeOfExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the type of expense item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_type_of_expense_item, parent,false);
        // Return a new view holder with the inflated view.
        return new TypeOfExpenseViewHolder(view);
    }

    // This class represents a view holder for the type of expense item.
    class TypeOfExpenseViewHolder extends RecyclerView.ViewHolder{
        // The TextView displaying the type of expense.
        TextView typeOfExpense;
        // The edit button for the type of expense.
        Button editTypeOfExpenseButton;

        // Constructor for the view holder.
        public TypeOfExpenseViewHolder(@NonNull View itemView){
            super(itemView);
            // Initialize the TextView that will display the type of expense.
            typeOfExpense = itemView.findViewById(R.id.type_of_expense_name);
            // Initialize the Button that will trigger the editing of the type of expense.
            editTypeOfExpenseButton = itemView.findViewById(R.id.edit_type_of_expense_button);
        }
    }
}

