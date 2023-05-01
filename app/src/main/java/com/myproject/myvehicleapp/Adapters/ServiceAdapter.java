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
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteServiceActivity;
import com.myproject.myvehicleapp.Models.ServiceModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Utility;

public class ServiceAdapter extends FirestoreRecyclerAdapter<ServiceModel, ServiceAdapter.ServiceViewHolder> {
    Context context;

    public ServiceAdapter(@NonNull FirestoreRecyclerOptions<ServiceModel> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ServiceViewHolder holder, int position, @NonNull ServiceModel serviceModel) {

        holder.serviceDateTime.setText(Utility.timestampToString(serviceModel.serviceTimeStamp));
        holder.serviceOdometer.setText(String.valueOf(serviceModel.serviceOdometer));
        holder.serviceTypeOfService.setText(String.valueOf(serviceModel.serviceTypeOfService));
        holder.serviceTotalPrice.setText(String.valueOf(serviceModel.serviceTotalCost));

        holder.editBtn.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditDeleteServiceActivity.class);

            intent.putExtra("serviceTimeStamp",serviceModel.serviceTimeStamp);
            intent.putExtra("serviceOdometer",serviceModel.serviceOdometer);
            intent.putExtra("serviceTypeOfService",serviceModel.serviceTypeOfService);
            intent.putExtra("serviceTotalCost",serviceModel.serviceTotalCost);
            intent.putExtra("servicePaymentMethod",serviceModel.servicePaymentMethod);
            intent.putExtra("serviceNote",serviceModel.serviceNote);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);

            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_service_item,parent,false);
        return new ServiceViewHolder(view);
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder{
        TextView recyclerTypeTitle;
        TextView serviceDateTime;
        TextView serviceOdometer;
        TextView serviceTypeOfService;
        TextView serviceTotalPrice;
        ImageView editBtn;

        public void toggleVisibility(View view) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            LinearLayout linearLayout, revealServiceCard;
            linearLayout = itemView.findViewById(R.id.expanded_service_menu);
            revealServiceCard = itemView.findViewById(R.id.revealService);
            linearLayout.setVisibility(View.GONE);

            editBtn = itemView.findViewById(R.id.service_edit_btn);

            recyclerTypeTitle = itemView.findViewById(R.id.refuelingRecyclerTypeTitleTVItem);
            serviceDateTime = itemView.findViewById(R.id.serviceDateTVItem);
            serviceOdometer = itemView.findViewById(R.id.serviceOdometerCounterTVItem);
            serviceTypeOfService = itemView.findViewById(R.id.serviceTypeOfServiceTVItem);
            serviceTotalPrice = itemView.findViewById(R.id.serviceTotalPriceTVItem);

            revealServiceCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleVisibility(linearLayout);
                    notifyDataSetChanged();
                }
            });
        }
    }
}