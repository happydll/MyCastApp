package com.example.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.example.myapplication.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private ImageView imageView;
    private Handler handler;
    private String serverUrl = "http://192.168.1.106:5000"; //server ın urlsi

    private Runnable updateImageRunnable = new Runnable() {
        @Override
        public void run() {
            updateImage();
            handler.postDelayed(this, 1000); // Her saniyede bir güncelle
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        handler = new Handler();

        // İlk kez resmi yükle
        updateImage();
        // Daha sonra saniyede bir güncelle
        handler.postDelayed(updateImageRunnable, 1000);
    }

    private void updateImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Ekran görüntüsünü indir
                    URL url = new URL(serverUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();

                    // Check for a successful response code
                    if (connection.getResponseCode() != 200) {
                        Log.e("MainActivity", "Error: " + connection.getResponseCode());
                        return; // Bir hata meydana gelirse durdur
                    }

                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);

                    // Ekranda göster
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    Log.e("MainActivity", "Error loading image: " + e.getMessage());
                }
            }
        }).start();
    }
}