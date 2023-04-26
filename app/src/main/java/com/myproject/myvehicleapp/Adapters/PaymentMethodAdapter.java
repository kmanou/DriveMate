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


public class PaymentMethodAdapter extends FirestoreRecyclerAdapter<PaymentMethodModel, PaymentMethodAdapter.PaymentMethodViewHolder>  {
    Context context;
    private OnPaymentMethodItemClickListener onPaymentMethodItemClickListener;
    private boolean selectMode;

    public interface OnPaymentMethodItemClickListener{
        void onPaymentMethodItemClick(PaymentMethodModel paymentMethodModel, String docId);
    }

    public PaymentMethodAdapter(@NonNull FirestoreRecyclerOptions<PaymentMethodModel> options, Context context, OnPaymentMethodItemClickListener onPaymentMethodItemClickListener, boolean selectMode) {
        super(options);
        this.context = context;
        this.onPaymentMethodItemClickListener = onPaymentMethodItemClickListener;
        this.selectMode = selectMode;
    }

    @Override
    protected void onBindViewHolder(@NonNull PaymentMethodViewHolder holder, int position, @NonNull PaymentMethodModel paymentMethodModel) {
        holder.paymentMethod.setText(paymentMethodModel.paymentMethod);

        holder.itemView.setOnClickListener(v -> {
            String docId = this.getSnapshots().getSnapshot(position).getId();
            if (selectMode) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedPaymentMethod", paymentMethodModel.paymentMethod);
                ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                ((Activity) context).finish();
            } else {
                onPaymentMethodItemClickListener.onPaymentMethodItemClick(paymentMethodModel, docId);
            }
        });

        holder.editPaymentMethodButton.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditDeletePaymentMethodActivity.class);
            intent.putExtra("paymentMethod", paymentMethodModel.paymentMethod);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_payment_method_item, parent,false);
        return new PaymentMethodViewHolder(view);
    }

    class PaymentMethodViewHolder extends RecyclerView.ViewHolder{
        TextView paymentMethod;
        Button editPaymentMethodButton;

        public PaymentMethodViewHolder(@NonNull View itemView){
            super(itemView);
            paymentMethod = itemView.findViewById(R.id.payment_method_name);
            editPaymentMethodButton = itemView.findViewById(R.id.edit_paymentMethod_button);
        }
    }
}