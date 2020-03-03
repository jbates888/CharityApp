package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminEventDetails extends AppCompatActivity {

    FirebaseDatabase mFirebasedatabase;
    DatabaseReference mRefrence;

    TextView nameTxt;
    TextView progTxt;
    TextView descTxt;
    TextView dateTxt;
    TextView timeTxt;
    TextView fundsTxt;
    TextView volsTxt;
    TextView volsNeededTxt;

    Button deleteBtn;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_details);

        ref = FirebaseDatabase.getInstance().getReference("Events");

        nameTxt = findViewById(R.id.event_details_title);
        progTxt = findViewById(R.id.event_details_prog);
        descTxt = findViewById(R.id.event_details_desc);
        dateTxt = findViewById(R.id.event_details_date);
        timeTxt = findViewById(R.id.event_details_time);
        fundsTxt = findViewById(R.id.event_details_funds);
        volsTxt = findViewById(R.id.event_details_vols);
        volsNeededTxt = findViewById(R.id.event_details_volsNeeded);

        deleteBtn = findViewById(R.id.delete_btn);

        Bundle extras = getIntent().getExtras();
        nameTxt.setText( extras.getString("Name"));
        progTxt.setText("Program: "  + extras.getString("Program"));
        descTxt.setText("Description: "  +  extras.getString("Description"));
        dateTxt.setText("Date: "  +  extras.getString("Date"));
        timeTxt.setText("Time: "  +  extras.getString("Time"));
        fundsTxt.setText("Funding: $"  +  extras.getInt("Funds", 0));
        volsTxt.setText("Volunteers: "  +  extras.getString("Volunteers"));
        volsNeededTxt.setText("Volunteers Needed: "  +  extras.getInt("VolunteersNeeded", 0));


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventname = extras.getString("Name");
                Query eventquery = ref.orderByChild("name").equalTo(eventname);
                eventquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot eventshot: dataSnapshot.getChildren()){
                            // Toast.makeText(getApplicationContext(), eventshot.getKey(), Toast.LENGTH_LONG).show();
                            eventshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                startActivity(new Intent(AdminEventDetails.this, MainActivity.class));
                finish();
            }
        });
    }
}
