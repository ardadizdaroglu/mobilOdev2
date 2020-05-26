package com.example.MobilOdev2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity { //bildirime tıklandığında bu activity açılıyor

    TextView baslik,tarih;
    ImageView okay,logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        baslik = findViewById(R.id.txtBaslik);
        tarih = findViewById(R.id.txtTarih);
        okay = findViewById(R.id.okayImage2);
        logo = findViewById(R.id.imageRemindMeLogo);

        Intent intent = getIntent();
        String baslikText = intent.getStringExtra("BASLIK_ID"); //alarmreceiver'den gelen intentteki mesajlar
        String tarihText = intent.getStringExtra("TARIH_SAAT_ID"); //alarmreceiver'den gelen intentteki mesajlar

//darkmode kontrol
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //"Lolipop versiyonundan yeni mi" kontrolü
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_darkmode, getApplicationContext().getTheme()));
                okay.setImageDrawable(getResources().getDrawable(R.drawable.okay_darkmode, getApplicationContext().getTheme()));
            }
            else
            {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_darkmode));
                okay.setImageDrawable(getResources().getDrawable(R.drawable.okay_darkmode));
            }
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //"Lolipop versiyonundan yeni mi" kontrolü
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_lightmode, getApplicationContext().getTheme()));
                okay.setImageDrawable(getResources().getDrawable(R.drawable.okay_lightmode, getApplicationContext().getTheme()));
            }
            else
            {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_lightmode));
                okay.setImageDrawable(getResources().getDrawable(R.drawable.okay_lightmode));
            }
        }
//darkmode kontrol bitiş
        baslik.setText(baslikText);
        tarih.setText(tarihText);
        okay.setOnClickListener(new View.OnClickListener() { //sağ üstteki okay tuşuna basılırsa
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
