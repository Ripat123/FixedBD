package com.sbitbd.fixedbd.Config;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Configuration;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixedbd.Adapter.four_model;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.product_controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class service extends JobService {
    private static final String TAG = "JobService";
    private static final String CHANNEL_ID = "channel";
    private boolean jobcancelled = false;
    private DoConfig config = new DoConfig();
    private four_model four_model;
    private product_controller product_controller = new product_controller();

    public service() {
        Configuration.Builder builder = new Configuration.Builder();
        builder.setJobSchedulerJobIdRange(0, 1000);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "jobStarted");
        createNotificationChannel();

//        startForeground(1,builder);
        initData("select id as 'one',title as 'two',description as 'three' from notification " +
                "WHERE start_date <= curdate() and end_date >= curdate() order by id desc limit 1");
//        dobackground(params);
        return true;
    }

    private void dobackground(JobParameters parameters) {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Log.d(TAG, "job: " + i);
                if (jobcancelled)
                    return;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            Log.d(TAG,"job finished");
//            jobFinished(parameters,false);
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "job cancelled");
        jobcancelled = true;
        return true;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void initData(String sql) {
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DMS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                try {
                                    String id = product_controller.getPref("notification_id", service.this);
                                    JSONObject jsonObject = new JSONObject(response.trim());
                                    JSONArray result = jsonObject.getJSONArray(config.RESULT);
                                    JSONObject collegeData = result.getJSONObject(0);
                                    String notiid = collegeData.getString(config.ONE);
                                    if (id != null) {
                                        if (Integer.parseInt(notiid) > Integer.parseInt(id)) {
                                            notification(collegeData.getString(config.TWO),collegeData.getString(config.THREE));
                                            product_controller.putPref("notification_id", notiid, service.this);
                                        }
                                    } else {
                                        notification(collegeData.getString(config.TWO),collegeData.getString(config.THREE));
                                        product_controller.putPref("notification_id", notiid, service.this);
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(service.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(service.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } catch (Exception e) {
        }
    }

    private void notification(String title,String description){
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.buynfeel)
                .setContentTitle(Html.fromHtml(title))
                .setContentText(Html.fromHtml(description))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Html.fromHtml(description)))
                .setAutoCancel(false)
                .setVibrate(new long[]{1000, 1000})
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_PROMO)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1, builder.build());
    }
}
