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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    List<String> liststring, listTempDays, listAllDays, listWillShown ;
    ArrayAdapter<String> adapter ;
    SimpleAdapter simpleAdapter;
    ListView listView ;
    private Spinner spinnerSiklik;
    TextView tvTamamlandiSayac;
    ImageView logo,plusImage,settingsImage;

//custom listview için gerekli arrayler
    int[] listviewImage = new int[]{
            R.drawable.false_png, R.drawable.true_png,
    };
    String[] from = {"listview_okay", "listview_title", "listview_description"};
    int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};
//custom listview için gerekli arrayler bitiş

    int say;
    int LastClickSpinnerSiklik;
    int LastClickSpinnerSes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        tvTamamlandiSayac = findViewById(R.id.txtTamamlandi);
        liststring = new ArrayList<String>();
        listTempDays = new ArrayList<String>();
        listAllDays = new ArrayList<String>();
        listWillShown = new ArrayList<String>();
        final List<HashMap<String, String>> aList2 = new ArrayList<HashMap<String, String>>();
        listView = (ListView)findViewById(R.id.listview1);
        logo = findViewById(R.id.imageRemindMeLogo);
        plusImage = findViewById(R.id.plusImage);
        plusImage.setOnClickListener(new View.OnClickListener() { //+ resmine tıklanırsa
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EklemeActivity.class);
                startActivity(intent);
            }
        });
        settingsImage = findViewById(R.id.settingsImage);
        settingsImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //ayarlar resmine tıklanırsa
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

//dark mode kontrol
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_darkmode, getApplicationContext().getTheme()));
                plusImage.setImageDrawable(getResources().getDrawable(R.drawable.plus_darkmode, getApplicationContext().getTheme()));
                settingsImage.setImageDrawable(getResources().getDrawable(R.drawable.settings_darkmode, getApplicationContext().getTheme()));
            }
            else
            {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_darkmode));
                plusImage.setImageDrawable(getResources().getDrawable(R.drawable.plus_darkmode));
                settingsImage.setImageDrawable(getResources().getDrawable(R.drawable.settings_darkmode));
            }
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_lightmode, getApplicationContext().getTheme()));
                plusImage.setImageDrawable(getResources().getDrawable(R.drawable.plus_lightmode, getApplicationContext().getTheme()));
                settingsImage.setImageDrawable(getResources().getDrawable(R.drawable.settings_lightmode, getApplicationContext().getTheme()));
            }
            else
            {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.remind_me_lightmode));
                plusImage.setImageDrawable(getResources().getDrawable(R.drawable.plus_lightmode));
                settingsImage.setImageDrawable(getResources().getDrawable(R.drawable.settings_lightmode));
            }
        }
//dark mode kontrol bitiş

