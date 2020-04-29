package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

/**
 * @description
 *
 * @authors
 * @date_created
 * @date_modified
 */
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
    DatabaseReference vol;
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

        //bring in the extras passed into the activity
        Bundle extras = getIntent().getExtras();
        //set all the text field values to the extras passed in
        nameTxt.setText(extras.getString("Name"));
        progTxt.setText("Program: " + extras.getString("Program"));
        descTxt.setText("Description: " + extras.getString("Description"));
        dateTxt.setText("Date: " + extras.getString("Date"));
        timeTxt.setText("Time: " + extras.getString("Time"));
        fundsTxt.setText("Funding: $" + extras.getInt("Funds"));
        volsTxt.setText("Volunteers: " + extras.getString("Volunteers"));
        volsNeededTxt.setText("Volunteers Needed: " + extras.getInt("VolunteersNeeded", 0));
        //create an event with the passed in extras
        Event event = new Event(extras.getString("Name"), extras.getString("Program"), extras.getString("Date"), extras.getString("Time"), extras.getInt("Funds"), extras.getString("Description"), extras.getString("Volunteers"), extras.getInt("VolunteersNeeded"), extras.getInt("numVolunteers"));

        //set up reference to the events
        ref = FirebaseDatabase.getInstance().getReference("Events");
        //set up reference to the data
        dataReference = FirebaseDatabase.getInstance().getReference("Data");
        //make an auth object for the current user
        mAuth = FirebaseAuth.getInstance();

        //donate button on click
        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if its a valid donation amount set amount to that
                if (isValid(donateAmount.getText().toString())) {
                    amount = Integer.parseInt(donateAmount.getText().toString());
                } else {
                    //if not valid tell the user to enter a valid amount an return
                    Toast.makeText(getApplicationContext(), "Enter a valid amount", Toast.LENGTH_LONG).show();
                    return;
                }
                //get the event name
                String eventname = extras.getString("Name");
                Query eventquery = ref.orderByChild("name").equalTo(eventname);
                eventquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot eventshot : dataSnapshot.getChildren()) {
                            //set the funding amount in the database to the new value
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
                        //update the new data for the admin stats after a new donation
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
                FirebaseUser user = mAuth.getCurrentUser();
                vol = FirebaseDatabase.getInstance().getReference("DonorDetails").child(user.getDisplayName()).child("donated");
                vol.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int hours = dataSnapshot.getValue(Integer.class);
                        hours += amount;
                        vol.setValue(hours);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                finish();
                //send the user to the donor activity and tell them thanks for donating
                startActivity(new Intent(DonorEventDetails.this, DonorActivity.class));
                Toast.makeText(getApplicationContext(), "Thank you for donating!", Toast.LENGTH_LONG).show();
            }
        });
        //volunteer on click
        volunteerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the event name
                String eventname = extras.getString("Name");
                Query eventquery = ref.orderByChild("name").equalTo(eventname);
                //get the current user
                FirebaseUser user = mAuth.getCurrentUser();
                //get rid of the donor tag in username
                String temp = user.getDisplayName().replaceAll("Donor:", "");

                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("volunteeractivity", "Number of events" + Long.toString(dataSnapshot.getChildrenCount()));
                        //get children in database
                        Iterable<DataSnapshot> events = dataSnapshot.getChildren();
                        int[] two_events = {0};
                        int count = 0;
                        //iterate through each child
                        for (DataSnapshot s : events) {
                            //if the event name matched the key ingore it
                            if (event.getName().equals(s.getKey())) {
                                //do nothing
                            } else {
                                Log.d("volunteeractivity", "Event name" + s.getKey());
                                Log.d("volunteeractivity", "Date after test" + two_events[0]);
                                String month = s.child("date").getValue(String.class);
                                String[] compareevent = month.split("\\/");
                                String[] eventdetails = event.getDate().split("\\/");

                                if (eventdetails[2].equals(compareevent[2])) {
                                    if (eventdetails[0].equals(compareevent[0])) {
                                        if (eventdetails[1].equals(compareevent[1])) {
                                            String vols = s.child("volunteers").getValue(String.class);
                                            if (vols.contains(temp)) {
                                                String[] comparetimes = s.child("time").getValue(String.class).split("-");
                                                Log.d("times", comparetimes[0]);
                                                String[] times = event.getTime().split("-");
                                                Date start1 = null;
                                                Date start2 = null;
                                                Date end1 = null;
                                                Date end2 = null;
                                                for (int i = 0; i < comparetimes.length; i++) {
                                                    SimpleDateFormat mformat = new SimpleDateFormat("HH:mm");
                                                    SimpleDateFormat oldformat = new SimpleDateFormat("hh:mma");
                                                    Date date = null;
                                                    try {
                                                        date = oldformat.parse(comparetimes[i]);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    comparetimes[i] = mformat.format(date);
                                                    if (i == 0) start1 = date;
                                                    if (i == 1) end1 = date;
                                                }

                                                for (int i = 0; i < times.length; i++) {
                                                    SimpleDateFormat mformat = new SimpleDateFormat("HH:mm");
                                                    SimpleDateFormat oldformat = new SimpleDateFormat("hh:mma");
                                                    Date date = null;
                                                    try {
                                                        date = oldformat.parse(times[i]);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    times[i] = mformat.format(date);
                                                    if (i == 0) start2 = date;
                                                    if (i == 1) end2 = date;
                                                }
                                                if ((null == end2 || start1.before(end2)) && (null == end1 || start2.before(end1))) {
                                                    two_events[0] = 1;
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        count++;
                        displayMessages(two_events[0], count, event, temp, user);
                        two_events[0] = 0;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                };
                ref.addListenerForSingleValueEvent(eventListener);
            }
        });

    }

    //checks if the user entered a valid donation amount
    private boolean isValid(String value) {
        //if they entered a positive int return true
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            //if not return false
            return false;
        }
    }

    private void displayMessages(int equals, int count, Event event, String temp, FirebaseUser user) {
        if (count <= 1) {
            if (equals == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DonorEventDetails.this);
                builder.setCancelable(true);
                builder.setTitle("Volunteering for Multiple Events");
                builder.setMessage("You cannot volunteer for multiple events at the same time.");

                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                //System.out.println(two_events[0]);
            } else if (event.getVolunteersNeeded() <= 0 && !event.getVolunteers().contains(temp)) {
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
            } else if (event.getVolunteers().contains(temp)) {
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
                        String eventname = event.getName();
                        Query eventquery = ref.orderByChild("name").equalTo(eventname);
                        eventquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot eventshot : dataSnapshot.getChildren()) {
                                    String original = event.getVolunteers();
                                    String newVolList;
                                    if (event.getVolunteers().length() <= temp.length() + 1) {
                                        newVolList = original.replace(temp, "");
                                    } else if (event.getVolunteers().substring(0, temp.length()).equals(temp)) {
                                        newVolList = original.replace(temp + ",", "");
                                    } else {
                                        newVolList = original.replace("," + temp, "");
                                    }

                                    ref.child(eventshot.getKey()).child("volunteers").setValue(newVolList);
                                    //get the number of volunteers and add one
                                    int val = event.getNumVolunteers() - 1;

                                    ref.child(eventshot.getKey()).child("volunteersNeeded").setValue(event.getVolunteersNeeded() + 1);
                                    ref.child(eventshot.getKey()).child("numVolunteers").setValue(val);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getApplicationContext(), "Removed from volunteer list", Toast.LENGTH_LONG).show();

                        vol = FirebaseDatabase.getInstance().getReference("DonorDetails").child(user.getDisplayName()).child("hours");
                        vol.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int hours = dataSnapshot.getValue(Integer.class);
                                String[] comparetimes = event.getTime().split("-");
                                Log.d("times", comparetimes[0]);
                                String[] times = event.getTime().split("-");
                                Date start1 = null;
                                Date end1 = null;

                                for(int i = 0; i < comparetimes.length; i++){
                                    SimpleDateFormat mformat = new SimpleDateFormat("HH:mm");
                                    SimpleDateFormat oldformat = new SimpleDateFormat("hh:mma");
                                    Date date = null;
                                    try {
                                        date = oldformat.parse(comparetimes[i]);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    comparetimes[i] = mformat.format(date);
                                    if(i == 0) start1 = date;
                                    if(i == 1) end1 = date;
                                }

                                long start = start1.getTime();
                                long end = end1.getTime();
                                long result = end - start;
                                result = result / 3600000;
                                hours  -= result;

                                vol.setValue(hours);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        startActivity(new Intent(DonorEventDetails.this, DonorActivity.class));

                    }
                });
                builder.show();
                //if they are not signed up, ask if they would like to
            } else {
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
                //pop up ok button
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get the event name
                        String eventname = event.getName();
                        Query eventquery = ref.orderByChild("name").equalTo(eventname);
                        eventquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot eventshot : dataSnapshot.getChildren()) {
                                    //if there are no volunteers yet dont add a comma to the list
                                    if (event.getVolunteers().length() == 0) {
                                        ref.child(eventshot.getKey()).child("volunteers").setValue(event.getVolunteers() + temp);
                                    } else {
                                        //otherwise add a comma for formatting of the list
                                        ref.child(eventshot.getKey()).child("volunteers").setValue(event.getVolunteers() + "," + temp);
                                    }
                                    //decrement needed volunteers
                                    ref.child(eventshot.getKey()).child("volunteersNeeded").setValue(event.getVolunteersNeeded() - 1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getApplicationContext(), "Signed Up", Toast.LENGTH_LONG).show();

                        vol = FirebaseDatabase.getInstance().getReference("DonorDetails").child(user.getDisplayName()).child("hours");
                        vol.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int hours = dataSnapshot.getValue(Integer.class);
                                String[] comparetimes = event.getTime().split("-");
                                Log.d("times", comparetimes[0]);
                                String[] times = event.getTime().split("-");
                                Date start1 = null;
                                Date end1 = null;

                                for(int i = 0; i < comparetimes.length; i++){
                                    SimpleDateFormat mformat = new SimpleDateFormat("HH:mm");
                                    SimpleDateFormat oldformat = new SimpleDateFormat("hh:mma");
                                    Date date = null;
                                    try {
                                        date = oldformat.parse(comparetimes[i]);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    comparetimes[i] = mformat.format(date);
                                    if(i == 0) start1 = date;
                                    if(i == 1) end1 = date;
                                }

                                long start = start1.getTime();
                                long end = end1.getTime();
                                long result = end - start;
                                result = result / 3600000;
                                hours  += result;

                                vol.setValue(hours);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        startActivity(new Intent(DonorEventDetails.this, DonorActivity.class));
                    }
                });
                builder.show();

            }
        }
    }

}
