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


public class FuelAdapter extends FirestoreRecyclerAdapter<FuelModel, FuelAdapter.FuelViewHolder> {
    Context context;
    private OnFuelItemClickListener onFuelItemClickListener;
    private boolean selectMode;

    public interface OnFuelItemClickListener {
        void onFuelItemClick(FuelModel fuelModel, String docId);
    }

    public FuelAdapter(@NonNull FirestoreRecyclerOptions<FuelModel> options, Context context, OnFuelItemClickListener onFuelItemClickListener, boolean selectMode) {
        super(options);
        this.context = context;
        this.onFuelItemClickListener = onFuelItemClickListener;
        this.selectMode = selectMode;
    }


    @Override
    protected void onBindViewHolder(@NonNull FuelViewHolder holder, int position, @NonNull FuelModel fuelModel) {
        holder.fuel.setText(fuelModel.fuelName);
        holder.fuelType.setText(fuelModel.fuelType);

        holder.itemView.setOnClickListener(v -> {
            String docId = this.getSnapshots().getSnapshot(position).getId();
            if (selectMode) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedFuelName", fuelModel.fuelName);
                //resultIntent.putExtra("selectedFuelType", fuelModel.fuelType);
                ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                ((Activity) context).finish();
            } else {
                onFuelItemClickListener.onFuelItemClick(fuelModel, docId);
            }
        });

        holder.editFuelButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditDeleteFuelActivity.class);
            intent.putExtra("fuelName", fuelModel.fuelName);
            intent.putExtra("fuelType", fuelModel.fuelType);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_fuel_item,parent,false);
        return new FuelViewHolder(view);
    }

    class FuelViewHolder extends RecyclerView.ViewHolder{
        TextView fuel;
        TextView fuelType;
        Button editFuelButton;

        public FuelViewHolder(@NonNull View itemView) {
            super(itemView);
            fuel = itemView.findViewById(R.id.fuel_name);
            fuelType = itemView.findViewById(R.id.fuel_type);
            editFuelButton = itemView.findViewById(R.id.edit_fuel_button);
        }
    }
}
