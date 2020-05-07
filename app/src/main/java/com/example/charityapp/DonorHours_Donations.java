package com.example.charityapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;


import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @description Class gets all data needed for the recycleviewer for listing all donors to admin
 *
 * @authors AJ Thut
 * @date_created 3/2/20
 * @date_modified 3/7/20
 */
public class DonorHours_Donations extends AppCompatActivity{

    //viewer, and database references
    RecyclerView recyclerView;
    DatabaseReference ref;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donordetails);
        database = FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("DonorDetails");
        //create the viewer
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //gets data needed to fill the recycle adapter
        FirebaseRecyclerAdapter<Donor, DonorAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Donor, DonorAdapter>(Donor.class, R.layout.donorrow, DonorAdapter.class, ref){
                    protected void populateViewHolder(DonorAdapter holder, Donor d, int i){
                        holder.setView(getApplicationContext(), d.getName(), d.getHours(), d.getDonated());
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
