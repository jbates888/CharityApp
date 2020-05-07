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

/**
 * @description activity where admins can view the data for the whole charity
 *
 * @authors Jack Bates
 * @date_created 03/16/20
 * @date_modified 05/03/20
 */
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
        //set the database references
        database = FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Data");
        //set elements to their id
        total = findViewById(R.id.totTxt);
        curTotal = findViewById(R.id.curTxt);
        nEvents = findViewById(R.id.eventTxt);
        nTotEvents = findViewById(R.id.totNEventsTxt);

        //listens for change to data section of database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //set the text views to the current values in the database
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
