package com.myproject.myvehicleapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentReference;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteReminderActivity;
import com.myproject.myvehicleapp.Models.ReminderModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReminderAdapter extends FirestoreRecyclerAdapter<ReminderModel, ReminderAdapter.ReminderViewHolder> {
    Context context;
    private int expandedPosition = -1;

    public ReminderAdapter(@NonNull FirestoreRecyclerOptions<ReminderModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReminderViewHolder holder, int position, @NonNull ReminderModel reminderModel) {
        holder.reminderTitle.setText(reminderModel.reminderTitle);

        // Extract the date and time from the reminder timestamp
        Date reminderDate = reminderModel.reminderTimestamp.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String dateString = dateFormat.format(reminderDate);
        String timeString = timeFormat.format(reminderDate);

        // Set the date and time TextViews
        holder.reminderTimestampDate.setText(dateString);
        holder.reminderTimestampTime.setText(timeString);

        holder.reminderToggle.setOnCheckedChangeListener(null);

        holder.reminderDescription.setText(String.valueOf(reminderModel.reminderDescription));

        // Initialize the switch based on the alarm state stored in the reminderModel
        holder.reminderToggle.setChecked(reminderModel.isAlarmEnabled());

        // Update the color of the reminder icon based on the alarm state
        if (reminderModel.isAlarmEnabled()) {
            holder.reminderIcon.setColorFilter(ContextCompat.getColor(context, R.color.amber_900));
        } else {
            holder.reminderIcon.setColorFilter(ContextCompat.getColor(context, R.color.blue_grey_800));
        }

        // Set up a listener for the switch
        holder.reminderToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {

            // Get the document ID of the current reminder
            String docId = getSnapshots().getSnapshot(position).getId();

            if (isChecked) {
                // Enable alarm and notification
                // Set up the alarm using the existing data in the reminderModel
                Utility.scheduleAlarm(context, docId, reminderModel.getReminderTitle(), reminderModel.getReminderDescription(), reminderModel.getReminderTimestamp());

                //set the color of the icon when enabled
                holder.reminderIcon.setColorFilter(ContextCompat.getColor(context, R.color.amber_900));
            } else {
                //set the color of the icon when disabled
                holder.reminderIcon.setColorFilter(ContextCompat.getColor(context, R.color.blue_grey_800));

                // Disable alarm and notification
                Utility.cancelAlarm(context, docId);
            }

            // Update the alarm state in the Firestore document
            DocumentReference documentReference = Utility.getCollectionReferenceForReminders().document(docId);
            documentReference.update("alarmEnabled", isChecked);
        });

        holder.editBtn.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditDeleteReminderActivity.class);

            intent.putExtra("reminderTitle",reminderModel.reminderTitle);
            intent.putExtra("reminderTimestamp",reminderModel.reminderTimestamp);
            intent.putExtra("reminderDescription",reminderModel.reminderDescription);

            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);

            context.startActivity(intent);
        });

        // Expand or collapse the item based on the expandedPosition
        final boolean isExpanded = position == expandedPosition;
        holder.linearLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_reminder_item,parent,false);
        return new ReminderViewHolder(view);
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder{

        TextView reminderTitle;
        TextView reminderTimestampDate;
        TextView reminderTimestampTime;
        TextView reminderDescription;

        SwitchMaterial reminderToggle;

        ImageView reminderIcon;
        ImageView editBtn;
        LinearLayout linearLayout;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.expanded_reminder_menu);
            editBtn = itemView.findViewById(R.id.reminder_edit_btn);

            reminderTitle = itemView.findViewById(R.id.reminder_title);
            reminderTimestampDate = itemView.findViewById(R.id.reminder_timestamp_date);
            reminderTimestampTime = itemView.findViewById(R.id.reminder_timestamp_time);
            reminderDescription = itemView.findViewById(R.id.reminder_description);

            reminderToggle = itemView.findViewById(R.id.reminder_toggle);
            reminderIcon = itemView.findViewById(R.id.reminderSwitchIcon);

            itemView.setOnClickListener(view -> {
                if (expandedPosition == getBindingAdapterPosition()) {
                    expandedPosition = -1;
                    notifyItemChanged(getBindingAdapterPosition());
                } else {
                    if (expandedPosition != -1) {
                        int oldExpandedPosition = expandedPosition;
                        expandedPosition = -1;
                        notifyItemChanged(oldExpandedPosition);
                    }
                    expandedPosition = getBindingAdapterPosition();
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });
        }
    }
}