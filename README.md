# ytuMobilOdev2
Yıldız Teknik Üniversitesi Bilgi Teknolojileri Tezsiz Yüksek Lisans 2019-2020 Bahar Dönemi BLM5218 Mobil Teknolojiler ve Uygulamaları Ödev-2 by Arda Dizdaroğlu 19574016
- Ders Yürütücüsü: Dr. Öğr. Üyesi M. Amaç GÜVENSAN
- Proje : Hatırlatma Uygulaması Geliştirilmesi
- Konu : AlarmManager, Database, Internal Storage, Activity, Intent, Broadcast Receiver

İçerik :

1. Görev, Doğumgünü, Toplantı vb. Etkinliklerin Yönetimi: 
a. Listeleme,Ekleme,Silme,Güncelleme
b. Günlük,Haftalık,Aylık Gösterim

2. Hatırlatma Özelliği: 
a. Tarih ve Zaman Atama, İstenilen Zamanlarda Hatırlatma
b. Bildirim ve/veya AlertDialog yardımı ile bilgilendirme
c. Hatırlatma sırasında zil sesi, titreşim vb. kullanma

3. Ayarlar Ekranı
a. Varsayılan Zil Sesi
b. Varsayılan Hatırlatma Zamanı
c. Varsayılan Hatırlatma Sıklığı
d. Dark/Light Mod

4. Görev/Not/Doğumğünü vb. Paylaşımı
a. E mail ve Sosyal Medya Uygulamaları Üzerinden Paylaşım

5. Hatırlatma Kategorileri Belirleme
a. Renk, Kategori isimleri ve Etiket Kullanımı

6. Mesaj ile Yapılan İşlerin Kullanıcıya Gösterilmesi
a. “Ekleme Yapıldı”, “Silme İşlem Tamamlandı” vb. mesajlar i le kullanıcıların
bilgilendirimesi

7. Tamamlanan/Tamamlanamayan İşlerin İşaretlenmesi ve İstatistik Bilgi Olarak
Gösterilmesi
a. 10/20 Tamamlandı vb.

# Önemli Notlar
1. APK dosyasını da ekledim. Projeyi indirdiğinizde app-debug.apk dosyasını telefona yüklerseniz çalışacaktır.
2. Projeyi API 29'da çalıştırmanız gerekmektedir. API 26 yı aklıma gelen bütün yöntemlerle denememe rağmen bir yerde hata alıyorum, en basit Hello Worl projesi bile malesef çalışmıyor.

# Tespit Ettiğim Buglar (en kısa sürede çözeceğim :) )
1. Diyelimki şuan 26.05.2020 12:15. Ve siz aynı gün 1. alarmı 12:16'a ve 2.alarmı 12:19'a alarm kurdunuz. İlk alarm 12:16'da ötünce, ayarlar ekranında seçtiğiniz sıklığa göre 12:19'a kadar 1.alarm ötecek. 12:19'dan itibaren sadece 2.alarm ötüyor, bir şekilde eski alarm çalışmamaya başlıyor
2. Uygulamayı ilk açtığınızda karşınıza gelen ayarlar ekranında belirlediğiniz ayarlar SharedPreferences ile arkaplanda tutuluyor. Ancak uygulama içerisinde istediğiniz zaman ses ayarını değiştirirseniz ilk belirlediğiniz ses çalıyor. Darkmode ve sıklık ayarında sorun yok.
3. Günlük, haftalık gösterimde sorun yok ancak aylık gösterimde hatalar olabiliyor.
