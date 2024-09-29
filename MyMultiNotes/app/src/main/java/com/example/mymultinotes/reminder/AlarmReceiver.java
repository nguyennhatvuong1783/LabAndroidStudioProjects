package com.example.mymultinotes.reminder;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mymultinotes.R;
import com.example.mymultinotes.activities.CreateNoteActivity;
import com.example.mymultinotes.entities.Note;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context.getApplicationContext(), intent);
    }

    @SuppressLint("MissingPermission")
    private void sendNotification(Context context, Intent intent) {
        int id = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String subtitle = intent.getStringExtra("subtitle");
        Note note = (Note) intent.getSerializableExtra("note");

        Intent resultIntent = new Intent(context, CreateNoteActivity.class);
        resultIntent.putExtra("isViewOrUpdate", true);
        resultIntent.putExtra("isNotification", true);
        resultIntent.putExtra("idOfNotification", id);
        resultIntent.putExtra("note", note);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(id,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setSmallIcon(R.drawable.ic_alarm)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id, notification);
    }
}
