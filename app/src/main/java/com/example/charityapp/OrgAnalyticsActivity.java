package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class OrgAnalyticsActivity extends AppCompatActivity {

    DatabaseReference ref;
    FirebaseDatabase database;

    TextView total;
    TextView curTotal;
    TextView nEvents;
    TextView nTotEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_analytics);

        database = FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Data");

        total = findViewById(R.id.totTxt);
        curTotal = findViewById(R.id.curTxt);
        nEvents = findViewById(R.id.eventTxt);
        nTotEvents = findViewById(R.id.totNEventsTxt);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total.setText(dataSnapshot.child("Total").getValue().toString());
                curTotal.setText(dataSnapshot.child("curTotal").getValue().toString());
                nEvents.setText(dataSnapshot.child("numEvents").getValue().toString());
                nTotEvents.setText(dataSnapshot.child("totNumEvents").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
