package com.example.nyaritabor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;


public class NotificationHandler {
    private static final String CHANNEL_ID = "nyaritabor_csatorna";
    private final int NOTIFICATION_ID = 0;

    private NotificationManager mManager;
    private Context mContext;


    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel
                (CHANNEL_ID, "nyaritabor Notification", NotificationManager.IMPORTANCE_HIGH);

        channel.enableLights(true);
        channel.setLightColor(Color.rgb(66, 233, 245));
        channel.enableVibration(true);
        channel.setDescription("nyaritabor");

        mManager.createNotificationChannel(channel);
    }

    public void send(String message) {
        Intent intent = new Intent(mContext, TaboroklistajaActivity.class);
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(mContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(mContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Nyari taboraid:")
                .setContentText(message)
                .setSmallIcon(R.drawable.mernokos)
                .setContentIntent(pendingIntent);

        this.mManager.notify(NOTIFICATION_ID, builder.build());
    }

}
