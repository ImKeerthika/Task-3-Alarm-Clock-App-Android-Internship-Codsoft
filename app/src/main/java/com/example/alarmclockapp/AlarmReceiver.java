package com.example.alarmclockapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public static Ringtone ringtone;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get alarm sound
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        // Play ringtone
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        // Snooze action
        Intent snoozeIntent = new Intent(context, SnoozeReceiver.class);
        PendingIntent snoozePending = PendingIntent.getBroadcast(
                context, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Dismiss action
        Intent dismissIntent = new Intent(context, DismissReceiver.class);
        PendingIntent dismissPending = PendingIntent.getBroadcast(
                context, 2, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create Notification Channel (Android 8+)
        NotificationChannel channel = new NotificationChannel(
                "alarmChannel",
                "Alarm Notifications",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarmChannel")
                .setSmallIcon(R.drawable.ic_alarm) // Make sure you have this icon in drawable
                .setContentTitle("Alarm is ringing!")
                .setContentText("Tap Snooze or Dismiss")
                .addAction(R.drawable.ic_snooze, "Snooze", snoozePending)
                .addAction(R.drawable.ic_dismiss, "Dismiss", dismissPending)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Show notification
        NotificationManagerCompat.from(context).notify(100, builder.build());
    }
}
