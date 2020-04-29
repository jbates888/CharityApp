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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description
 *
 * @authors
 * @date_created
 * @date_modified
 */
public class VolunteerEventDetails extends AppCompatActivity {

    TextView nameTxt;
    TextView progTxt;
    TextView descTxt;
    TextView dateTxt;
    TextView timeTxt;
    TextView fundsTxt;
    TextView volsTxt;
    TextView volsNeededTxt;
    Button volunteerBtn;
    DatabaseReference ref;
    DatabaseReference vol;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_event_details);

        nameTxt = findViewById(R.id.event_details_title);
        progTxt = findViewById(R.id.event_details_prog);
        descTxt = findViewById(R.id.event_details_desc);
        dateTxt = findViewById(R.id.event_details_date);
        timeTxt = findViewById(R.id.event_details_time);
        fundsTxt = findViewById(R.id.event_details_funds);
        volsTxt = findViewById(R.id.event_details_vols);
        volsNeededTxt = findViewById(R.id.event_details_volsNeeded);
        volunteerBtn = findViewById(R.id.volBtn);

        Bundle extras = getIntent().getExtras();
        nameTxt.setText( extras.getString("Name"));
        progTxt.setText("Program: "  + extras.getString("Program"));
        descTxt.setText("Description: "  +  extras.getString("Description"));
        dateTxt.setText("Date: "  +  extras.getString("Date"));
        timeTxt.setText("Time: "  +  extras.getString("Time"));
        fundsTxt.setText("Funding: $"  +  extras.getInt("Funds", 0));
        volsTxt.setText("Volunteers: "  +  extras.getString("Volunteers"));
        volsNeededTxt.setText("Volunteers Needed: "  +  extras.getInt("VolunteersNeeded", 0));
        Event event = new Event(extras.getString("Name"), extras.getString("Program"), extras.getString("Date"), extras.getString("Time"), extras.getInt("Funds"),
                extras.getString("Description"), extras.getString("Volunteers"), extras.getInt("VolunteersNeeded"), extras.getInt("numVolunteers"));

        ref = FirebaseDatabase.getInstance().getReference("Events");
        mAuth = FirebaseAuth.getInstance();


        volunteerBtn.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {

                String eventname = extras.getString("Name");
                Query eventquery = ref.orderByChild("name").equalTo(eventname);

                FirebaseUser user = mAuth.getCurrentUser();
                String temp = user.getDisplayName().replaceAll("Volunteer:", "");


            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("volunteeractivity", "Number of events" + Long.toString(dataSnapshot.getChildrenCount()));
                    Iterable<DataSnapshot> events = dataSnapshot.getChildren();
                    int[] two_events = {0};
                    int count = 0;
                    for (DataSnapshot s : events) {

                        if(event.getName().equals(s.getKey())){
                            //do nothing
                        } else {
                            Log.d("volunteeractivity", "Event name" + s.getKey());

                            //two_events[0] = 1;
                            Log.d("volunteeractivity", "Date after test" + two_events[0]);
                            String month = s.child("date").getValue(String.class);
                            String[] compareevent = month.split("\\/");
                            //Log.d("split the date", comapareevent[2]);
                            String[] eventdetails = event.getDate().split("\\/");
                            if(eventdetails[2].equals(compareevent[2])){
                                if(eventdetails[0].equals(compareevent[0])){
                                    if(eventdetails[1].equals(compareevent[1])){
                                        String vols = s.child("volunteers").getValue(String.class);
                                        if(vols.contains(temp)){
                                            String[] comparetimes = s.child("time").getValue(String.class).split("-");
                                            Log.d("times", comparetimes[0]);
                                            String[] times = event.getTime().split("-");
                                            Date start1 = null;
                                            Date start2 = null;
                                            Date end1 = null;
                                            Date end2 = null;
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

                                            for(int i = 0; i < times.length; i++){
                                                SimpleDateFormat mformat = new SimpleDateFormat("HH:mm");
                                                SimpleDateFormat oldformat = new SimpleDateFormat("hh:mma");
                                                Date date = null;
                                                try {
                                                    date = oldformat.parse(times[i]);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                times[i] = mformat.format(date);
                                                if(i == 0) start2 = date;
                                                if(i == 1) end2 = date;
                                            }
                                            if((null == end2 || start1.before(end2)) && (null == end1 || start2.before(end1))){
                                                two_events[0] = 1;
                                            }
                                        }
//                                                            two_events[0] = 1;
                                    }
                                }
//                                                    String day = s.child("date").getValue(String.class);
//                                                    String eventday = event.getDate();
//                                                    if (day.charAt(2) == '/') {
//                                                        day = day.substring(0, 2);
//                                                    } else {
//                                                        month = month.substring(0, 3);
//                                                    }
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

    private void displayMessages(int equals, int count, Event event, String temp, FirebaseUser user){
        if(count <= 1){
            if (equals == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VolunteerEventDetails.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(VolunteerEventDetails.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(VolunteerEventDetails.this);
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

                        vol = FirebaseDatabase.getInstance().getReference("VolHours").child(user.getDisplayName()).child("hours");
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
                                if(hours < 0){
                                    hours = 0;
                                }
                                vol.setValue(hours);



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        startActivity(new Intent(VolunteerEventDetails.this, VolunteerActivty.class));
                    }



                });
                builder.show();
                //if they are not signed up, ask if they would like to
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(VolunteerEventDetails.this);
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
                        String eventname = event.getName();
                        Query eventquery = ref.orderByChild("name").equalTo(eventname);
                        eventquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot eventshot : dataSnapshot.getChildren()) {
                                    //Toast.makeText(getApplicationContext(), eventshot.getKey(), Toast.LENGTH_LONG).show();
                                    if (event.getVolunteers().length() == 0) {
                                        ref.child(eventshot.getKey()).child("volunteers").setValue(event.getVolunteers() + temp);
                                    } else {
                                        ref.child(eventshot.getKey()).child("volunteers").setValue(event.getVolunteers() + "," + temp);
                                    }
                                    ref.child(eventshot.getKey()).child("volunteersNeeded").setValue(event.getVolunteersNeeded() - 1);
                                }
                            }



                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(getApplicationContext(), "Signed Up", Toast.LENGTH_LONG).show();

                        vol = FirebaseDatabase.getInstance().getReference("VolHours").child(user.getDisplayName()).child("hours");
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
                        startActivity(new Intent(VolunteerEventDetails.this, VolunteerActivty.class));
                    }
                });
                builder.show();

            }
        }
    }
}
