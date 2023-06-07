package com.myproject.myvehicleapp.Utilities;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.myproject.myvehicleapp.DriveMate;
import com.myproject.myvehicleapp.R;

public class ReminderAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "ReminderAlarmReceiver";

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        // Create Notification
        createNotificationChannel(context);

        // Extract reminder data from the intent
        String reminderId = intent.getStringExtra("reminderId");
        String reminderTitle = intent.getStringExtra("reminderTitle");
        String reminderDescription = intent.getStringExtra("reminderDescription");

        if (reminderId == null) {
            Log.e(TAG, "Reminder ID is null. Skipping notification.");
            return;
        }

        // Create notification intent
        Intent notificationIntent = new Intent(context, DriveMate.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        // Create notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reminder_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(reminderTitle)
                .setContentText(reminderDescription)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(reminderDescription))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Retrieve the sound URI from the intent
        String soundUriString = intent.getStringExtra("soundUri");
        if (soundUriString != null) {
            Uri soundUri = Uri.parse(soundUriString);
            builder.setSound(soundUri); // Set the system alarm sound for the notification
        }

        // Show notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(reminderId.hashCode(), builder.build());

        Log.d(TAG, "Reminder notification sent: " + reminderTitle);
    }

    /**
     * Creates the notification channel for reminder notifications on Android Oreo and above.
     * @param context The application context.
     */
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "reminder_channel";
            CharSequence name = "Reminder Channel";
            String description = "Channel for reminder notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                channel.setDescription(description);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
