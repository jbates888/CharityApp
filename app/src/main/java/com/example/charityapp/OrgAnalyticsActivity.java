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
    TextView nAdmin;
    TextView nDon;
    TextView nVol;
    TextView nEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_analytics);

        database = FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Data");

        total = findViewById(R.id.totTxt);
        curTotal = findViewById(R.id.curTxt);
        nAdmin = findViewById(R.id.adTxt);
        nDon = findViewById(R.id.donTxt);
        nVol = findViewById(R.id.volTxt);
        nEvents = findViewById(R.id.eventTxt);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total.setText(dataSnapshot.child("Total").getValue().toString());
                curTotal.setText(dataSnapshot.child("curTotal").getValue().toString());
                nAdmin.setText(dataSnapshot.child("numAdmin").getValue().toString());
                nDon.setText(dataSnapshot.child("numDonor").getValue().toString());
                nVol.setText(dataSnapshot.child("numVols").getValue().toString());
                nEvents.setText(dataSnapshot.child("numEvents").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
