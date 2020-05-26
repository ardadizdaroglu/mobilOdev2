package com.example.MobilOdev2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuncellemeSilmeActivity extends AppCompatActivity {
    private String message, alarmId;
    private DatabaseReference databaseReference;
    EditText txtbaslik,txtkategori,txtetiket;
    DatePicker pickerDate;
    TimePicker pickerTime;
    private Spinner spinnerTamamlandi, spinnerRenk;
    ImageView logo,backImage,deleteImage,shareImage,guncelleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guncelleme_silme);
        final Gorev[] gorev = new Gorev[1];
        txtbaslik = (EditText) findViewById(R.id.edit2);
        txtkategori = (EditText) findViewById(R.id.edit4);
        txtetiket = (EditText) findViewById(R.id.edit5);
        logo = findViewById(R.id.imageRemindMeLogo);
        backImage = findViewById(R.id.backImage);
        deleteImage = findViewById(R.id.deleteImage);
        shareImage = findViewById(R.id.shareImage);
        guncelleImage = findViewById(R.id.updateImage);

//darkmode kontrolü
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //"Lolipop versiyonundan yeni mi" kontrolü
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_darkmode, getApplicationContext().getTheme()));
                backImage.setImageDrawable(getResources().getDrawable(R.drawable.arrow_darkmode, getApplicationContext().getTheme()));
                deleteImage.setImageDrawable(getResources().getDrawable(R.drawable.delete_darkmode, getApplicationContext().getTheme()));
                shareImage.setImageDrawable(getResources().getDrawable(R.drawable.share_darkmode, getApplicationContext().getTheme()));
                guncelleImage.setImageDrawable(getResources().getDrawable(R.drawable.update_darkmode, getApplicationContext().getTheme()));
            }
            else
            {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_darkmode));
                backImage.setImageDrawable(getResources().getDrawable(R.drawable.arrow_darkmode));
                deleteImage.setImageDrawable(getResources().getDrawable(R.drawable.delete_darkmode));
                shareImage.setImageDrawable(getResources().getDrawable(R.drawable.share_darkmode));
                guncelleImage.setImageDrawable(getResources().getDrawable(R.drawable.update_darkmode));
            }
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //"Lolipop versiyonundan yeni mi" kontrolü
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_lightmode, getApplicationContext().getTheme()));
                backImage.setImageDrawable(getResources().getDrawable(R.drawable.arrow_lightmode, getApplicationContext().getTheme()));
                deleteImage.setImageDrawable(getResources().getDrawable(R.drawable.delete_lightmode, getApplicationContext().getTheme()));
                shareImage.setImageDrawable(getResources().getDrawable(R.drawable.share_lightmode, getApplicationContext().getTheme()));
                guncelleImage.setImageDrawable(getResources().getDrawable(R.drawable.update_lightmode, getApplicationContext().getTheme()));
            }
            else
            {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_lightmode));
                backImage.setImageDrawable(getResources().getDrawable(R.drawable.arrow_lightmode));
                deleteImage.setImageDrawable(getResources().getDrawable(R.drawable.delete_lightmode));
                shareImage.setImageDrawable(getResources().getDrawable(R.drawable.share_lightmode));
                guncelleImage.setImageDrawable(getResources().getDrawable(R.drawable.update_lightmode));
            }
        }
//darkmode kontrolü bitiş

