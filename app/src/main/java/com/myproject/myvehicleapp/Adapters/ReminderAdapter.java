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
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteReminderActivity;
import com.myproject.myvehicleapp.Models.ReminderModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utlities.Utility;

public class ReminderAdapter extends FirestoreRecyclerAdapter<ReminderModel, ReminderAdapter.ReminderViewHolder> {
    Context context;

    public ReminderAdapter(@NonNull FirestoreRecyclerOptions<ReminderModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReminderViewHolder holder, int position, @NonNull ReminderModel reminderModel) {
        holder.reminderTitle.setText(reminderModel.reminderTitle);
        holder.reminderTimestamp.setText(Utility.timestampToString(reminderModel.reminderTimestamp));
        holder.reminderDescription.setText(String.valueOf(reminderModel.reminderDescription));

        holder.editBtn.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditDeleteReminderActivity.class);

            intent.putExtra("reminderTitle",reminderModel.reminderTitle);
            intent.putExtra("reminderTimestamp",reminderModel.reminderTimestamp);
            intent.putExtra("reminderDescription",reminderModel.reminderDescription);

            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);

            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_reminder_item,parent,false);
        return new ReminderViewHolder(view);
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder{
        TextView reminderTitle;
        TextView reminderTimestamp;
        TextView reminderDescription;
        ImageView editBtn;

        public void toggleVisibility(View view) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);

            LinearLayout linearLayout, revealReminderCard;
            linearLayout = itemView.findViewById(R.id.expanded_reminder_menu);
            revealReminderCard = itemView.findViewById(R.id.revealReminder);
            linearLayout.setVisibility(View.GONE);

            editBtn = itemView.findViewById(R.id.reminder_edit_btn);

            reminderTitle = itemView.findViewById(R.id.reminder_title);
            reminderTimestamp = itemView.findViewById(R.id.reminder_timestamp);
            reminderDescription = itemView.findViewById(R.id.reminder_description);

            revealReminderCard.setOnClickListener(view -> {
                toggleVisibility(linearLayout);
                notifyDataSetChanged();
            });
        }
    }
}