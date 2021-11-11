package com.sbitbd.fixedbd.Config;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Configuration;

import com.sbitbd.fixedbd.R;

public class service extends JobService {
    private static final String TAG = "JobService";
    private static final String CHANNEL_ID = "channel";
    private boolean jobcancelled = false;

    public service() {
        Configuration.Builder builder = new Configuration.Builder();
        builder.setJobSchedulerJobIdRange(0,1000);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG,"jobStarted");
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.buynfeel)
                .setContentTitle("Buy with inexpensive prices")
                .setContentText("get pure product by trusted fixed bd...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Get pure product by trusted fixed bd..."))
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1, builder.build());
//        startForeground(1,builder);
        dobackground(params);
        return true;
    }

    private void dobackground(JobParameters parameters){
        new Thread(() -> {
            for (int i = 0; i < 10; i++){
                Log.d(TAG,"job: "+i);
                if (jobcancelled)
                    return;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG,"job finished");
            jobFinished(parameters,true);
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG,"job cancelled");
        jobcancelled = true;
        return true;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
