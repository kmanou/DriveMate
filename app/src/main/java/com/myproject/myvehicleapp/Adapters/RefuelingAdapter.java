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

public class RefuelingAdapter extends FirestoreRecyclerAdapter<RefuelingModel, RefuelingAdapter.RefuelingViewHolder> {
    Context context;

    public RefuelingAdapter(@NonNull FirestoreRecyclerOptions<RefuelingModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RefuelingViewHolder holder, int position, @NonNull RefuelingModel refuelingModel) {
        holder.recyclerTypeTitle.setText(refuelingModel.recyclerTitle);
        holder.refuelingDateTime.setText(Utility.timestampToString(refuelingModel.refuelingTimestamp));
        holder.refuelingTotalLitres.setText(String.valueOf(refuelingModel.refuelingFuelLitres));
        holder.refuelingTypeOfFuel.setText(refuelingModel.refuelingFuelType);
        holder.refuelingCostPerLitre.setText(String.valueOf(refuelingModel.refuelingPricePerLitre));
        holder.refuelingOdometer.setText(String.valueOf(refuelingModel.refuelingOdometer));
        holder.refuelingTotalPrice.setText(String.valueOf(refuelingModel.refuelingTotalCost));

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

    @NonNull
    @Override
    public RefuelingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_refueling_item,parent,false);
        return new RefuelingViewHolder(view);
    }

    class RefuelingViewHolder extends RecyclerView.ViewHolder{
        TextView recyclerTypeTitle;
        TextView refuelingDateTime;
        TextView refuelingTotalLitres;
        TextView refuelingTypeOfFuel;
        TextView refuelingCostPerLitre;
        //TextView refuelingFuelConsumption;
        TextView refuelingOdometer;
        TextView refuelingTotalPrice;
        ImageView editBtn;


        public void toggleVisibility(View view) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }

        public RefuelingViewHolder(@NonNull View itemView) {
            super(itemView);

            LinearLayout linearLayout, revealRefuelingCard;
            linearLayout = itemView.findViewById(R.id.expanded_refueling_menu);
            revealRefuelingCard = itemView.findViewById(R.id.revealRefueling);
            linearLayout.setVisibility(View.GONE);

            editBtn = itemView.findViewById(R.id.refueling_edit_btn);

            recyclerTypeTitle = itemView.findViewById(R.id.refuelingRecyclerTypeTitleTVItem);
            refuelingDateTime = itemView.findViewById(R.id.refuelingDateTVItem);

            refuelingTotalLitres = itemView.findViewById(R.id.refuelingTotalLitresTVItem);
            refuelingTypeOfFuel = itemView.findViewById(R.id.refuelingTypeOfFuelTVItem);
            refuelingCostPerLitre = itemView.findViewById(R.id.refuelingCostPerLitreTVItem);
            //refuelingFuelConsumption = itemView.findViewById(R.id.refuelingFuelConsumptionTVItem);
            refuelingOdometer = itemView.findViewById(R.id.refuelingOdometerCounterTVItem);
            refuelingTotalPrice = itemView.findViewById(R.id.refuelingTotalPriceTVItem);

            revealRefuelingCard.setOnClickListener(view -> {
                toggleVisibility(linearLayout);
                notifyDataSetChanged();
            });
        }
    }
}

