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
    DatabaseReference ref;
    FirebaseAuth mAuth;
    TextView donateAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_event_details);

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
        mAuth = FirebaseAuth.getInstance();

        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = Integer.parseInt(donateAmount.getText().toString());

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

                //check if the max amount of volunteers has been reached
                if(extras.getInt("VolunteersNeeded") <= 0){
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
                else if(extras.getString("Volunteers").contains(temp)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DonorEventDetails.this);
                    builder.setCancelable(true);
                    builder.setTitle("Already Signed up");
                    builder.setMessage("You are already signed up for this event");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    //if they are not signed up, ask if they would like to
                }else{
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
                                        ref.child(eventshot.getKey()).child("volunteers").setValue(extras.getString("Volunteers") + temp + ", ");
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
}
