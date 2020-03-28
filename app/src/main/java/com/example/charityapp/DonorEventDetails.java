package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class DonorEventDetails extends AppCompatActivity {

    TextView nameTxt;
    TextView progTxt;
    TextView descTxt;
    TextView dateTxt;
    TextView timeTxt;
    TextView fundsTxt;
    TextView volsTxt;
    TextView volsNeededTxt;
    Button volunteerBtn;
    Button donateBtn;
    DatabaseReference ref, dataReference;
    FirebaseAuth mAuth;
    TextView donateAmount;
    int amount;

    boolean bool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_event_details);

        bool = false;

        nameTxt = findViewById(R.id.event_details_title);
        progTxt = findViewById(R.id.event_details_prog);
        descTxt = findViewById(R.id.event_details_desc);
        dateTxt = findViewById(R.id.event_details_date);
        timeTxt = findViewById(R.id.event_details_time);
        fundsTxt = findViewById(R.id.event_details_funds);
        volsTxt = findViewById(R.id.event_details_vols);
        volsNeededTxt = findViewById(R.id.event_details_volsNeeded);
        volunteerBtn = findViewById(R.id.volunteer_btn);
        donateBtn = findViewById(R.id.donate_btn);
        donateAmount = findViewById(R.id.donateEditTxt);


        Bundle extras = getIntent().getExtras();
        nameTxt.setText( extras.getString("Name"));
        progTxt.setText("Program: "  + extras.getString("Program"));
        descTxt.setText("Description: "  +  extras.getString("Description"));
        dateTxt.setText("Date: "  +  extras.getString("Date"));
        timeTxt.setText("Time: "  +  extras.getString("Time"));
        fundsTxt.setText("Funding: $"  +  extras.getInt("Funds"));
        volsTxt.setText("Volunteers: "  +  extras.getString("Volunteers"));
        volsNeededTxt.setText("Volunteers Needed: "  +  extras.getInt("VolunteersNeeded", 0));

        ref = FirebaseDatabase.getInstance().getReference("Events");
        dataReference = FirebaseDatabase.getInstance().getReference("Data");
        mAuth = FirebaseAuth.getInstance();

        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValid(donateAmount.getText().toString())){
                    amount = Integer.parseInt(donateAmount.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Enter a valid amount", Toast.LENGTH_LONG).show();
                    return;
                }
                String eventname = extras.getString("Name");
                Query eventquery = ref.orderByChild("name").equalTo(eventname);
                eventquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot eventshot: dataSnapshot.getChildren()){
                            ref.child(eventshot.getKey()).child("funding").setValue(extras.getInt("Funds") + amount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int value = dataSnapshot.child("Total").getValue(Integer.class);
                        dataReference.child("Total").setValue(value + amount);
                        int value2 = dataSnapshot.child("curTotal").getValue(Integer.class);
                        dataReference.child("curTotal").setValue(value2 + amount);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Code
                    }
                });

                finish();
                startActivity(new Intent(DonorEventDetails.this, DonorActivity.class));
                Toast.makeText(getApplicationContext(), "Thank you for donating!", Toast.LENGTH_LONG).show();
            }
        });

        volunteerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventname = extras.getString("Name");
                Query eventquery = ref.orderByChild("name").equalTo(eventname);

                FirebaseUser user = mAuth.getCurrentUser();
                String temp = user.getDisplayName().replaceAll("Donor:", "");
//                ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Event event = snapshot.getValue(Event.class);
//
//                            if(event.getVolunteers().contains(temp)){
//                                if(event.getDate().equals(extras.getString("Date"))){
//                                    Toast.makeText(getApplicationContext(), "Overlap!!!", Toast.LENGTH_LONG).show();
//                                    setBool();
//                                }
//                            }
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // Code
//                    }
//                });

                //check if the max amount of volunteers has been reached
                if (extras.getInt("VolunteersNeeded") <= 0 && !extras.getString("Volunteers").contains(temp)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DonorEventDetails.this);
                    builder.setCancelable(true);
                    builder.setTitle("Volunteer Limit Reached");
                    builder.setMessage("The maximum amount of volunteers needed for the event has been reached!");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                //check if that users name is already signed up
                else if (extras.getString("Volunteers").contains(temp)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DonorEventDetails.this);
                    builder.setCancelable(true);
                    builder.setTitle("Un-Volunteer");
                    builder.setMessage("Would you like to un sign up for this event?");

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String eventname = extras.getString("Name");
                            Query eventquery = ref.orderByChild("name").equalTo(eventname);
                            eventquery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot eventshot: dataSnapshot.getChildren()){
                                        String original = eventshot.child("volunteers").getValue(String.class);
                                        String newVolList;
                                        if (extras.getString("Volunteers").length() <= temp.length() + 1) {
                                            newVolList = original.replace(temp, "");
                                        } else if(extras.getString("Volunteers").substring(0, temp.length()).equals(temp)) {
                                            newVolList = original.replace(temp + ",", "");
                                        } else{
                                            newVolList = original.replace("," + temp, "");
                                        }

                                        ref.child(eventshot.getKey()).child("volunteers").setValue(newVolList);
                                        //get the number of volunteers and add one
                                        int val = extras.getInt("numVolunteers") - 1;
                                        ref.child(eventshot.getKey()).child("volunteersNeeded").setValue(extras.getInt("VolunteersNeeded") + 1);
                                        ref.child(eventshot.getKey()).child("numVolunteers").setValue(val);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            finish();
                            startActivity(new Intent(DonorEventDetails.this, DonorActivity.class));
                            Toast.makeText(getApplicationContext(), "Removed from volunteer list", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.show();
                    //if they are not signed up, ask if they would like to
                }
//                else if(bool) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(DonorEventDetails.this);
//                    builder.setCancelable(true);
//                    builder.setTitle("Event Overlap");
//                    builder.setMessage("You are already signed up for and event at this time");
//
//                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    builder.show();
//           }
                 else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(DonorEventDetails.this);
                    builder.setCancelable(true);
                    builder.setTitle("Volunteer for event");
                    builder.setMessage("Would you like to sign up for this event?");

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String eventname = extras.getString("Name");
                            Query eventquery = ref.orderByChild("name").equalTo(eventname);
                            eventquery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot eventshot: dataSnapshot.getChildren()){
                                        //Toast.makeText(getApplicationContext(), eventshot.getKey(), Toast.LENGTH_LONG).show();
                                        if (extras.getString("Volunteers").length() == 0) {
                                            ref.child(eventshot.getKey()).child("volunteers").setValue(extras.getString("Volunteers") + temp);
                                        } else {
                                            ref.child(eventshot.getKey()).child("volunteers").setValue(extras.getString("Volunteers") + ","  + temp);

                                        }
                                        ref.child(eventshot.getKey()).child("volunteersNeeded").setValue(extras.getInt("VolunteersNeeded") - 1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            finish();
                            startActivity(new Intent(DonorEventDetails.this, DonorActivity.class));
                            Toast.makeText(getApplicationContext(), "Signed Up", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.show();

                }
            }
        });

    }

    private boolean isValid(String value){
        try{
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException e){
            return false;
        }

    }

//    private void setBool(){
//        bool = true;
//    }

}
