package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.HomeActivity;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository.BillDao;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository.RoomDatabase;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_icon)
                        .setContentTitle(context.getResources().getString(R.string.notification_title))
                        .setContentText(context.getResources().getString(R.string.notification_body))
                        .setAutoCancel(true)
                        .setContentIntent(PendingIntent.getActivity(context,
                                0, new Intent(context, HomeActivity.class), 0));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        notificationManager.notify(0, builder.build());
    }
}
