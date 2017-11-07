package com.example.hacunamatata.rikkeisoft_assignment.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.hacunamatata.rikkeisoft_assignment.R;
import com.example.hacunamatata.rikkeisoft_assignment.activity.MainActivity;

public class AlarmService extends IntentService {

    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sendNotification("*****RENG RENG RENG*****");
    }

    private void sendNotification(String message) {
        Log.d("AlarmService", "Preparing to send notification...: " + message);
        alarmNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this).setContentTitle("Alarm")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentText(message);

        builder.setContentIntent(pendingIntent);
        alarmNotificationManager.notify(1, builder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}
