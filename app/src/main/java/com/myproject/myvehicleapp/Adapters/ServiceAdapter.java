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

// This class is a FirestoreRecyclerAdapter that handles the service data for each item in the RecyclerView.
public class ServiceAdapter extends FirestoreRecyclerAdapter<ServiceModel, ServiceAdapter.ServiceViewHolder> {

    // Context of the app
    Context context;

    // Constructor for ServiceAdapter that receives FirestoreRecyclerOptions and Context.
    // These parameters are passed to the super constructor of FirestoreRecyclerAdapter and used in the class.
    public ServiceAdapter(@NonNull FirestoreRecyclerOptions<ServiceModel> options, Context context) {
        super(options);
        this.context = context;
    }

    // This method binds the ServiceModel data to the ServiceViewHolder.
    @Override
    protected void onBindViewHolder(@NonNull ServiceViewHolder holder, int position, @NonNull ServiceModel serviceModel) {

        // Set the text for each TextView in the item.
        holder.serviceDateTime.setText(Utility.timestampToString(serviceModel.serviceTimeStamp));
        holder.serviceOdometer.setText(String.valueOf(serviceModel.serviceOdometer));
        holder.serviceTypeOfService.setText(String.valueOf(serviceModel.serviceTypeOfService));
        holder.serviceTotalPrice.setText(String.valueOf(serviceModel.serviceTotalCost));

        // Set up a click listener for the edit button.
        // When clicked, it starts the AddEditDeleteServiceActivity with the data from the current ServiceModel.
        holder.editBtn.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditDeleteServiceActivity.class);

            // Add the data from the ServiceModel to the Intent.
            intent.putExtra("serviceTimeStamp",serviceModel.serviceTimeStamp);
            intent.putExtra("serviceOdometer",serviceModel.serviceOdometer);
            intent.putExtra("serviceTypeOfService",serviceModel.serviceTypeOfService);
            intent.putExtra("serviceTotalCost",serviceModel.serviceTotalCost);
            intent.putExtra("servicePaymentMethod",serviceModel.servicePaymentMethod);
            intent.putExtra("serviceNote",serviceModel.serviceNote);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);

            // Start the activity.
            context.startActivity(intent);
        });
    }

    // This method creates a new ServiceViewHolder and inflates the layout for each item.
    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_service_item,parent,false);
        return new ServiceViewHolder(view);
    }

    // This inner class holds the views for each item in the RecyclerView.
    class ServiceViewHolder extends RecyclerView.ViewHolder{
        TextView recyclerTypeTitle;
        TextView serviceDateTime;
        TextView serviceOdometer;
        TextView serviceTypeOfService;
        TextView serviceTotalPrice;
        ImageView editBtn;

        // This method toggles the visibility of a view.
        public void toggleVisibility(View view) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }

        // Constructor for ServiceViewHolder.
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get the views from the layout.
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

            // Set an OnClickListener for the card.
            // When clicked, it toggles the visibility of the expanded service menu and notifies the adapter to update the item.
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
