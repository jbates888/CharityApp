package com.example.charityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import static java.lang.Thread.sleep;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread myThread = new Thread(() -> {

            try {
                sleep(3000);

                Intent i = new Intent(splash_screen.this,MainActivity.class);
                startActivity(i);
                finish();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        myThread.start();
    }
}
