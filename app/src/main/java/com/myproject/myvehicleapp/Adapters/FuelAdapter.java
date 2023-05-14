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

import com.myproject.myvehicleapp.AddActivities.AddEditDeleteFuelActivity;
import com.myproject.myvehicleapp.Models.FuelModel;
import com.myproject.myvehicleapp.R;


// This class handles the display of fuel data in a RecyclerView
public class FuelAdapter extends FirestoreRecyclerAdapter<FuelModel, FuelAdapter.FuelViewHolder> {
    Context context;
    private OnFuelItemClickListener onFuelItemClickListener;
    private boolean selectMode;

    // Interface for click events on fuel items
    public interface OnFuelItemClickListener {
        void onFuelItemClick(FuelModel fuelModel, String docId);
    }

    // Constructor takes Firestore options, a Context, a listener for item clicks, and a boolean for select mode
    public FuelAdapter(@NonNull FirestoreRecyclerOptions<FuelModel> options, Context context, OnFuelItemClickListener onFuelItemClickListener, boolean selectMode) {
        super(options);
        this.context = context;
        this.onFuelItemClickListener = onFuelItemClickListener;
        this.selectMode = selectMode;
    }

    // onBindViewHolder is called by RecyclerView to display the data at the specified position
    @Override
    protected void onBindViewHolder(@NonNull FuelViewHolder holder, int position, @NonNull FuelModel fuelModel) {
        // Setting fuel details to their respective views
        holder.fuel.setText(fuelModel.fuelName);
        holder.fuelType.setText(fuelModel.fuelType);

        // Setting on click listener for the item view
        holder.itemView.setOnClickListener(v -> {
            String docId = this.getSnapshots().getSnapshot(position).getId();
            // Check if in select mode
            if (selectMode) {
                // If so, create a result intent and finish the activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedFuelName", fuelModel.fuelName);
                ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                ((Activity) context).finish();
            } else {
                // If not, notify the item click listener
                onFuelItemClickListener.onFuelItemClick(fuelModel, docId);
            }
        });

        // Setting on click listener for the edit button
        holder.editFuelButton.setOnClickListener(v -> {
            // Creating an intent for AddEditDeleteFuelActivity and adding extras
            Intent intent = new Intent(context, AddEditDeleteFuelActivity.class);
            intent.putExtra("fuelName", fuelModel.fuelName);
            intent.putExtra("fuelType", fuelModel.fuelType);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            // Starting the new Activity
            context.startActivity(intent);
        });
    }

    // This method is called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item
    @NonNull
    @Override
    public FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_fuel_item,parent,false);
        return new FuelViewHolder(view);
    }

    // This class provides a reference to the views for each data item
    class FuelViewHolder extends RecyclerView.ViewHolder{
        TextView fuel;
        TextView fuelType;
        Button editFuelButton;

        // ViewHolder's constructor takes the root view of the item
        public FuelViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initializing views
            fuel = itemView.findViewById(R.id.fuel_name);
            fuelType = itemView.findViewById(R.id.fuel_type);
            editFuelButton = itemView.findViewById(R.id.edit_fuel_button);
        }
    }
}

