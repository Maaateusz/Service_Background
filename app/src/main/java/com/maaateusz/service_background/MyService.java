package com.maaateusz.service_background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.renderscript.RenderScript;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private String TAG = MyService.class.getSimpleName();
    private Handler handler = new Handler();
    private int i=0;
    private static  final int ONGOING_NOTIFICATION_ID = 2137;
    private String channelId;
    //PowerManager.WakeLock wakeLock;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

//        PowerManager mgr = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
//        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
//        wakeLock.acquire();

        setNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        handler.postDelayed(runnable, 1000);
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        stopForeground(true);
        Log.d(TAG, "onDestroy");
        //wakeLock.release();
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            i++;
            Log.d(TAG, "handler.postDelayed: " + i);
            handler.postDelayed(this, 1000);
        }
    };

    public void setNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        //Notification notification = new Notification.Builder(this, channelId) //CHANNEL_DEFAULT_IMPORTANCE // @RequiresApi(api = Build.VERSION_CODES.O)
        Notification notification = new NotificationCompat.Builder(this, channelId)
                        .setContentTitle("Content Title")
                        .setContentText("Content Text")
                        .setContentIntent(pendingIntent)
                        .setTicker("Ticker")
                        .setOnlyAlertOnce(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE) //shows basic information, such as the notification's icon and the content title, but hides the notification's full content.
                        .setCategory(NotificationCompat.CATEGORY_SERVICE)
                        .setSound(null)
                        .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification); //ONGOING_NOTIFICATION_ID
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager){
        String channelId = "my_service_channel_id";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

}

//https://androidwave.com/foreground-service-android-example/