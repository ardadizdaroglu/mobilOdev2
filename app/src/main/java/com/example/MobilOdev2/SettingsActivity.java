package com.example.MobilOdev2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;

    private Button btnToggleDark;

    SharedPreferences LastSelectSiklik, LastSelectSes;
    SharedPreferences.Editor myEditorSiklik, myEditorSes;
    private Spinner spinnerSiklik, spinnerSes;
    ImageView logo,okayImage;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logo = findViewById(R.id.imageRemindMeLogo);
        okayImage = findViewById(R.id.okayImage);

        btnToggleDark = findViewById(R.id.btnToggleDark);

//darkmode kontrol
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            btnToggleDark.setText("Dark Mode Kapat");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //"Lolipop versiyonundan yeni mi" kontrolü
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_darkmode, getApplicationContext().getTheme()));
                okayImage.setImageDrawable(getResources().getDrawable(R.drawable.okay_darkmode, getApplicationContext().getTheme()));
            }
            else
            {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_darkmode));
                okayImage.setImageDrawable(getResources().getDrawable(R.drawable.okay_darkmode));
            }
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            btnToggleDark.setText("Dark Mode Aktif Et");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //"Lolipop versiyonundan yeni mi" kontrolü
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_lightmode, getApplicationContext().getTheme()));
                okayImage.setImageDrawable(getResources().getDrawable(R.drawable.okay_lightmode, getApplicationContext().getTheme()));
            }
            else
            {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_lightmode));
                okayImage.setImageDrawable(getResources().getDrawable(R.drawable.okay_lightmode));
            }
        }
//darkmode kontrol bitiş

//sıklık spinneri ayarları
        LastSelectSiklik = getSharedPreferences("LastSettingSiklik", Context.MODE_PRIVATE);
        myEditorSiklik = LastSelectSiklik.edit();
        final int LastClickSimpleSpinner = LastSelectSiklik.getInt("LastClickSpinnerSiklik",0);
        spinnerSiklik = findViewById(R.id.siklik);
        final List<String> myList = new ArrayList<>();
        myList.add("DAKIKADA 1");
        myList.add("5 DAKIKADA 1");
        ArrayAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
        spinnerSiklik.setAdapter(myAdapter);
        spinnerSiklik.setSelection(LastClickSimpleSpinner);
        spinnerSiklik.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                myEditorSiklik.putInt("LastClickSpinnerSiklik",position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });
//sıklık spinneri ayarları bitiş

//ses spinneri ayarları
        LastSelectSes = getSharedPreferences("LastSettingSes", Context.MODE_PRIVATE);
        myEditorSes = LastSelectSes.edit();
        final int LastClickSimpleSpinnerSes = LastSelectSes.getInt("LastClickSpinnerSes",0);
        spinnerSes = findViewById(R.id.ses);
        final List<String> myList2 = new ArrayList<>();
        myList2.add("SES 1");
        myList2.add("SES 2");
        ArrayAdapter myAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myList2){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSes.setAdapter(myAdapter2);
        spinnerSes.setSelection(LastClickSimpleSpinnerSes);
        spinnerSes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                myEditorSes.putInt("LastClickSpinnerSes",position).apply();
                //Toast.makeText(getApplicationContext(), "Position" + position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });
//ses spinneri ayarları bitiş

        //dark mode butonuna basılırsa
        btnToggleDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDarkModeOn) {

                    // dark mode aktifse, kapanacak
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    // neredeyse bütün activitylerde çağırdığımız isDarkModeOn booleanını false yapacak
                    editor.putBoolean("isDarkModeOn", false);
                    editor.apply();

                    // butondaki metni değiştir
                    btnToggleDark.setText("Dark Mode Aktif Et");
                }
                else {

                    // dark mode kapalıysa, açacak
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    // neredeyse bütün activitylerde çağırdığımız isDarkModeOn booleanını true yapacak
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();

                    // butondaki metni değiştir
                    btnToggleDark.setText("Dark Mode Kapat");
                }
            }
        });
        okayImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //toolbardaki okay butonuna basılırsa
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
