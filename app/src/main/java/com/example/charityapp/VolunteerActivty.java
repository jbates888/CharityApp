package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
 * @authors AJ Thut, Jack Bates and Felix Estrella
 * @date_created
 * @date_modified
 */
@SuppressWarnings("serial")
public class VolunteerActivty extends AppCompatActivity implements Serializable {

    RecyclerView recyclerView;
    DatabaseReference ref, vol;
    FirebaseDatabase database;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_activty);

        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Events");

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //creating/setting all data for listing the evetns for user
        FirebaseRecyclerAdapter<Event, recycleAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Event, recycleAdapter>(Event.class, R.layout.row, recycleAdapter.class, ref){
                    protected void populateViewHolder(recycleAdapter holder, Event event, int i){
                        holder.setView(getApplicationContext(), event.getName(), event.getProgram(), event.getDate(), event.getTime());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            //passing all of event information to the event details page if user decides to view the event
                            public void onClick(View v) {
                                Intent intent = new Intent(VolunteerActivty.this, VolunteerEventDetails.class);
                                intent.putExtra("Name", event.getName());
                                intent.putExtra("Program", event.getProgram());
                                intent.putExtra("Description", event.getDescription());
                                intent.putExtra("Date", event.getDate());
                                intent.putExtra("Time", event.getTime());
                                intent.putExtra("Funds", event.getFunding());
                                intent.putExtra("Volunteers", event.getVolunteers());
                                intent.putExtra("VolunteersNeeded", event.getVolunteersNeeded());
                                intent.putExtra("index", 1 + holder.getAdapterPosition());
                                intent.putExtra("numVolunteers", event.getNumVolunteers());

                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
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
                                                String month = s.child("date").getValue(String.class);
                                                String[] compareevent = month.split("\\/");
                                                String[] eventdetails = event.getDate().split("\\/");
                                                //checking if event overlaps with other events, checks year, then month, day, then time
                                                if(eventdetails[2].equals(compareevent[2])){
                                                    if(eventdetails[0].equals(compareevent[0])){
                                                        if(eventdetails[1].equals(compareevent[1])){
                                                            String vols = s.child("volunteers").getValue(String.class);
                                                            //check if times overlap if volunteer is signed up for another event
                                                            if(vols.contains(temp)){
                                                                String[] comparetimes = s.child("time").getValue(String.class).split("-");
                                                                String[] times = event.getTime().split("-");
                                                                Date start1 = null;
                                                                Date start2 = null;
                                                                Date end1 = null;
                                                                Date end2 = null;
                                                                //get time for event into format for date object
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
                                                                //get time for other event into format for data object
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
                                                                //check if the times overlap
                                                                if((null == end2 || start1.before(end2)) && (null == end1 || start2.before(end1))){
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

                                return true;

                            }
                        });
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.guestmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ActionLogout:
                Toast.makeText(getApplicationContext(), "User Logged out", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.ActionHelp:
                startActivity(new Intent(this, HelpActivity.class));
                return true;
        }
        return true;
    }

    /**
     * Display correct message to user if they want to sign up for an event
     * @param equals
     * @param count
     * @param event
     * @param temp
     * @param user
     */
    private void displayMessages(int equals, int count, Event event, String temp, FirebaseUser user){
        if(count <= 1){
            //if user is already signed up for event at the same time
            if (equals == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VolunteerActivty.this);
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
                //if we don't need anymore volunteers for an event
            } else if (event.getVolunteersNeeded() <= 0 && !event.getVolunteers().contains(temp)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VolunteerActivty.this);
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
                //if user doesn't want to volunteer for event they are signed up for
            } else if (event.getVolunteers().contains(temp)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VolunteerActivty.this);
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
                                //remove the volunteer for the list of volunteers
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
                                    //get the number of volunteers and subtract one
                                    int val = event.getNumVolunteers() - 1;
                                    //setting values back to database
                                    ref.child(eventshot.getKey()).child("volunteersNeeded").setValue(event.getVolunteersNeeded() + 1);
                                    ref.child(eventshot.getKey()).child("numVolunteers").setValue(val);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getApplicationContext(), "Removed from volunteer list", Toast.LENGTH_LONG).show();
                        //get reference to volunteer in the database
                        vol = FirebaseDatabase.getInstance().getReference("VolHours").child(user.getDisplayName().replace("Volunteer:", "")).child("hours");
                        vol.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            //comparing times is the same as time checking for events above
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
                                //subtract the length of the event from the hours volunteered for user
                                hours  -= result;
                                if(hours < 0){
                                    hours = 0;
                                }
                                //set that value to database
                                vol.setValue(hours);



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                builder.show();
                //if they are not signed up, ask if they would like to
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(VolunteerActivty.this);
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
                        //get reference to volunteer in the database
                        vol = FirebaseDatabase.getInstance().getReference("VolHours").child(user.getDisplayName().replace("Volunteer:", "")).child("hours");
                        vol.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            //time checking is same as time checking for events above
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
                                //get the length of event in hours, add those hours to hours volunteered for the user
                                hours  += result;
                                if(hours < 0){
                                    hours = 0;
                                }
                                vol.setValue(hours);



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                builder.show();

            }
        }
    }
}