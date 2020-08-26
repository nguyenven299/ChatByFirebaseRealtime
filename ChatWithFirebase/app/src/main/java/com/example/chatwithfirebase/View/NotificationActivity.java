package com.example.chatwithfirebase.View;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.chatwithfirebase.View.UI.ProfileFragment;
import com.example.chatwithfirebase.R;

import static com.example.chatwithfirebase.View.MainActivity.CHANEL_ID;

public class NotificationActivity {
    public static void Notification(Context context,String title,String body)
    {
        Intent intent = new Intent(context, ProfileFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                .setSmallIcon(R.drawable.ic_action_send)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,builder.build());

    }
}
