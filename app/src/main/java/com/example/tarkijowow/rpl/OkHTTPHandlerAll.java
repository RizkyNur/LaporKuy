package com.example.tarkijowow.rpl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.json.JSONObject;

public class OkHTTPHandlerAll{

    private Context context;
    private static OkHTTPHandlerAll ourInstance;
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    public String CHANNEL_ID = "yolo";
    public int notificationId = 24;

    public static OkHTTPHandlerAll getInstance(Context c) {
        if(ourInstance == null){
            ourInstance = new OkHTTPHandlerAll(c.getApplicationContext());
        }
        return ourInstance;
    }

    public OkHTTPHandlerAll(Context c) {
        context = c;
    }

    public void executo(String url_Read){
        OkHttpHandler okHTTPHandler = new OkHttpHandler(new OkHttpHandler.OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {
                Log.d(getClass().toString(), "return from asynctask 1");
                createNotification(context);
            }
        }, context);
        okHTTPHandler.execute(url_Read);
    }

    public void executo2(String url_Create, JSONObject jsonObject){
        OkHttpHandler2 okHttpHandler2 = new OkHttpHandler2(new OkHttpHandler2.OnTaskCompleted2() {
            @Override
            public void onTaskCompleted2() {
                Log.d(getClass().toString(), "return from asynctask 2");
                //CrimeList.get(context.getApplicationContext()).saveCrime();
            }
        });
        okHttpHandler2.setJsonObject(jsonObject);
        okHttpHandler2.execute(url_Create);
    }

    private void createNotification(Context context){
        Intent intent = new Intent(context.getApplicationContext(),
                SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pIntent = PendingIntent.getBroadcast(context,
                (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.
                Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_cute)
                .setContentTitle("LaporKuy")
                .setContentText("Ada Kasus disekitarmu")
                .addAction(R.drawable.baseline_check_black_18dp, "Call", pIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Ada Kasus disekitarmu."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context.getApplicationContext());
        notificationManager.notify(notificationId, mBuilder.build());


        createNotificationChannel(context);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "kunam";
            String description = "munak";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
