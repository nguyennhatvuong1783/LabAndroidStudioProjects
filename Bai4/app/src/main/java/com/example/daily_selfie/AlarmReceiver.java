package com.example.daily_selfie;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 1;

    // Notification action elements
    private Intent notificationIntent;
    private PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            notificationIntent = new Intent(context, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);

            // Build notification
            Notification.Builder notificationBuilder = new Notification.Builder(context)
                    .setTicker("Time for another selfie")
                    .setSmallIcon(R.drawable.ic_camera)
                    .setAutoCancel(true)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText("Time for another selfie")
                    .setContentIntent(pendingIntent);
            //.setSound(soundURI);

            String channelId = "ALARM";
            NotificationChannel channel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                channel = new NotificationChannel(
                        channelId,
                        "Your alarm is here",
                        NotificationManager.IMPORTANCE_HIGH);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
                notificationBuilder.setChannelId(channelId);
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            }
            // Get NotificationManager
//            Toast.makeText(context, "Notification", Toast.LENGTH_LONG).show();
        }
        catch (Exception exception) {
            Log.d("NOTIFICATION", exception.getMessage().toString());
        }
    }
}
