package com.example.charityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import static java.lang.Thread.sleep;

/**
 * @description class for the splash screen when the app opens
 *
 * @authors Travis Agarano
 * @date_created
 * @date_modified
 */
public class splash_screen extends AppCompatActivity {
    
    /**
     * Shows the splash screen when app is opened for 3 seconds,
     * then it will go to the sign in page, and end the splash screen
     * so users cannot go back to it
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread myThread = new Thread(() -> {
            try {
                //show for 3 seconds
                sleep(3000);
                //start a new intent to the main activity
                Intent i = new Intent(splash_screen.this, MainActivity.class);
                startActivity(i);
                //finish the activity
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //start
        myThread.start();
    }
}
