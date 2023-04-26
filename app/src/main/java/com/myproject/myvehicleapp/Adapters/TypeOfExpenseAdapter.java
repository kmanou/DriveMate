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

public class TypeOfExpenseAdapter extends FirestoreRecyclerAdapter<TypeOfExpenseModel, 
        TypeOfExpenseAdapter.TypeOfExpenseViewHolder> {
    Context context;
    private OnTypeOfExpenseMethodItemClickListener onTypeOfExpenseMethodItemClickListener;
    private boolean selectMode;

    public interface OnTypeOfExpenseMethodItemClickListener{
        void onTypeOfExpenseMethodItemClickListener(TypeOfExpenseModel typeOfExpenseModel, String docId);
    }

    public TypeOfExpenseAdapter(@NonNull FirestoreRecyclerOptions<TypeOfExpenseModel> options, Context context,
                                TypeOfExpenseAdapter.OnTypeOfExpenseMethodItemClickListener onTypeOfExpenseMethodItemClickListener,
                                boolean selectMode) {
        super(options);
        this.context = context;
        this.onTypeOfExpenseMethodItemClickListener = onTypeOfExpenseMethodItemClickListener;
        this.selectMode = selectMode;
    }

    @Override
    protected void onBindViewHolder(@NonNull TypeOfExpenseAdapter.TypeOfExpenseViewHolder holder, int position,
                                    @NonNull TypeOfExpenseModel typeOfExpenseModel) {
        holder.typeOfExpense.setText(typeOfExpenseModel.typeOfExpense);

        holder.itemView.setOnClickListener(v -> {
            String docId = this.getSnapshots().getSnapshot(position).getId();
            if (selectMode) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedTypeOfExpense", typeOfExpenseModel.typeOfExpense);
                ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                ((Activity) context).finish();
            } else {
                onTypeOfExpenseMethodItemClickListener.onTypeOfExpenseMethodItemClickListener(typeOfExpenseModel, docId);
            }
        });

        holder.editTypeOfExpenseButton.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditDeleteTypeOfExpenseActivity.class);
            intent.putExtra("typeOfExpense", typeOfExpenseModel.typeOfExpense);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public TypeOfExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_type_of_expense_item, parent,false);
        return new TypeOfExpenseViewHolder(view);
    }

    class TypeOfExpenseViewHolder extends RecyclerView.ViewHolder{
        TextView typeOfExpense;
        Button editTypeOfExpenseButton;
        
        public TypeOfExpenseViewHolder(@NonNull View itemView){
            super(itemView);
            typeOfExpense = itemView.findViewById(R.id.type_of_expense_name);
            editTypeOfExpenseButton = itemView.findViewById(R.id.edit_type_of_expense_button);
        }
    }

}
