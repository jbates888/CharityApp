package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @description where admin users are sent after logging in to view all the events
 *
 * @authors Jack Bates
 * @date_created 3/1/20
 * @date_modified 3/28/20
 */
public class AdminEventDetails extends AppCompatActivity {

    DatabaseReference dataRefrence;
    TextView nameTxt;
    TextView progTxt;
    TextView descTxt;
    TextView dateTxt;
    TextView timeTxt;
    TextView fundsTxt;
    TextView volsTxt;
    TextView volsNeededTxt;
    Button deleteBtn;
    DatabaseReference ref, vol, don;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_details);

        //set up reference to the events
        ref = FirebaseDatabase.getInstance().getReference("Events");
        //set up reference to the data
        dataRefrence = FirebaseDatabase.getInstance().getReference("Data");

        //set all variable to their objects ID
        nameTxt = findViewById(R.id.event_details_title);
        progTxt = findViewById(R.id.event_details_prog);
        descTxt = findViewById(R.id.event_details_desc);
        dateTxt = findViewById(R.id.event_details_date);
        timeTxt = findViewById(R.id.event_details_time);
        fundsTxt = findViewById(R.id.event_details_funds);
        volsTxt = findViewById(R.id.event_details_vols);
        volsNeededTxt = findViewById(R.id.event_details_volsNeeded);
        deleteBtn = findViewById(R.id.delete_btn);

        //bring in the extras passed into the activity
        Bundle extras = getIntent().getExtras();
        //set all the text field values to the extras passed in
        nameTxt.setText(extras.getString("Name"));
        progTxt.setText("Program: " + extras.getString("Program"));
        descTxt.setText("Description: " + extras.getString("Description"));
        dateTxt.setText("Date: " + extras.getString("Date"));
        timeTxt.setText("Time: " + extras.getString("Time"));
        fundsTxt.setText("Funding: $" + extras.getInt("Funds", 0));
        volsTxt.setText("Volunteers: " + extras.getString("Volunteers"));
        volsNeededTxt.setText("Volunteers Needed: " + extras.getInt("VolunteersNeeded", 0));

        //delete button on click
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the events name
                String eventname = extras.getString("Name");
                String[] names = extras.getString("Volunteers").replaceAll(" ", "").split(", ");
                List<String> people = Arrays.asList(names);


                Query eventquery = ref.orderByChild("name").equalTo(eventname);
                eventquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot eventshot : dataSnapshot.getChildren()) {
                            //remove the event
                            eventshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dataRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //set all the data values after the delete
                        int value = dataSnapshot.child("numEvents").getValue(Integer.class);
                        dataRefrence.child("numEvents").setValue(value - 1);
                        int value2 = dataSnapshot.child("curTotal").getValue(Integer.class);
                        dataRefrence.child("curTotal").setValue(value2 - (extras.getInt("Funds", 0)));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Code
                    }
                });
                //send user back to main activity
                startActivity(new Intent(AdminEventDetails.this, MainActivity.class));
                //finish the activity
                finish();
            }
        });
    }


}
