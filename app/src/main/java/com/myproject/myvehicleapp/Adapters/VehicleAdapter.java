package com.myproject.myvehicleapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteVehicleActivity;
import com.myproject.myvehicleapp.Models.VehicleModel;
import com.myproject.myvehicleapp.R;

// Custom adapter class for FirestoreRecyclerAdapter with VehicleModel
public class VehicleAdapter extends FirestoreRecyclerAdapter<VehicleModel, VehicleAdapter.VehicleViewHolder> {
    Context context;

    // Constructor for VehicleAdapter
    public VehicleAdapter(@NonNull FirestoreRecyclerOptions<VehicleModel> options, Context context) {
        super(options);
        this.context = context;
    }

    // Binds the data to the viewholder
    @Override
    protected void onBindViewHolder(@NonNull VehicleViewHolder holder, int position, @NonNull VehicleModel vehicleModel) {

        // Set the data to the TextViews
        holder.vehicle.setText(vehicleModel.vehicleName);
        holder.manufacturer.setText(vehicleModel.manufacturer);
        holder.model.setText(vehicleModel.model);
        holder.licencePlate.setText(vehicleModel.licencePlate);
        holder.year.setText(vehicleModel.year);
        holder.fuelCapacity.setText(vehicleModel.fuelCapacity);
        holder.chassisNumber.setText(vehicleModel.chassisNumber);
        holder.identificationVin.setText(vehicleModel.identificationVin);

        // Setting up OnClickListener for the view
        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditDeleteVehicleActivity.class);

            // Putting extra data to the intent
            intent.putExtra("vehicle",vehicleModel.vehicle);
            intent.putExtra("vehicleName",vehicleModel.vehicleName);
            intent.putExtra("manufacturer",vehicleModel.manufacturer);
            intent.putExtra("vehicleModel",vehicleModel.model);
            intent.putExtra("licencePlate",vehicleModel.licencePlate);
            intent.putExtra("vehicleYear",vehicleModel.year);
            intent.putExtra("fuelType",vehicleModel.fuelType);
            intent.putExtra("fuelCapacity",vehicleModel.fuelCapacity);
            intent.putExtra("chassisNumber",vehicleModel.chassisNumber);
            intent.putExtra("identificationVin",vehicleModel.identificationVin);
            intent.putExtra("vehicleNote",vehicleModel.vehicleNotes);

            // Getting the document id from the snapshot
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    // Method to create new ViewHolder
    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_vehicle_item,parent,false);
        return new VehicleViewHolder(view);
    }

    // ViewHolder class for Vehicle
    class VehicleViewHolder extends RecyclerView.ViewHolder{
        TextView vehicle;
        TextView manufacturer;
        TextView model;
        TextView licencePlate;
        TextView year;
        TextView fuelCapacity;
        TextView chassisNumber;
        TextView identificationVin;

        // Method for toggling visibility
        public void toggleVisibility(View view) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }

        // Constructor for the ViewHolder
        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the view elements
            LinearLayout linearLayout, revealVehicleCard;
            linearLayout = itemView.findViewById(R.id.expanded_vehicle_menu);
            revealVehicleCard = itemView.findViewById(R.id.revealVehicle);
            linearLayout.setVisibility(View.GONE);

            model = itemView.findViewById(R.id.modelTV);
            licencePlate = itemView.findViewById(R.id.licencePlateTV);
            fuelCapacity = itemView.findViewById(R.id.fuelCapacityTV);
            year = itemView.findViewById(R.id.yearTV);
            chassisNumber = itemView.findViewById(R.id.chassisNumberTV);
            identificationVin = itemView.findViewById(R.id.identificationVinTV);
            vehicle = itemView.findViewById(R.id.vehicle_name);
            manufacturer = itemView.findViewById(R.id.vehicle_manufacturer);

            // Setting up OnClickListener for the
            revealVehicleCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleVisibility(linearLayout);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
