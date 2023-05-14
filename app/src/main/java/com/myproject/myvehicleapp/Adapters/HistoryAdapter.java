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
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.myproject.myvehicleapp.AddActivities.AddEditDeleteExpenseActivity;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteRefuelingActivity;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteServiceActivity;
import com.myproject.myvehicleapp.Models.HistoryModel;
import com.myproject.myvehicleapp.Models.ExpenseModel;
import com.myproject.myvehicleapp.Models.RefuelingModel;
import com.myproject.myvehicleapp.Models.ServiceModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Utility;

import java.util.List;

// This class is the adapter for the RecyclerView that displays history data
public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LiveData<List<HistoryModel>> collectionsLiveData;
    private int expandedPosition = -1; // Keeps track of the expanded item's position

    // The constructor takes a context and LiveData object that provides the data
    public HistoryAdapter(Context context, LiveData<List<HistoryModel>> collectionsLiveData) {
        this.context = context;
        this.collectionsLiveData = collectionsLiveData;
    }

    // Determines the type of View that should be used for the item at the given position
    @Override
    public int getItemViewType(int position) {
        if (collectionsLiveData.getValue() == null) {
            return -1;
        }

        HistoryModel collectionItem = collectionsLiveData.getValue().get(position);
        switch (collectionItem.getCollectionType()) {
            case "my_refueling":
                return 0;
            case "my_expense":
                return 1;
            case "my_service":
                return 2;
            default:
                return -1;
        }
    }

    // Called when the RecyclerView needs a new ViewHolder of the given type to represent an item
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:// Inflate layout for refueling item
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_refueling_item, parent, false);
                return new RefuelingViewHolder(view);
            case 1:// Inflate layout for expense item
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_expense_item, parent, false);
                return new ExpenseViewHolder(view);
            case 2:// Inflate layout for service item
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_service_item, parent, false);
                return new ServiceViewHolder(view);
            default:// Throw exception for invalid view type
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    // Called by RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryModel collection = collectionsLiveData.getValue().get(position);
        boolean isExpanded = position == expandedPosition; // Check if this position is the expanded item

        switch (holder.getItemViewType()) {
            case 0: // Handle refueling item
                RefuelingViewHolder refuelingViewHolder = (RefuelingViewHolder) holder;
                refuelingViewHolder.refuelingRecyclerTypeTitle.setText(collection.getRecyclerTitle());

                RefuelingModel refuelingModel = collection.getRefuelingModel();

                if (refuelingModel != null) { // Ensure the refueling model is not null before binding data to views
                    // Bind other views as needed
                    refuelingViewHolder.refuelingRecyclerTypeTitle.setText(refuelingModel.recyclerTitle);
                    refuelingViewHolder.refuelingDateTime.setText(Utility.timestampToString(refuelingModel.refuelingTimestamp));
                    refuelingViewHolder.refuelingTotalLitres.setText(String.valueOf(refuelingModel.refuelingFuelLitres));
                    refuelingViewHolder.refuelingTypeOfFuel.setText(refuelingModel.refuelingFuelType);
                    refuelingViewHolder.refuelingCostPerLitre.setText(String.valueOf(refuelingModel.refuelingPricePerLitre));
                    refuelingViewHolder.refuelingOdometer.setText(String.valueOf(refuelingModel.refuelingOdometer));
                    refuelingViewHolder.refuelingTotalPrice.setText(String.valueOf(refuelingModel.refuelingTotalCost));
                }

                // Set click listener for the edit button
                refuelingViewHolder.refuelingEditBtn.setOnClickListener((v)-> {
                    // Create an intent for the AddEditDeleteRefuelingActivity and put extras
                    Intent intent = new Intent(context, AddEditDeleteRefuelingActivity.class);

                    intent.putExtra("refuelingTimestamp", refuelingModel.refuelingTimestamp);
                    intent.putExtra("refuelingFuelLitres", refuelingModel.refuelingFuelLitres);
                    intent.putExtra("refuelingFuelType", refuelingModel.refuelingFuelType);
                    intent.putExtra("refuelingPricePerLitre", refuelingModel.refuelingPricePerLitre);
                    intent.putExtra("refuelingOdometer", refuelingModel.refuelingOdometer);
                    intent.putExtra("refuelingTotalCost", refuelingModel.refuelingTotalCost);
                    intent.putExtra("refuelingPaymentMethod", refuelingModel.refuelingPaymentMethod);
                    intent.putExtra("refuelingNote", refuelingModel.refuelingNote);

                    String docId = collection.getId();
                    intent.putExtra("docId", docId);

                    context.startActivity(intent);
                    notifyDataSetChanged(); // Notify the adapter that the data set has changed
                });

                // Toggle visibility of the LinearLayout based on the expanded state
                refuelingViewHolder.refuelingLinearLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                // Set click listener for the card that reveals the refueling data
                refuelingViewHolder.revealRefuelingCard.setOnClickListener(view -> {
                    int previousExpandedPosition = expandedPosition;
                    // Update the expanded position
                    expandedPosition = position == expandedPosition ? -1 : position;
                    // If a previous item was expanded, notify it to update
                    if (previousExpandedPosition != -1) {
                        notifyItemChanged(previousExpandedPosition);
                    }
                    // Notify the current item to update
                    notifyItemChanged(position);
                });
                break;
            case 1: // Handle expense item
                ExpenseViewHolder expenseViewHolder = (ExpenseViewHolder) holder;
                expenseViewHolder.expenseRecyclerTypeTitle.setText(collection.getRecyclerTitle());

                ExpenseModel expenseModel = collection.getExpenseModel();

                if (expenseModel != null) { // Ensure the expense model is not null before binding data to views
                    // Bind other views as needed
                    expenseViewHolder.expenseRecyclerTypeTitle.setText(expenseModel.recyclerTitle);
                    expenseViewHolder.expenseDateTime.setText(Utility.timestampToString(expenseModel.expenseTimeStamp));
                    expenseViewHolder.expenseOdometer.setText(String.valueOf(expenseModel.expenseOdometer));
                    expenseViewHolder.expenseTypeOfExpense.setText(String.valueOf(expenseModel.expenseTypeOfExpense));
                    expenseViewHolder.expenseTotalPrice.setText(String.valueOf(expenseModel.expenseTotalCost));
                }

                // Set click listener for the edit button
                expenseViewHolder.expenseEditBtn.setOnClickListener((v)->{
                    // Create an intent for the AddEditDeleteExpenseActivity and put extras
                    Intent intent = new Intent(context, AddEditDeleteExpenseActivity.class);

                    intent.putExtra("expenseTimeStamp",expenseModel.expenseTimeStamp);
                    intent.putExtra("expenseOdometer",expenseModel.expenseOdometer);
                    intent.putExtra("expenseTypeOfExpense",expenseModel.expenseTypeOfExpense);
                    intent.putExtra("expenseTotalCost",expenseModel.expenseTotalCost);
                    intent.putExtra("expensePaymentMethod",expenseModel.expensePaymentMethod);
                    intent.putExtra("expenseNote",expenseModel.expenseNote);
                    String docId = collection.getId();
                    intent.putExtra("docId",docId);

                    context.startActivity(intent);
                    notifyDataSetChanged(); // Notify the adapter that the data set has changed
                });

                // Toggle visibility of the LinearLayout based on the expanded state
                expenseViewHolder.expenseLinearLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                // Set click listener for the card that reveals the expense data
                expenseViewHolder.revealExpenseCard.setOnClickListener(view -> {
                    int previousExpandedPosition = expandedPosition;
                    // Update the expanded position
                    expandedPosition = position == expandedPosition ? -1 : position;
                    // If a previous item was expanded, notify it to update
                    if (previousExpandedPosition != -1) {
                        notifyItemChanged(previousExpandedPosition);
                    }
                    // Notify the current item to update
                    notifyItemChanged(position);
                });

                break;
            case 2: // Handle service item
                ServiceViewHolder serviceViewHolder = (ServiceViewHolder) holder;
                // Set the title of this service item
                serviceViewHolder.serviceRecyclerTypeTitle.setText(collection.getRecyclerTitle());

                ServiceModel serviceModel = collection.getServiceModel();

                if (serviceModel != null) { // Ensure the service model is not null before binding data to views
                    // Bind other views as needed
                    serviceViewHolder.serviceRecyclerTypeTitle.setText(serviceModel.recyclerTitle);
                    serviceViewHolder.serviceDateTime.setText(Utility.timestampToString(serviceModel.serviceTimeStamp));
                    serviceViewHolder.serviceOdometer.setText(String.valueOf(serviceModel.serviceOdometer));
                    serviceViewHolder.serviceTypeOfService.setText(String.valueOf(serviceModel.serviceTypeOfService));
                    serviceViewHolder.serviceTotalPrice.setText(String.valueOf(serviceModel.serviceTotalCost));
                }
                // Set click listener for the edit button
                serviceViewHolder.serviceEditBtn.setOnClickListener((v)->{
                    // Create an intent for the AddEditDeleteServiceActivity and put extras
                    Intent intent = new Intent(context, AddEditDeleteServiceActivity.class);

                    intent.putExtra("serviceTimeStamp",serviceModel.serviceTimeStamp);
                    intent.putExtra("serviceOdometer",serviceModel.serviceOdometer);
                    intent.putExtra("serviceTypeOfService",serviceModel.serviceTypeOfService);
                    intent.putExtra("serviceTotalCost",serviceModel.serviceTotalCost);
                    intent.putExtra("servicePaymentMethod",serviceModel.servicePaymentMethod);
                    intent.putExtra("serviceNote",serviceModel.serviceNote);
                    String docId = collection.getId();
                    intent.putExtra("docId",docId);

                    context.startActivity(intent);
                    notifyDataSetChanged(); // Notify the adapter that the data set has changed
                });

                // Toggle visibility of the LinearLayout based on the expanded state
                serviceViewHolder.serviceLinearLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                // Set click listener for the card that reveals the expense data
                serviceViewHolder.revealServiceCard.setOnClickListener(view -> {
                    int previousExpandedPosition = expandedPosition;
                    // Update the expanded position
                    expandedPosition = position == expandedPosition ? -1 : position;
                    // If a previous item was expanded, notify it to update
                    if (previousExpandedPosition != -1) {
                        notifyItemChanged(previousExpandedPosition);
                    }
                    // Notify the current item to update
                    notifyItemChanged(position);
                });
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public int getItemCount() {
        // Return the size of the data set, or 0 if the data set is null
        return collectionsLiveData.getValue() != null ? collectionsLiveData.getValue().size() : 0;
    }

    // ViewHolder for refueling items
    public class RefuelingViewHolder extends RecyclerView.ViewHolder {

        //Define views
        TextView refuelingRecyclerTypeTitle;
        TextView refuelingDateTime;
        TextView refuelingTotalLitres;
        TextView refuelingTypeOfFuel;
        TextView refuelingCostPerLitre;
        TextView refuelingOdometer;
        TextView refuelingTotalPrice;
        ImageView refuelingEditBtn;
        LinearLayout refuelingLinearLayout;
        View revealRefuelingCard;

        public RefuelingViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            refuelingLinearLayout = itemView.findViewById(R.id.expanded_refueling_menu);
            revealRefuelingCard = itemView.findViewById(R.id.revealRefueling);
            refuelingEditBtn = itemView.findViewById(R.id.refueling_edit_btn);
            refuelingRecyclerTypeTitle = itemView.findViewById(R.id.refuelingRecyclerTypeTitleTVItem);
            refuelingDateTime = itemView.findViewById(R.id.refuelingDateTVItem);
            refuelingTotalLitres = itemView.findViewById(R.id.refuelingTotalLitresTVItem);
            refuelingTypeOfFuel = itemView.findViewById(R.id.refuelingTypeOfFuelTVItem);
            refuelingCostPerLitre = itemView.findViewById(R.id.refuelingCostPerLitreTVItem);
            refuelingOdometer = itemView.findViewById(R.id.refuelingOdometerCounterTVItem);
            refuelingTotalPrice = itemView.findViewById(R.id.refuelingTotalPriceTVItem);

            // Set click listener for the item view
            itemView.setOnClickListener(view -> {
                // If the clicked item is already expanded, collapse it
                if (expandedPosition == getBindingAdapterPosition()) {
                    expandedPosition = -1;
                    notifyItemChanged(getBindingAdapterPosition());
                } else {
                    // If there is another item expanded, collapse it
                    if (expandedPosition != -1) {
                        int oldExpandedPosition = expandedPosition;
                        expandedPosition = -1;
                        notifyItemChanged(oldExpandedPosition);
                    }
                    // Expand the clicked item
                    expandedPosition = getBindingAdapterPosition();
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });
        }
    }

    // ViewHolder for expense items
    public class ExpenseViewHolder extends RecyclerView.ViewHolder {

        //Define views
        TextView expenseRecyclerTypeTitle;
        TextView expenseDateTime;
        TextView expenseOdometer;
        TextView expenseTypeOfExpense;
        TextView expenseTotalPrice;
        ImageView expenseEditBtn;
        LinearLayout expenseLinearLayout;
        View revealExpenseCard;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views as needed
            expenseLinearLayout = itemView.findViewById(R.id.expanded_expense_menu);
            revealExpenseCard = itemView.findViewById(R.id.revealExpense);
            expenseLinearLayout.setVisibility(View.GONE);
            expenseEditBtn = itemView.findViewById(R.id.expense_edit_btn);
            expenseRecyclerTypeTitle = itemView.findViewById(R.id.expenseRecyclerTypeTitleTVItem);
            expenseDateTime = itemView.findViewById(R.id.expenseDateTVItem);
            expenseOdometer = itemView.findViewById(R.id.expenseOdometerCounterTVItem);
            expenseTypeOfExpense = itemView.findViewById(R.id.expenseTypeOfExpenseTVItem);
            expenseTotalPrice = itemView.findViewById(R.id.expenseTotalPriceTVItem);

            // Set click listener for the item view
            itemView.setOnClickListener(view -> {
                // If the clicked item is already expanded, collapse it
                if (expandedPosition == getBindingAdapterPosition()) {
                    expandedPosition = -1;
                    notifyItemChanged(getBindingAdapterPosition());
                } else {
                    // If there is another item expanded, collapse it
                    if (expandedPosition != -1) {
                        int oldExpandedPosition = expandedPosition;
                        expandedPosition = -1;
                        notifyItemChanged(oldExpandedPosition);
                    }
                    // Expand the clicked item
                    expandedPosition = getBindingAdapterPosition();
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });
        }
    }

    // ViewHolder for service items
    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        //Define Views
        TextView serviceRecyclerTypeTitle;
        TextView serviceDateTime;
        TextView serviceOdometer;
        TextView serviceTypeOfService;
        TextView serviceTotalPrice;
        ImageView serviceEditBtn;
        LinearLayout serviceLinearLayout;
        View revealServiceCard;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            serviceLinearLayout = itemView.findViewById(R.id.expanded_service_menu);
            revealServiceCard = itemView.findViewById(R.id.revealService);
            serviceEditBtn = itemView.findViewById(R.id.service_edit_btn);
            serviceRecyclerTypeTitle = itemView.findViewById(R.id.serviceRecyclerTypeTitleTVItem);
            serviceDateTime = itemView.findViewById(R.id.serviceDateTVItem);
            serviceOdometer = itemView.findViewById(R.id.serviceOdometerCounterTVItem);
            serviceTypeOfService = itemView.findViewById(R.id.serviceTypeOfServiceTVItem);
            serviceTotalPrice = itemView.findViewById(R.id.serviceTotalPriceTVItem);

            // Set a click listener on the item view
            itemView.setOnClickListener(view -> {
                // If the clicked item is already expanded, collapse it
                if (expandedPosition == getBindingAdapterPosition()) {
                    expandedPosition = -1;
                    notifyItemChanged(getBindingAdapterPosition());
                } else {
                    // If there is another item expanded, collapse it
                    if (expandedPosition != -1) {
                        int oldExpandedPosition = expandedPosition;
                        expandedPosition = -1;
                        notifyItemChanged(oldExpandedPosition);
                    }
                    // Expand the clicked item
                    expandedPosition = getBindingAdapterPosition();
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });
        }
    }
}

