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
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteRefuelingActivity;
import com.myproject.myvehicleapp.Models.RefuelingModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Utility;

// This class adapts a list of refueling data for display in a RecyclerView
public class RefuelingAdapter extends FirestoreRecyclerAdapter<RefuelingModel, RefuelingAdapter.RefuelingViewHolder> {
    // The context is needed for various Android operations
    Context context;

    // Constructor for the adapter; initializes it with data and context
    public RefuelingAdapter(@NonNull FirestoreRecyclerOptions<RefuelingModel> options, Context context) {
        super(options);
        this.context = context;
    }

    // This method is called to bind each item in the RecyclerView to its data
    @Override
    protected void onBindViewHolder(@NonNull RefuelingViewHolder holder, int position, @NonNull RefuelingModel refuelingModel) {
        // Setting the data for each TextView in the ViewHolder
        holder.recyclerTypeTitle.setText(refuelingModel.recyclerTitle);
        holder.refuelingDateTime.setText(Utility.timestampToString(refuelingModel.refuelingTimestamp));
        holder.refuelingTotalLitres.setText(String.valueOf(refuelingModel.refuelingFuelLitres));
        holder.refuelingTypeOfFuel.setText(refuelingModel.refuelingFuelType);
        holder.refuelingCostPerLitre.setText(String.valueOf(refuelingModel.refuelingPricePerLitre));
        holder.refuelingOdometer.setText(String.valueOf(refuelingModel.refuelingOdometer));
        holder.refuelingTotalPrice.setText(String.valueOf(refuelingModel.refuelingTotalCost));

        // Setting up an OnClickListener for the edit button to start AddEditDeleteRefuelingActivity
        holder.editBtn.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditDeleteRefuelingActivity.class);

            intent.putExtra("refuelingTimestamp",refuelingModel.refuelingTimestamp);
            intent.putExtra("refuelingFuelLitres",refuelingModel.refuelingFuelLitres);
            intent.putExtra("refuelingFuelType",refuelingModel.refuelingFuelType);
            intent.putExtra("refuelingPricePerLitre",refuelingModel.refuelingPricePerLitre);
            intent.putExtra("refuelingOdometer",refuelingModel.refuelingOdometer);
            intent.putExtra("refuelingTotalCost",refuelingModel.refuelingTotalCost);
            intent.putExtra("refuelingPaymentMethod",refuelingModel.refuelingPaymentMethod);
            intent.putExtra("refuelingNote",refuelingModel.refuelingNote);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);

            context.startActivity(intent);
        });
    }

    // This method is called to create a new ViewHolder
    @NonNull
    @Override
    public RefuelingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_refueling_item,parent,false);
        return new RefuelingViewHolder(view);
    }

    // ViewHolder class represents each item in the RecyclerView
    class RefuelingViewHolder extends RecyclerView.ViewHolder{

        // Views inside each item
        TextView recyclerTypeTitle;
        TextView refuelingDateTime;
        TextView refuelingTotalLitres;
        TextView refuelingTypeOfFuel;
        TextView refuelingCostPerLitre;
        TextView refuelingOdometer;
        TextView refuelingTotalPrice;
        ImageView editBtn;

        // Method to toggle visibility of a view
        public void toggleVisibility(View view) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }

        // ViewHolder constructor initializes the views inside each item
        public RefuelingViewHolder(@NonNull View itemView) {
            super(itemView);

            LinearLayout linearLayout, revealRefuelingCard;
            linearLayout = itemView.findViewById(R.id.expanded_refueling_menu);
            revealRefuelingCard = itemView.findViewById(R.id.revealRefueling);
            linearLayout.setVisibility(View.GONE);

            // Initialization of views
            editBtn = itemView.findViewById(R.id.refueling_edit_btn);
            recyclerTypeTitle = itemView.findViewById(R.id.refuelingRecyclerTypeTitleTVItem);
            refuelingDateTime = itemView.findViewById(R.id.refuelingDateTVItem);
            refuelingTotalLitres = itemView.findViewById(R.id.refuelingTotalLitresTVItem);
            refuelingTypeOfFuel = itemView.findViewById(R.id.refuelingTypeOfFuelTVItem);
            refuelingCostPerLitre = itemView.findViewById(R.id.refuelingCostPerLitreTVItem);
            refuelingOdometer = itemView.findViewById(R.id.refuelingOdometerCounterTVItem);
            refuelingTotalPrice = itemView.findViewById(R.id.refuelingTotalPriceTVItem);

            // Toggling visibility of linearLayout when revealRefuelingCard is clicked
            revealRefuelingCard.setOnClickListener(view -> {
                toggleVisibility(linearLayout);
                notifyDataSetChanged();
            });
        }
    }
}