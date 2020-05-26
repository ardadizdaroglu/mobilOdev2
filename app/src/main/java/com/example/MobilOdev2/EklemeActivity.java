package com.example.MobilOdev2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EklemeActivity extends AppCompatActivity {
    EditText txtbaslik,txtkategori,txtetiket;
    DatePicker pickerDate;
    TimePicker pickerTime;
    DatabaseReference reff;
    Gorev gorev;
    private Spinner spinnerRenk;
    ImageView logo,backImage,saveImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekleme);
        pickerDate = (DatePicker)findViewById(R.id.pickerdate);
        pickerTime = (TimePicker)findViewById(R.id.pickertime);
        logo = findViewById(R.id.imageRemindMeLogo);
        saveImage = findViewById(R.id.saveImage);
        backImage = findViewById(R.id.backImage);
        backImage.setOnClickListener(new View.OnClickListener() { //toolbar'da sol üstteki ok'a basılırsa
            public void onClick(View v) {
                Intent intent = new Intent(EklemeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//darkmode kontrolü
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //"Lolipop versiyonundan yeni mi" kontrolü
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_darkmode, getApplicationContext().getTheme()));
                saveImage.setImageDrawable(getResources().getDrawable(R.drawable.save_darkmode, getApplicationContext().getTheme()));
                backImage.setImageDrawable(getResources().getDrawable(R.drawable.arrow_darkmode, getApplicationContext().getTheme()));
            }
            else
            {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_darkmode));
                saveImage.setImageDrawable(getResources().getDrawable(R.drawable.save_darkmode));
                backImage.setImageDrawable(getResources().getDrawable(R.drawable.arrow_darkmode));
            }
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //"Lolipop versiyonundan yeni mi" kontrolü
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_lightmode, getApplicationContext().getTheme()));
                saveImage.setImageDrawable(getResources().getDrawable(R.drawable.save_lightmode, getApplicationContext().getTheme()));
                backImage.setImageDrawable(getResources().getDrawable(R.drawable.arrow_lightmode, getApplicationContext().getTheme()));
            }
            else
            {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_lightmode));
                saveImage.setImageDrawable(getResources().getDrawable(R.drawable.save_lightmode));
                backImage.setImageDrawable(getResources().getDrawable(R.drawable.arrow_lightmode));
            }
        }
//darkmode kontrolü bitiş

//renk spinneri ayarları
        spinnerRenk = findViewById(R.id.spinnerRenk);
        final List<String> myList = new ArrayList<>();
        myList.add("Magenta");
        myList.add("Mavi");
        final ArrayAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { //renk spinnerinin her bir elemanını darkmode'ye göre ayarlanması
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                if (isDarkModeOn) {
                    text.setTextColor(Color.WHITE);
                }
                else if (isDarkModeOn == false) {
                    text.setTextColor(Color.BLACK);
                }

                return view;
            }
        };
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRenk.setAdapter(myAdapter);
//renk spinneri ayarları bitiş

// görev ekleneceği zaman, ekleme ekranındaki datepicker ve timepickeri şuanki zamana ayarlıyoruz
        Calendar now = Calendar.getInstance();
        pickerDate.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                null);

        pickerTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        pickerTime.setCurrentMinute(now.get(Calendar.MINUTE));
//ekleme ekranındaki datepicker ve timepicker bitiş

        txtbaslik = (EditText) findViewById(R.id.edit2);
        txtkategori = (EditText) findViewById(R.id.edit4);
        txtetiket = (EditText) findViewById(R.id.edit5);
        gorev = new Gorev(); //yeni bir Gorev classı
        reff = FirebaseDatabase.getInstance().getReference().child("Gorevler"); // firebase referansı

//kaydet butonuna basıldığında
        saveImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(txtbaslik.getText().toString().equals("") || spinnerRenk.getSelectedItem().toString().equals("") || txtkategori.getText().toString().equals("") || txtetiket.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "LUTFEN ALANLARI EKSIKSIZ DOLDURUN", Toast.LENGTH_LONG).show();
                }//herhangi bir alan boş ise
                else{//bütün alanlar doluysa
                    String mytarih = Integer.toString(pickerDate.getYear()) + "-" + Integer.toString(pickerDate.getMonth()+1) + "-" + Integer.toString(pickerDate.getDayOfMonth());
                    String mysaat = Integer.toString(pickerTime.getCurrentHour()) + ":" + Integer.toString(pickerTime.getCurrentMinute());
                    String mybaslik = txtbaslik.getText().toString().trim();
                    String myrenk = spinnerRenk.getSelectedItem().toString().trim();
                    String mykategori = txtkategori.getText().toString().trim();
                    String myetiket = txtetiket.getText().toString().trim();
                    int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE); //her bir alarm'ın intenti için farklı bir id tanımlamak gerekiyor
                    gorev.setBaslik(mybaslik);
                    gorev.setTarih(mytarih);
                    gorev.setSaat(mysaat);
                    gorev.setRenk(myrenk);
                    gorev.setKategori(mykategori);
                    gorev.setEtiket(myetiket);
                    gorev.setTamamlandi("Tamamlanmadi");
                    gorev.setId(Integer.toString(m));
                    reff.child(mytarih).child(mysaat).setValue(gorev);
                    Toast.makeText(getApplicationContext(), "Ekleme islemi tamamlandi", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EklemeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
//kaydet butonu bitiş
    }
    @Override
    public void onBackPressed() { //telefondaki geri tuşuna bastılırsa
        Intent intent = new Intent(EklemeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
