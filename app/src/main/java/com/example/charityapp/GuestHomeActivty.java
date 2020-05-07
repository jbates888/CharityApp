package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/**
 * @description where guest users are sent after logging in to view all the events
 *
 * @authors Jack Bates
 * @date_created 2/8/20
 * @date_modified 2/28/20
 */

public class GuestHomeActivty extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference ref;
    FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home_activty);
        //set up the custom toolbar
        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        //set up the recycle view to see the events in
        recyclerView = findViewById(R.id.recycler_view);
        //format for recycle viewer
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //create database reference for the event
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Events");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //when the activity start create the recycler
        FirebaseRecyclerAdapter<Event, recycleAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Event, recycleAdapter>(Event.class, R.layout.row, recycleAdapter.class, ref){
                    protected void populateViewHolder(recycleAdapter holder, Event event, int i){
                        holder.setView(getApplicationContext(), event.getName(), event.getProgram(), event.getDate(), event.getTime());
                        //set on click listener for each event in the list
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //when an event is clicked, create an intent and add the extras to send to the details activity
                                Intent intent = new Intent(GuestHomeActivty.this, GuestEventDetails.class);
                                intent.putExtra("Name", event.getName());
                                intent.putExtra("Program", event.getProgram());
                                intent.putExtra("Description", event.getDescription());
                                intent.putExtra("Date", event.getDate());
                                intent.putExtra("Time", event.getTime());
                                intent.putExtra("Funds", event.getFunding());
                                intent.putExtra("Volunteers", event.getVolunteers());
                                //start the new activity
                                startActivity(intent);
                            }
                        });
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu in the o right corner of toolbar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.guestmenu, menu);
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
                //send the user to the main activity
                startActivity(new Intent(this, MainActivity.class));
                return true;
            //help button
            case R.id.ActionHelp:
                //send the user to the help screen
                startActivity(new Intent(this, HelpActivity.class));
                return true;
        }
        return true;
    }
}
