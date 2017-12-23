package exa.free.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import exa.free.bbin.MainUI;
import exa.free.bbin.R;

public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){

        int i = context.getSharedPreferences("GlobalPreferences", 0).getInt("Version", 0);
        int i2 = Integer.valueOf(context.getString(R.string.version));

        if(i != 0 && i < i2){

            if(Build.VERSION.SDK_INT >= 16){
                Intent mIntent = new Intent(context, MainUI.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 1232129, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification.Builder builder = new Notification.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("New Busybox Binary Available")
                        .setContentText("Tap here to update")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                builder.build().flags |= Notification.FLAG_AUTO_CANCEL;

                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1282129, builder.build());
            }else{
                Intent mIntent = new Intent(context, MainUI.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 1232129, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("New Busybox Binary Available")
                        .setContentText("Tap here to update")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                builder.build().flags |= Notification.FLAG_AUTO_CANCEL;

                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1282129, builder.build());
            }
        }
    }
}
