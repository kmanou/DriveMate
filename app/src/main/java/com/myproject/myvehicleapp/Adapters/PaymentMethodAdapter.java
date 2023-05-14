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
import com.myproject.myvehicleapp.AddActivities.AddEditDeletePaymentMethodActivity;
import com.myproject.myvehicleapp.Models.PaymentMethodModel;
import com.myproject.myvehicleapp.R;


// Adapter class to handle payment methods in the Firestore database
public class PaymentMethodAdapter extends FirestoreRecyclerAdapter<PaymentMethodModel, PaymentMethodAdapter.PaymentMethodViewHolder>  {
    // Variable to store application context
    Context context;
    // Listener to handle payment method item click events
    private OnPaymentMethodItemClickListener onPaymentMethodItemClickListener;
    // Flag to check if the adapter is in select mode
    private boolean selectMode;

    // Interface to handle payment method item click events
    public interface OnPaymentMethodItemClickListener{
        void onPaymentMethodItemClick(PaymentMethodModel paymentMethodModel, String docId);
    }

    // Constructor for the PaymentMethodAdapter
    public PaymentMethodAdapter(@NonNull FirestoreRecyclerOptions<PaymentMethodModel> options, Context context, OnPaymentMethodItemClickListener onPaymentMethodItemClickListener, boolean selectMode) {
        super(options); // Call the superclass constructor (FirestoreRecyclerAdapter)
        this.context = context; // Set the context
        this.onPaymentMethodItemClickListener = onPaymentMethodItemClickListener; // Set the item click listener
        this.selectMode = selectMode; // Set the select mode
    }

    // This function binds the data to the view holder
    @Override
    protected void onBindViewHolder(@NonNull PaymentMethodViewHolder holder, int position, @NonNull PaymentMethodModel paymentMethodModel) {
        // Set the payment method text
        holder.paymentMethod.setText(paymentMethodModel.paymentMethod);

        // Set an OnClickListener for the itemView
        holder.itemView.setOnClickListener(v -> {
            // Get the document ID
            String docId = this.getSnapshots().getSnapshot(position).getId();
            // Check if the adapter is in select mode
            if (selectMode) {
                // Create a new intent to return the selected payment method
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedPaymentMethod", paymentMethodModel.paymentMethod);
                // Set the result of the activity and finish it
                ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                ((Activity) context).finish();
            } else {
                // Call the item click listener
                onPaymentMethodItemClickListener.onPaymentMethodItemClick(paymentMethodModel, docId);
            }
        });

        // Set an OnClickListener for the edit button
        holder.editPaymentMethodButton.setOnClickListener((v)->{
            // Create a new intent for the AddEditDeletePaymentMethodActivity
            Intent intent = new Intent(context, AddEditDeletePaymentMethodActivity.class);
            // Add the payment method to the intent extras
            intent.putExtra("paymentMethod", paymentMethodModel.paymentMethod);
            // Get the document ID and add it to the intent extras
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            // Start the activity
            context.startActivity(intent);
        });
    }

    // This function inflates the view and returns the PaymentMethodViewHolder
    @NonNull
    @Override
    public PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view from the XML layout file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_payment_method_item, parent,false);
        // Return a new PaymentMethodViewHolder
        return new PaymentMethodViewHolder(view);
    }

    // ViewHolder class to hold the views for each payment method
    class PaymentMethodViewHolder extends RecyclerView.ViewHolder{
        // TextView to display the payment method and Button to edit the payment method
        TextView paymentMethod;
        Button editPaymentMethodButton;

        // Constructor for the PaymentMethodViewHolder
        public PaymentMethodViewHolder(@NonNull View itemView){
            super(itemView);
            // Initialize the TextView and Button
            paymentMethod = itemView.findViewById(R.id.payment_method_name);
            editPaymentMethodButton = itemView.findViewById(R.id.edit_paymentMethod_button);
        }
    }
}
