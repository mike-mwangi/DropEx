package com.example.driverapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.driverapplication.Service.TrackingService;

public class JobNotification  {
    /* The unique identifier for this type of notification.


     */
    private static final String NOTIFICATION_TAG = "JOB";


    public static void notify(final Context context,
                              final String title, final String text,String userID,String jobID){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1){
            notifyPre(context, title, text,userID,jobID);
        }else{
            notifyO(context, title, text,userID,jobID);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void notifyO(Context context, final String title, final String text,String userID,String jobID) {
        String channelId = createLocationChannel(context);
        Intent iStopService = new Intent(context, NavigationLauncherActivity.class);
        iStopService.putExtra("key", "Accept");
        iStopService.putExtra("userID",userID);
        iStopService.putExtra("JOBID",jobID);
        Uri uri=new Uri.Builder().appendQueryParameter("JOBID",jobID).appendQueryParameter("userID",userID).build();
        iStopService.setData(uri);
        PendingIntent piStopService = PendingIntent.getActivity(
                context, 0, iStopService, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Action action= new Notification.Action.Builder(R.mipmap.ic_launcher_foreground,"Accept",piStopService).build();

        Notification notification = new Notification.Builder(context, channelId)

                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)

                .setPriority(Notification.PRIORITY_DEFAULT)

                .setContentIntent(piStopService)


                .addAction(
                        R.mipmap.ic_launcher_foreground,"Accept",piStopService)

                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(false).build();
        notify(context, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String createLocationChannel(Context ctx) {
        // Create a channel.
        NotificationManager notificationManager =
                (NotificationManager)
                        ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence channelName = ctx.getString(R.string.channel_id);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel =
                new NotificationChannel(
                        ctx.getString(R.string.channel_id), channelName, importance);

        notificationManager.createNotificationChannel(
                notificationChannel);
        return ctx.getString(R.string.channel_id);
    }


    public static void notifyPre(final Context context,
                                 final String title, final String text,String userID,String jobID) {
        final Resources res = context.getResources();

        Intent iStopService = new Intent(context, NavigationLauncherActivity.class);
        iStopService.putExtra("key", "stop");
        iStopService.putExtra("userID",userID);
        iStopService.putExtra("JOBID",jobID);
        Uri uri=new Uri.Builder().appendQueryParameter("JOBID",jobID).appendQueryParameter("userID",userID).build();
        iStopService.setData(uri);
        PendingIntent piStopService = PendingIntent.getActivity(
                context, 0, iStopService,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action action= new NotificationCompat.Action.Builder(R.mipmap.ic_launcher_foreground,"Accept",piStopService).build();
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)

                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)


                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                .setContentIntent(piStopService)

                .addAction(
                       action)

                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(false);

        notify(context, builder.build());
    }

    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_TAG, 0, notification);
    }


    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_TAG, 0);
    }
}
