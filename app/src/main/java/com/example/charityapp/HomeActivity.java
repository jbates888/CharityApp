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
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * @description main activity for admins to view all events
 *
 * @authors Jack Bates
 * @date_created
 * @date_modified
 */
public class HomeActivity extends AppCompatActivity  {

    RecyclerView recyclerView;
    DatabaseReference ref, dataReference;
    FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //set up the custom tool bar
        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        //set up the recycle viewer for displaying the events
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //create a database reference for the events
        database = FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Events");
        dataReference = FirebaseDatabase.getInstance().getReference("Data");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Event, recycleAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Event, recycleAdapter>(Event.class, R.layout.row, recycleAdapter.class, ref){
                    protected void populateViewHolder(recycleAdapter holder, Event event, int i){
                        holder.setView(getApplicationContext(), event.getName(), event.getProgram(), event.getDate(), event.getTime());
                        //on click listener for each event in the list
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //create an intent to the detail page of the clicked event
                                Intent intent = new Intent(HomeActivity.this, AdminEventDetails.class);
                                //put all the events details in the intent
                                intent.putExtra("Name", event.getName());
                                intent.putExtra("Program", event.getProgram());
                                intent.putExtra("Description", event.getDescription());
                                intent.putExtra("Date", event.getDate());
                                intent.putExtra("Time", event.getTime());
                                intent.putExtra("Funds", event.getFunding());
                                intent.putExtra("Volunteers", event.getVolunteers());
                                intent.putExtra("VolunteersNeeded", event.getVolunteersNeeded());
                                //start the intent
                                startActivity(intent);
                            }
                        });
                        //long click listener for each event
                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                            public boolean onLongClick(View v){
                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Admin Options");
                                //create a button for deleting the event
                                builder.setNegativeButton("Delete Event", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String eventname = event.getName();
                                        Query eventquery = ref.orderByChild("name").equalTo(eventname);
                                        eventquery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot eventshot: dataSnapshot.getChildren()){
                                                    //remove the event from the database
                                                    eventshot.getRef().removeValue();

                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        //listens for if a event is delted
                                        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //update the admins data
                                                int value = dataSnapshot.child("numEvents").getValue(Integer.class);
                                                dataReference.child("numEvents").setValue(value - 1);
                                                int value2 = dataSnapshot.child("curTotal").getValue(Integer.class);
                                                dataReference.child("curTotal").setValue(value2 - event.getFunding());
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Code
                                            }
                                        });
                                    }
                                });
                                //add a button for modifying the event
                                builder.setPositiveButton("Modify Event", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //create an intent to the modify page of the clicked event
                                        Intent intent = new Intent(HomeActivity.this, EditEventActivity.class);
                                        //put all the events details in the intent
                                        intent.putExtra("Name", event.getName());
                                        intent.putExtra("Program", event.getProgram());
                                        intent.putExtra("Description", event.getDescription());
                                        intent.putExtra("Date", event.getDate());
                                        intent.putExtra("Time", event.getTime());
                                        intent.putExtra("Funds", event.getFunding());
                                        intent.putExtra("Volunteers", event.getVolunteers());
                                        intent.putExtra("VolunteersNeeded", event.getVolunteersNeeded());
                                        //start the intent
                                        startActivity(intent);
                                    }
                                });
                                //add a canlce button the the pop up
                                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //close the pop up
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                                return true;
                            }
                        });
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu in the top right corner of toolbar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //switch statement for when each menu item is selected
        switch (item.getItemId()){
            //logout button
            case R.id.ActionLogout:
                //tell user they logged out and sign them out from firebase
                Toast.makeText(getApplicationContext(), "User Logged out", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.ActionCreate:
                //button for creating a new event
                startActivity(new Intent(this, MakeEventActivity.class));
                return true;
            case R.id.Datadonor:
                startActivity(new Intent(this, DonorHours_Donations.class));
                return true;
            case R.id.ActionAdmin:
                //button to go to the admin firebase console
                Intent broswerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://console.firebase.google.com/u/0/project/charity-app-43ea3/authentication/users"));
                startActivity(broswerIntent);
                return true;
            case R.id.DataAdmin:
                //button to view admin details
                startActivity(new Intent(this, OrgAnalyticsActivity.class));
                return true;
            case R.id.VolHours:
                startActivity(new Intent(this, VolunteerHours.class));
                return true;
            case R.id.ActionHelp:
                startActivity(new Intent(this, HelpActivity.class));
                return true;
        }
        return true;
    }

}
