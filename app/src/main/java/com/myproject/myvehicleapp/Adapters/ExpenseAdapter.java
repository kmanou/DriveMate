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
import com.myproject.myvehicleapp.Utlities.Utility;

public class ExpenseAdapter extends FirestoreRecyclerAdapter<ExpenseModel, ExpenseAdapter.ExpenseViewHolder> {
    Context context;

    public ExpenseAdapter(@NonNull FirestoreRecyclerOptions<ExpenseModel> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull ExpenseModel expenseModel) {

        holder.expenseDateTime.setText(Utility.timestampToString(expenseModel.expenseTimeStamp));
        holder.expenseOdometer.setText(String.valueOf(expenseModel.expenseOdometer));
        holder.expenseTypeOfExpense.setText(String.valueOf(expenseModel.expenseTypeOfExpense));
        holder.expenseTotalPrice.setText(String.valueOf(expenseModel.expenseTotalCost));

        holder.editBtn.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditDeleteExpenseActivity.class);

            intent.putExtra("expenseTimeStamp",expenseModel.expenseTimeStamp);
            intent.putExtra("expenseOdometer",expenseModel.expenseOdometer);
            intent.putExtra("expenseTypeOfExpense",expenseModel.expenseTypeOfExpense);
            intent.putExtra("expenseTotalCost",expenseModel.expenseTotalCost);
            intent.putExtra("expensePaymentMethod",expenseModel.expensePaymentMethod);
            intent.putExtra("expenseNote",expenseModel.expenseNote);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);

            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_expense_item,parent,false);
        return new ExpenseViewHolder(view);
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder{
        TextView recyclerTypeTitle;
        TextView expenseDateTime;
        TextView expenseOdometer;
        TextView expenseTypeOfExpense;
        TextView expenseTotalPrice;
        ImageView editBtn;

        public void toggleVisibility(View view) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            LinearLayout linearLayout, revealExpenseCard;
            linearLayout = itemView.findViewById(R.id.expanded_expense_menu);
            revealExpenseCard = itemView.findViewById(R.id.revealExpense);
            linearLayout.setVisibility(View.GONE);

            editBtn = itemView.findViewById(R.id.expense_edit_btn);

            recyclerTypeTitle = itemView.findViewById(R.id.refuelingRecyclerTypeTitleTVItem);
            expenseDateTime = itemView.findViewById(R.id.expenseDateTVItem);
            expenseOdometer = itemView.findViewById(R.id.expenseOdometerCounterTVItem);
            expenseTypeOfExpense = itemView.findViewById(R.id.expenseTypeOfExpenseTVItem);
            expenseTotalPrice = itemView.findViewById(R.id.expenseTotalPriceTVItem);

            revealExpenseCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleVisibility(linearLayout);
                    notifyDataSetChanged();
                }
            });
        }
    }
}