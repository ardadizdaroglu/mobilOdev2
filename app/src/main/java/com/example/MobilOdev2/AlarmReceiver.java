package com.example.MobilOdev2;

import android.content.BroadcastReceiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private String CHANNEL_ID = "channel_id";
    private int NOTIFICATION_ID;
    private String CHANNEL_NAME = "Notification name";
    public int NOTIFICIATION_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;
    Uri sound;
    int LastClickSpinnerSes;
    @Override
    public void onReceive(Context context, Intent intent) {
        //String sesText = intent.getStringExtra("SES_ID");
        //int sesInt = Integer.parseInt(sesText);

        SharedPreferences sharedPreferences = context.getSharedPreferences("LastSettingSes", Context.MODE_PRIVATE); //ayarlar ekranında ses spinnerinden gelen data
        LastClickSpinnerSes = sharedPreferences.getInt("LastClickSpinnerSes",-1); //ayarlar ekranında ses spinnerinden gelen data

        String baslikText = intent.getStringExtra("BASLIK_ID"); //main'deki alarmmanager aracılığıyla gelen intentteki mesajlar
        String tarihText = intent.getStringExtra("TARIH_SAAT_ID"); //main'deki alarmmanager aracılığıyla gelen intentteki mesajlar
        //Toast.makeText(context, "SesNo:" + LastClickSpinnerSes, Toast.LENGTH_LONG).show();
        //Random random = new Random();
        NOTIFICATION_ID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE); //birçok bildirimin çıkması için her birinin farklı IDlere sahip olması lazım, tek yol bu şekilde

        if (LastClickSpinnerSes == 0){ //son seçilen ses
            sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.swiftly); //ses1 seçildiyse swiftly adlı ses çalacak.
        }
        else{
            sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.got_it_done); //ses2 seçildiyse got_it_done adlı ses çalacak.
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { //notification ve notification channel ayarlamaları
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NOTIFICIATION_IMPORTANCE);
            notificationChannel.setDescription("This is a notification channel");

            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build(); //notificationa ses eklenmesi
            notificationChannel.setSound(sound, attributes);
            notificationChannel.enableVibration(true);
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent myIntent = new Intent(context, NotificationActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myIntent.putExtra("BASLIK_ID", baslikText);
        myIntent.putExtra("TARIH_SAAT_ID", tarihText);
        @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(context,0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(baslikText) //notificationdaki başlık
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(tarihText + " zamanina kurmustunuz.")) //notificationdaki metin
                .setVibrate(new long[]{0, 500, 1000}) //titreşim ayarlaması
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());

    }


}