//ayarlar ekranındaki sıklık ve ses spinnerlerinin kontrolü
        SharedPreferences sharedPreferences2 = getSharedPreferences("LastSettingSiklik", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        LastClickSpinnerSiklik = sharedPreferences2.getInt("LastClickSpinnerSiklik",-1);

        SharedPreferences sharedPreferences3 = getSharedPreferences("LastSettingSes", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor3 = sharedPreferences3.edit();
        LastClickSpinnerSes = sharedPreferences3.getInt("LastClickSpinnerSes",-1);
//ayarlar ekranındaki sıklık ve ses spinnerlerinin kontrolü bitiş

        if(LastClickSpinnerSes == -1 || LastClickSpinnerSiklik == -1){ //uygulama ilk açıldığında ayarlardan gelecek sıklık ve spinnerleri def valuelardan kurtarmak için ilk olarak ayarlar activitye yönlendircek
            Toast.makeText(getApplicationContext(), "ONCELIKLE AYARLARI YAPMALISINIZ", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        else{
            final String sesKonum = Integer.toString(LastClickSpinnerSes); //ayarlar ekranındaki ses spinneri konumu alınıyor.

            //ana ekrandaki günlük haftalık aylık sıralama için spinenr ayarları
            spinnerSiklik = findViewById(R.id.siklik);
            final List<String> myList = new ArrayList<>();
            myList.add("Tumu");
            myList.add("Gunluk");
            myList.add("Haftalik");
            myList.add("Aylik");
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
            //ana ekrandaki günlük haftalık aylık sıralama için spinenr ayarları bitiş

            final Map<String, String> map = new HashMap<String, String>(); //{"2020-5-26"=" 12:36 AmacHocaSunum", "2020-5-25"=" 15:56 SuAl", "2020-5-28"=" 11:36 MarketeGit", ...}
            final Map<String, String> mapTamamlandi = new HashMap<String, String>(); //{"2020-5-26 12:36"="Tamamlandi", "2020-5-25 15:56"="Tamamlanmadi", "2020-5-28 11:36"="Tamamlanmadi", ...}
            final Map<String, String> mapRenkler = new HashMap<String, String>(); //{"2020-5-26 12:36"="Magenta", "2020-5-25 15:56"="Mavi", "2020-5-28 11:36"="Mavi", ...}
            final Map<String, String> mapBasliklar = new HashMap<String, String>(); //{"2020-5-26 12:36"="AmacHocaSunum", "2020-5-25 15:56"="SuAl", "2020-5-28 11:36"="MarketeGit", ...}
            final Map<String, String> mapIDler = new HashMap<String, String>(); //{"2020-5-26 12:36"="1590442938", "2020-5-25 15:56"="1590442937", "2020-5-28 11:36"="1590442936", ...}
            databaseReference = FirebaseDatabase.getInstance().getReference("Gorevler");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    liststring.clear();
                    aList.clear();
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String p1 = dataSnapshot1.getKey();
                        for(DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                            String p2 = dataSnapshot2.getKey();
                            HashMap<String, String> hm = new HashMap<String, String>();
                            say = 0;
                            for(DataSnapshot dataSnapshot3: dataSnapshot2.getChildren()){
                                String p3 = dataSnapshot3.getKey();
                                String v = (String) dataSnapshot3.getValue();
                                hm.put("listview_description", p1 + " " + p2); //{"listview_description":"2020-5-26 12:36"}
                                if (p3.equals("baslik")){
                                    String t = v;
                                    hm.put("listview_title", t);
                                    liststring.add(p1 + " " + p2 + " " + t); //["2020-5-26 12:36 AmacHocaSunum", "2020-5-25 15:56 SuAl", "2020-5-28 11:36 MarketeGit",...]
                                    listAllDays.add(p1); //["2020-5-26", "2020-5-25", "2020-5-28",...]
                                    map.put(p1, " " + p2 + " " + t);
                                    mapBasliklar.put(p1 + " " + p2, t);
                                }
                                else if (p3.equals("id")){
                                    String t = v;
                                    mapIDler.put(p1 + " " + p2, t);
                                    //Toast.makeText(getApplicationContext(), "Tarih :"+p1 + " " + p2+"VERENK : " +t , Toast.LENGTH_LONG).show();
                                }
                                else if (p3.equals("renk")){
                                    String t = v;
                                    mapRenkler.put(p1 + " " + p2, t);
                                    //Toast.makeText(getApplicationContext(), "Tarih :"+p1 + " " + p2+"VERENK : " +t , Toast.LENGTH_LONG).show();
                                }
                                else if (p3.equals("tamamlandi")){
                                    String t = v;
                                    mapTamamlandi.put(p1 + " " + p2, t);
                                    if(t.equals("Tamamlandi")){
                                        hm.put("listview_okay", Integer.toString(listviewImage[1]));
                                    }
                                    else if(t.equals("Tamamlanmadi")){
                                        hm.put("listview_okay", Integer.toString(listviewImage[0]));
                                    }
                                }
                                say++;
                                if(say == 8){ //her bir gorev kaydında 8 key var. 8.keye geldiğimizde sorgu bitiyor, yeni görevin sorgusuna geçiyoruz
                                    aList.add(hm); //"2020-5-26 12:36":{"listview_description":"2020-5-26 12:36"}
                                }
                            }
                        }
                    }

                    int tamamlandiMiSayac = 0;
                    int tamamlananlar = 0;
                    for (Map.Entry<String, String> entry : mapTamamlandi.entrySet()) { //tamamlananlar/hepsi oranı
                        //String k = entry.getKey();
                        String v = entry.getValue();
                        if (v.equals("Tamamlandi")){
                            tamamlananlar++;
                        }
                        tamamlandiMiSayac++;
                    }
                    String str1 = Integer.toString(tamamlananlar);
                    String str2 = Integer.toString(tamamlandiMiSayac);
                    tvTamamlandiSayac.setText("Tamamlama miktarı: " + str1 + "/" + str2);
                    Collections.reverse(aList); //firebase bize eskiden yeniye doğru bir çıktı veriyor. tam tersine çevirmek görsel açıdan daha güzel.

//custom listview için gerekli ayarlamalar
                    simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.my_listview, from, to){
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { //listviewdeki her bir item'in TEXT renginin darkmodeye göre ayarı
                            View view = super.getView(position, convertView, parent);

                            TextView text1 = (TextView) view.findViewById(R.id.listview_item_title);
                            TextView text2 = (TextView) view.findViewById(R.id.listview_item_short_description);
                            if (isDarkModeOn) {
                                text1.setTextColor(Color.WHITE);
                                text2.setTextColor(Color.WHITE);
                            }
                            else if (isDarkModeOn == false) {
                                text1.setTextColor(Color.BLACK);
                                text2.setTextColor(Color.BLACK);
                            }
                            String mydate = text2.getText().toString(); //listviewdeki her bir item'in BACKGROUND renginin belirlenmiş renge göre ayarı
                            String renk = mapRenkler.get(mydate);
                            //Toast.makeText(getApplicationContext(), "Tarih:"+mydate+"VERENK:" +renk , Toast.LENGTH_LONG).show();
                            if(renk.equals("Magenta")){
                                view.setBackgroundColor(Color.MAGENTA);
                            }
                            else if(renk.equals("Mavi")){
                                view.setBackgroundColor(Color.BLUE);
                            }

                            return view;
                        }
                    };
                    listView.setAdapter(simpleAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //herhangi bir item tıklanırsa
                            //String  itemValue    = (String) listView.getItemAtPosition(position);
                            String baslik = ((TextView)view.findViewById(R.id.listview_item_title)).getText().toString();
                            String tarih = ((TextView)view.findViewById(R.id.listview_item_short_description)).getText().toString();

                            // Show Alert
                            //Toast.makeText(getApplicationContext(), "Position :"+position+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), "Position :"+position + "Baslik: " + selected, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(MainActivity.this, GuncellemeSilmeActivity.class);
                            i.putExtra("EXTRA_MESSAGE",tarih + " " +baslik);
                            startActivity(i);
                        }
                    });
//custom listview için gerekli ayarlamalar bitiş

                    Calendar[] calendars = new Calendar[liststring.size()];
                    Calendar current = Calendar.getInstance();
                    Intent[] intents = new Intent[liststring.size()];
                    PendingIntent[] pendingIntents = new PendingIntent[liststring.size()];
                    AlarmManager[] alarmManagers = new AlarmManager[liststring.size()];
                    for(int i=0;i<liststring.size();i++){
                        String[] splited = liststring.get(i).split("\\s+"); //"2020-5-26 12:36 AmacHocaSunum" ifadesi " " ya göre ayrılacak
                        String totaldate = splited[0]; //"2020-5-26"
                        String totalhour = splited[1]; //"12:36"
                        String[] splited_totaldate = totaldate.split("-"); //"2020-5-26" ifadesi "-" ya göre ayrılacak
                        String[] splited_totalhour = totalhour.split(":"); //"12:36" ifadesi ":" ya göre ayrılacak
                        String myyear = splited_totaldate[0]; //"2020"
                        String mymonth = splited_totaldate[1]; //"5"
                        String myday = splited_totaldate[2]; //"26"
                        String myhour = splited_totalhour[0]; //12
                        String mymin = splited_totalhour[1]; //36
                        calendars[i] = Calendar.getInstance();
                        calendars[i].set(Integer.parseInt(myyear), //calendar ayarı
                                (Integer.parseInt(mymonth))-1,
                                Integer.parseInt(myday),
                                Integer.parseInt(myhour),
                                Integer.parseInt(mymin),
                                00);

                        //if((calendars[i].compareTo(current) <= 0) || mapTamamlandi.get(totaldate + " " + totalhour).equals("Tamamlandi")){
                        if(mapTamamlandi.get(totaldate + " " + totalhour).equals("Tamamlandi")){ //eğerki item "Tamamlandi" ise alarm kurmuyor
                            //The set Date/Time already passed
                            //Toast.makeText(getApplicationContext(), "Invalid Date/Time", Toast.LENGTH_LONG).show();
                        }else{
                            String str_id = mapIDler.get(totaldate + " " + totalhour); //her bir alarmın IDsini çekiyorum, pendingIntent'te unique request code için lazım olacak.
                            int int_id = Integer.parseInt(str_id);
                            //alarm kurma işlemleri
                            intents[i] = new Intent(getApplicationContext(), AlarmReceiver.class);
                            intents[i].putExtra("SES_ID", sesKonum);
                            intents[i].putExtra("BASLIK_ID", mapBasliklar.get(totaldate + " " + totalhour));
                            intents[i].putExtra("TARIH_SAAT_ID", totaldate + " " + totalhour);
                            pendingIntents[i] = PendingIntent.getBroadcast(getApplicationContext(), int_id, intents[i], 0);
                            alarmManagers[i] = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                            assert alarmManagers[i] != null;
                            //alarmManagers[i].set(AlarmManager.RTC_WAKEUP, calendars[i].getTimeInMillis(), pendingIntents[i]); //sadece bu şekilde kurarsak tekrarlama sıklığı aktif olmuyor.
                            if (LastClickSpinnerSiklik == 0){
                                editor3.putInt("LastClickSpinnerSes",Integer.parseInt(sesKonum)).apply();
                                alarmManagers[i].setRepeating(AlarmManager.RTC_WAKEUP, calendars[i].getTimeInMillis(), 1000 * 60 * 1, pendingIntents[i]); //tekrarlama sıklığıyla beraber alarmı kuruyoruz
                                //Toast.makeText(getApplicationContext(), "DK:1 ve ses:" + sesKonum, Toast.LENGTH_LONG).show();
                            }
                            else if (LastClickSpinnerSiklik == 1){
                                editor3.putInt("LastClickSpinnerSes",Integer.parseInt(sesKonum)).apply();
                                alarmManagers[i].setRepeating(AlarmManager.RTC_WAKEUP, calendars[i].getTimeInMillis(), 1000 * 60 * 5, pendingIntents[i]);
                                //Toast.makeText(getApplicationContext(), "DK:5 ve ses:" + sesKonum, Toast.LENGTH_LONG).show();
                            }
                            //alarm kurma işlemleri bitti
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //gunluk haftalık aylık sıralama için ayarlamalar
            final int[] gun = new int[1];
            spinnerSiklik.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    listTempDays.clear();
                    listWillShown.clear();
                    aList2.clear();
                    if(position == 0){ //"Tumu" seçiliyse
                        simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.my_listview, from, to){
                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { //listviewdeki her bir item'in TEXT renginin darkmodeye göre ayarı
                                View view = super.getView(position, convertView, parent);

                                TextView text1 = (TextView) view.findViewById(R.id.listview_item_title);
                                TextView text2 = (TextView) view.findViewById(R.id.listview_item_short_description);
                                if (isDarkModeOn) {
                                    text1.setTextColor(Color.WHITE);
                                    text2.setTextColor(Color.WHITE);
                                }
                                else if (isDarkModeOn == false) {
                                    text1.setTextColor(Color.BLACK);
                                    text2.setTextColor(Color.BLACK);
                                }
                                String mydate = text2.getText().toString(); //listviewdeki her bir item'in BACKGROUND renginin belirlenmiş renge göre ayarı
                                String renk = mapRenkler.get(mydate);
                                //Toast.makeText(getApplicationContext(), "Tarih:"+mydate+"VERENK:" +renk , Toast.LENGTH_LONG).show();
                                if(renk.equals("Magenta")){
                                    view.setBackgroundColor(Color.MAGENTA);
                                }
                                else if(renk.equals("Mavi")){
                                    view.setBackgroundColor(Color.BLUE);
                                }

                                return view;
                            }
                        };
                        listView.setAdapter(simpleAdapter);
                    }
                    else{
                        if(position == 1){ //"Gunluk" seciliyse
                            gun[0] = 0;
                        }
                        else if(position == 2){//"Haftalik" seçiliyse
                            gun[0] = 7;
                        }
                        else if(position == 3){//"Aylık" seçiliyse
                            gun[0] = 30;
                        }
                        for(int i = 0; i< gun[0]+1; i++){ //Mesela "Haftalık" seçildi. Bulunduğumuz gün dahil sonraki 7 günün listesini elde ediyorum
                            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, i);
                            Date todate1 = cal.getTime();
                            String fromdate = dateFormat.format(todate1);
                            //Toast.makeText(getApplicationContext(), fromdate, Toast.LENGTH_LONG).show();
                            String[] splited_tempday = fromdate.split("-");
                            char first = splited_tempday[1].charAt(0);
                            if(first == '0'){ //günlerin numaralarının baş sayısı bazen 0 oluyor (..,07,08,09) onu engellemek adına
                                splited_tempday[1] = String.valueOf(splited_tempday[1].charAt(1));
                            }
                            fromdate =splited_tempday[0] + "-" + splited_tempday[1] + "-" + splited_tempday[2];
                            listTempDays.add(fromdate); //geçici günler listesine ekliyorum
                            //Toast.makeText(getApplicationContext(), fromdate, Toast.LENGTH_LONG).show();
                        }
                        //listTempDays.retainAll(listAllDays);
                        for (Map.Entry<String, String> entry : mapBasliklar.entrySet()) { //elde ettiğim günler listesiyle elimdeki listenin kesişimlerini alıyorum
                            String k = entry.getKey();
                            String v = entry.getValue();
                            String[] splited_k = k.split("\\s+");
                            if(listTempDays.contains(splited_k[0])){
                                listWillShown.add(k + " " + v); //kesişimin sonucunda gösterilecek günler listesi oluşturuyorum
                            }
                        }
                        for(int i=0; i< listWillShown.size();i++){
                            //Toast.makeText(getApplicationContext(), listTempDays.get(i), Toast.LENGTH_LONG).show();
                            HashMap<String, String> hm2 = new HashMap<String, String>(); //kesişimler sonucunda yeni bir hashmap oluşturuyorum. içerisi yukarıdaki aList ile aynı mantıkta dolacak.
                            String arda = listWillShown.get(i);
                            String[] splited_arda = arda.split("\\s+");
                            //listWillShown.add(listTempDays.get(i) + map.get(listTempDays.get(i)));
                            if(mapTamamlandi.get(splited_arda[0] + " " + splited_arda[1]).equals("Tamamlandi")){
                                hm2.put("listview_okay", Integer.toString(listviewImage[1]));
                            }
                            else if(mapTamamlandi.get(splited_arda[0] + " " + splited_arda[1]).equals("Tamamlanmadi")){
                                hm2.put("listview_okay", Integer.toString(listviewImage[0]));
                            }
                            //Toast.makeText(getApplicationContext(), mapTamamlandi.get(listTempDays.get(i) + " " + splited_mytemp[1]), Toast.LENGTH_LONG).show();
                            //hm2.put("listview_okay", Integer.toString(listviewImage[1]));
                            hm2.put("listview_title", splited_arda[2]);
                            hm2.put("listview_description", splited_arda[0] + " " + splited_arda[1]);
                            aList2.add(hm2);
                        }
                        Collections.reverse(aList2);
                        simpleAdapter = new SimpleAdapter(getBaseContext(), aList2, R.layout.my_listview, from, to){
                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { //listviewdeki her bir item'in TEXT renginin darkmodeye göre ayarı
                                View view = super.getView(position, convertView, parent);

                                TextView text1 = (TextView) view.findViewById(R.id.listview_item_title);
                                TextView text2 = (TextView) view.findViewById(R.id.listview_item_short_description);
                                if (isDarkModeOn) {
                                    text1.setTextColor(Color.WHITE);
                                    text2.setTextColor(Color.WHITE);
                                }
                                else if (isDarkModeOn == false) {
                                    text1.setTextColor(Color.BLACK);
                                    text2.setTextColor(Color.BLACK);
                                }
                                String mydate = text2.getText().toString(); //listviewdeki her bir item'in BACKGROUND renginin belirlenmiş renge göre ayarı
                                String renk = mapRenkler.get(mydate);
                                //Toast.makeText(getApplicationContext(), "Tarih:"+mydate+"VERENK:" +renk , Toast.LENGTH_LONG).show();
                                if(renk.equals("Magenta")){
                                    view.setBackgroundColor(Color.MAGENTA);
                                }
                                else if(renk.equals("Mavi")){
                                    view.setBackgroundColor(Color.BLUE);
                                }

                                return view;
                            }
                        };
                        listView.setAdapter(simpleAdapter);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    @Override
    public void onBackPressed() { //telefondaki geri tuşuna basılırsa
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
