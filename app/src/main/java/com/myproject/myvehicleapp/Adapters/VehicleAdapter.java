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

public class VehicleAdapter extends FirestoreRecyclerAdapter<VehicleModel, VehicleAdapter.VehicleViewHolder> {
    Context context;

    public VehicleAdapter(@NonNull FirestoreRecyclerOptions<VehicleModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull VehicleViewHolder holder, int position, @NonNull VehicleModel vehicleModel) {

        //holder.vehicleTI.setText(vehicleModel.vehicle);
        holder.vehicle.setText(vehicleModel.vehicleName);
        holder.manufacturer.setText(vehicleModel.manufacturer);
        holder.model.setText(vehicleModel.model);
        holder.licencePlate.setText(vehicleModel.licencePlate);
        holder.year.setText(vehicleModel.year);
        //holder.fuelType.setText(vehicleModel.fuelType);
        holder.fuelCapacity.setText(vehicleModel.fuelCapacity);
        holder.chassisNumber.setText(vehicleModel.chassisNumber);
        holder.identificationVin.setText(vehicleModel.identificationVin);
        //holder.vehicleNotes.setText(vehicleModel.vehicleNotes);


        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditDeleteVehicleActivity.class);

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

            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }


    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_vehicle_item,parent,false);
        return new VehicleViewHolder(view);
    }

    class VehicleViewHolder extends RecyclerView.ViewHolder{
        TextView vehicle;
        TextView manufacturer;
        TextView model;
        TextView licencePlate;
        TextView year;
        TextView fuelCapacity;
        TextView chassisNumber;
        TextView identificationVin;

/*      TextInputEditText vehicleTI;
        TextInputEditText vehicleNameTI;
        TextInputEditText manufacturerTI;
        TextInputEditText modelTI;
        TextInputEditText licencePlateTI;
        TextInputEditText yearTI;
        TextInputEditText fuelTypeTI;
        TextInputEditText fuelCapacityTI;
        TextInputEditText chassisNumberTI;
        TextInputEditText identificationVinTI;
        TextInputEditText vehicleNotesTI;
*/
        public void toggleVisibility(View view) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);

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

            //vehicleTI = itemView.findViewById(R.id.vehicleTI);
            vehicle = itemView.findViewById(R.id.vehicle_name);
            manufacturer = itemView.findViewById(R.id.vehicle_manufacturer);
/*          modelTI = itemView.findViewById(R.id.modelTI);
            licencePlateTI = itemView.findViewById(R.id.licencePlateTI);
            yearTI = itemView.findViewById(R.id.yearTI);
            fuelTypeTI = itemView.findViewById(R.id.fuelTypeTI);
            fuelCapacityTI = itemView.findViewById(R.id.fuelCapacityTI);
            chassisNumberTI = itemView.findViewById(R.id.chassisNumberTI);
            identificationVinTI = itemView.findViewById(R.id.identificationVinTI);
            vehicleNotesTI = itemView.findViewById(R.id.vehicleNotesTI);
*/
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
