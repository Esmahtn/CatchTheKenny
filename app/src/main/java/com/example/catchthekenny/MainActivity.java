package com.example.catchthekenny;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView scoreText;
    TextView timeText;
    ImageView imageView0;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;

    ImageView[] imageArray;
    Handler handler;
    Runnable runnable;

    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initialize
        timeText = (TextView) findViewById(R.id.timeText);
        scoreText = (TextView) findViewById(R.id.scoreText);

        imageView0 = (ImageView) findViewById(R.id.imageView0);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        imageView7 = (ImageView) findViewById(R.id.imageView7);
        imageView8 = (ImageView) findViewById(R.id.imageView8);

        imageArray = new ImageView[]{
                imageView0, imageView1, imageView2, imageView3, imageView4,
                imageView5, imageView6, imageView7, imageView8
        };

        score = 0; // Skoru onCreate içinde initialize etmek daha doğru

        hideImages(); // Kenny gösterme işlemini başlat

        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeText.setText("time: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timeText.setText("Time OFF!");

                if (handler != null && runnable != null) {
                    handler.removeCallbacks(runnable);
                }

                for (ImageView image : imageArray) {
                    if (image != null) { // Güvenlik için null kontrolü
                        image.setVisibility(View.INVISIBLE);
                    }
                }

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle("RESTART?");
                alertBuilder.setMessage("Are you sure to restart game?"); // Yazım hatası düzeltildi
                // alertBuilder.setCancelable(false); // Bu satırı isteğe bağlı olarak ekleyebilirsin

                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // Senin kodundaki gibi küçük harf 'yes'
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // RESTART MANTIĞI (Senin orijinal kodundaki gibi)
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });

                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() { // Senin kodundaki gibi küçük harf 'no'
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Game Over!", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = alertBuilder.create();
                dialog.show(); // Diyalogu göster
            }
        }.start();
    } // onCreate Metodu Bitişi

    // Bu metod MainActivity sınıfının bir üyesi olmalı, onCreate içinde değil.
    public void incraeseScore(View view) {
        score++;
        scoreText.setText("Score: " + score);
    }

    // Bu metod MainActivity sınıfının bir üyesi olmalı, onCreate içinde değil.
    public void hideImages() {
        // Handler ve Runnable her çağrıldığında yeniden oluşturuluyor.
        // Eğer oyun sırasında birden fazla kez hideImages çağrılmayacaksa bu sorun değil.
        // onCreate içinde sadece bir kez çağrıldığı için mevcut durumda sorun yaratmaz.
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ImageView image : imageArray) {
                    if (image != null) { // Güvenlik için null kontrolü
                        image.setVisibility(View.INVISIBLE);
                    }
                }

                Random random = new Random();
                int i = random.nextInt(9); // 0-8 arası rastgele sayı
                if (imageArray[i] != null) { // Güvenlik için null kontrolü
                    imageArray[i].setVisibility(View.VISIBLE);
                }

                if (handler != null) { // Handler null değilse post et
                    handler.postDelayed(this, 500); // Yarım saniyede bir tekrarla
                }
            }
        };
        if (handler != null) { // Handler null değilse başlat
            handler.post(runnable); // Runnable'ı hemen başlat
        }
    }

    // Activity yok edildiğinde kaynakları serbest bırakmak iyi bir pratiktir.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable); // Handler'daki bekleyen görevleri iptal et
        }
        // Eğer CountDownTimer'ı sınıf seviyesinde bir değişkene atarsan,
        // burada onu da .cancel() ile durdurabilirsin.
        // Örneğin:
        // if (myCountDownTimer != null) {
        //     myCountDownTimer.cancel();
        // }
    }
}