//tamamlandi spinneri ayarları
        spinnerTamamlandi = findViewById(R.id.spinnerTamamlandi);
        final List<String> myList = new ArrayList<>();
        myList.add("Tamamlandi");
        myList.add("Tamamlanmadi");
        final ArrayAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {//tamamlandi spinnerinin her bir elemanını darkmode'ye göre ayarlanması
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
        spinnerTamamlandi.setAdapter(myAdapter);
//tamamlandi spinneri ayarları bitiş

//renk spinneri ayarları
        spinnerRenk = findViewById(R.id.spinnerRenk2);
        final List<String> myList3 = new ArrayList<>();
        myList3.add("Magenta");
        myList3.add("Mavi");
        final ArrayAdapter myAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myList3){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {//renk spinnerinin her bir elemanını darkmode'ye göre ayarlanması
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
        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRenk.setAdapter(myAdapter3);
//renk spinneri ayarları bitiş

        pickerDate = (DatePicker)findViewById(R.id.pickerdate);
        pickerTime = (TimePicker)findViewById(R.id.pickertime);
        Intent intent = getIntent();
        message = intent.getStringExtra("EXTRA_MESSAGE"); //main'den gelen tam tarih ve başlık (tarih + " " +baslik)
        final String[] splited = message.split("\\s+"); //" "'ya göre split edildi
        //Toast.makeText(getApplicationContext(), "1:" + splited[0] + "2:" + splited[1] , Toast.LENGTH_LONG).show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Gorevler").child(splited[0]).child(splited[1]); //splited[0]=2020-5-26 splited[1]=12:36
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){ //Gorevler-->Tarih-->Saat içerisindeki her bir key ve value kontrolü
                    String k = dataSnapshot1.getKey();
                    String v = (String) dataSnapshot1.getValue();
                    assert k != null;
                    if (k.equals("baslik")){
                        String t = v;
                        txtbaslik.setText(t);
                    }
                    else if (k.equals("etiket")){
                        String t = v;
                        txtetiket.setText(t);
                    }
                    else if (k.equals("id")){
                        String t = v;
                        alarmId = t;
                    }
                    else if (k.equals("kategori")){
                        String t = v;
                        txtkategori.setText(t);
                    }
                    else if (k.equals("renk")){
                        String t = v;
                        int spinnerPosition = myAdapter3.getPosition(t);
                        spinnerRenk.setSelection(spinnerPosition);
                    }
                    else if (k.equals("tamamlandi")){
                        String t = v;
                        int spinnerPosition = myAdapter.getPosition(t);
                        spinnerTamamlandi.setSelection(spinnerPosition);
                    }
                    else if (k.equals("saat")){
                        String t = v;
                        String[] splited_hour = t.split(":");
                        pickerTime.setCurrentHour(Integer.parseInt(splited_hour[0]));
                        pickerTime.setCurrentMinute(Integer.parseInt(splited_hour[1]));
                    }
                    else if (k.equals("tarih")){
                        String t = v;
                        String[] splited_date = t.split("-");
                        pickerDate.updateDate(Integer.parseInt(splited_date[0]), (Integer.parseInt(splited_date[1]))-1, Integer.parseInt(splited_date[2]));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        backImage.setOnClickListener(new View.OnClickListener() { //toolbarda sol üstteki geri tuşuna basılırsa
            public void onClick(View v) {
                Intent intent = new Intent(GuncellemeSilmeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        deleteImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //toolbardaki delete tuşuna basılırsa
                AlertDialog.Builder builder = new AlertDialog.Builder(GuncellemeSilmeActivity.this); //emin misiniz mesajı
                builder.setMessage("Silmek istediğine emin misin?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() { //evete basarsak
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //var olan eski alarmı silme
                                        Intent myintent = new Intent(getApplicationContext(), AlarmReceiver.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(alarmId), myintent, 0);
                                        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                                        alarmManager.cancel(pendingIntent);
                                        //var olan eski alarmı silme bitiş
                                        databaseReference.removeValue(); //eski alarmı databaseden silme
                                        Toast.makeText(getApplicationContext(), "Silme islemi tamamlandi", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(GuncellemeSilmeActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {//hayıra basarsak popoupu kaldırıyor
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        guncelleImage.setOnClickListener(new View.OnClickListener() {//toolbarda sağ üstteki güncelleme butonuna basarsak
            public void onClick(View v) {
                //var olan eski alarmı silme
                Intent myintent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(alarmId), myintent, 0);
                AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                //var olan eski alarmı silme bitiş
                databaseReference.removeValue(); //eski alarmı databaseden silme
                databaseReference = FirebaseDatabase.getInstance().getReference("Gorevler"); //yeni ref //buradan itibaren ekleme ekranındaki gibi aynı mantık
                gorev[0] = new Gorev();
                String mytarih = Integer.toString(pickerDate.getYear()) + "-" + Integer.toString(pickerDate.getMonth()+1) + "-" + Integer.toString(pickerDate.getDayOfMonth());
                String mysaat = Integer.toString(pickerTime.getCurrentHour()) + ":" + Integer.toString(pickerTime.getCurrentMinute());
                String mybaslik = txtbaslik.getText().toString().trim();
                String myrenk = spinnerRenk.getSelectedItem().toString().trim();
                String mykategori = txtkategori.getText().toString().trim();
                String myetiket = txtetiket.getText().toString().trim();
                String mytamamlandi = spinnerTamamlandi.getSelectedItem().toString().trim();
                int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                gorev[0].setBaslik(mybaslik);
                gorev[0].setTarih(mytarih);
                gorev[0].setSaat(mysaat);
                gorev[0].setRenk(myrenk);
                gorev[0].setKategori(mykategori);
                gorev[0].setEtiket(myetiket);
                gorev[0].setTamamlandi(mytamamlandi);
                gorev[0].setId(Integer.toString(m));
                databaseReference.child(mytarih).child(mysaat).setValue(gorev[0]);
                Toast.makeText(getApplicationContext(), "Guncelleme islemi tamamlandi", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GuncellemeSilmeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        shareImage.setOnClickListener(new View.OnClickListener() { //toolbardaki paylaş butonunas basarsak //DİPNOT: WHATSAPP, TWITTER, EMAIL uygulamalarında test edildi ve çalışıyor
            public void onClick(View v) {
                String mytarih = Integer.toString(pickerDate.getYear()) + "-" + Integer.toString(pickerDate.getMonth()+1) + "-" + Integer.toString(pickerDate.getDayOfMonth());
                String mysaat = Integer.toString(pickerTime.getCurrentHour()) + ":" + Integer.toString(pickerTime.getCurrentMinute());
                String mybaslik = txtbaslik.getText().toString().trim();
                String myrenk = spinnerRenk.getSelectedItem().toString().trim();
                String mykategori = txtkategori.getText().toString().trim();
                String myetiket = txtetiket.getText().toString().trim();
                String mytamamlandi = spinnerTamamlandi.getSelectedItem().toString().trim();
                String strPaylas = mytarih + " " + mysaat + " " +mybaslik + " " +myrenk + " " +mykategori + " " +myetiket + " " +mytamamlandi;
                Intent myintent = new Intent(Intent.ACTION_SEND);
                myintent.setType("text/plain");
                myintent.putExtra(Intent.EXTRA_SUBJECT, "REMIND ME APP");
                myintent.putExtra(Intent.EXTRA_TEXT, strPaylas);
                startActivity(Intent.createChooser(myintent, "Araciligiyla gonder..."));
            }
        });
    }

    @Override
    public void onBackPressed() { //telefondaki geri tuşuna basılırsa
        Intent intent = new Intent(GuncellemeSilmeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
