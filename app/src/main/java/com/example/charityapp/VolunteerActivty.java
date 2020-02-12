package com.example.charityapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class VolunteerActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_activty);

        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
    }
}
