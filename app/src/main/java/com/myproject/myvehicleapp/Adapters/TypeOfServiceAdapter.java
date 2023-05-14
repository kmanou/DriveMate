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
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteTypeOfServiceActivity;
import com.myproject.myvehicleapp.Models.TypeOfServiceModel;
import com.myproject.myvehicleapp.R;

// Custom adapter class for FirestoreRecyclerAdapter with TypeOfServiceModel
public class TypeOfServiceAdapter extends FirestoreRecyclerAdapter<TypeOfServiceModel, TypeOfServiceAdapter.TypeOfServiceViewHolder> {
    Context context;
    private OnTypeOfServiceMethodItemClickListener onTypeOfServiceMethodItemClickListener;
    private boolean selectMode;

    // Interface for item click event
    public interface OnTypeOfServiceMethodItemClickListener{
        void onTypeOfServiceMethodItemClickListener(TypeOfServiceModel typeOfServiceModel, String docId);
    }

    // Constructor for TypeOfServiceAdapter
    public TypeOfServiceAdapter(@NonNull FirestoreRecyclerOptions<TypeOfServiceModel> options, Context context,
                                OnTypeOfServiceMethodItemClickListener onTypeOfServiceMethodItemClickListener,
                                boolean selectMode) {
        super(options);
        this.context = context;
        this.onTypeOfServiceMethodItemClickListener = onTypeOfServiceMethodItemClickListener;
        this.selectMode = selectMode;
    }

    // Binds the data to the Viewholder
    @Override
    protected void onBindViewHolder(@NonNull TypeOfServiceAdapter.TypeOfServiceViewHolder holder, int position,
                                    @NonNull TypeOfServiceModel typeOfServiceModel) {
        holder.typeOfService.setText(typeOfServiceModel.typeOfService);

        // Setting up OnClickListener for the view
        holder.itemView.setOnClickListener(v -> {
            String docId = this.getSnapshots().getSnapshot(position).getId();
            if (selectMode) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedPaymentMethod", typeOfServiceModel.typeOfService);
                ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                ((Activity) context).finish();
            } else {
                onTypeOfServiceMethodItemClickListener.onTypeOfServiceMethodItemClickListener(typeOfServiceModel, docId);
            }
        });

        // Setting up OnClickListener for the edit button
        holder.editTypeOfServiceButton.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditDeleteTypeOfServiceActivity.class);
            intent.putExtra("typeOfService", typeOfServiceModel.typeOfService);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    // Method to create new ViewHolder
    @NonNull
    @Override
    public TypeOfServiceAdapter.TypeOfServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_type_of_service_item, parent,false);
        return new TypeOfServiceAdapter.TypeOfServiceViewHolder(view);
    }

    // ViewHolder class for TypeOfService
    class TypeOfServiceViewHolder extends RecyclerView.ViewHolder{
        TextView typeOfService;
        Button editTypeOfServiceButton;

        // Constructor for the ViewHolder
        public TypeOfServiceViewHolder(@NonNull View itemView){
            super(itemView);
            // Initialize the view elements
            typeOfService = itemView.findViewById(R.id.type_of_service_name);
            editTypeOfServiceButton = itemView.findViewById(R.id.edit_type_of_service_button);
        }
    }
}
