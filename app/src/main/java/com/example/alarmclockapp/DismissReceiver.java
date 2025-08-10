package com.example.alarmclockapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

public class DismissReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Stop alarm sound
        if (AlarmReceiver.ringtone != null && AlarmReceiver.ringtone.isPlaying()) {
            AlarmReceiver.ringtone.stop();
        }

        // Cancel notification
        NotificationManagerCompat.from(context).cancel(100);
    }
}
