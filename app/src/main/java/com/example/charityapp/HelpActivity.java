package com.example.charityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//this class is where user who click the help button in the menu are sent to
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }
}
