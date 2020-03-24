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

public class HomeActivity extends AppCompatActivity  {

    RecyclerView recyclerView;
    DatabaseReference ref;
    FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Events");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Event, recycleAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Event, recycleAdapter>(Event.class, R.layout.row, recycleAdapter.class, ref){
                    protected void populateViewHolder(recycleAdapter holder, Event event, int i){
                        holder.setView(getApplicationContext(), event.getName(), event.getProgram(), event.getDate(), event.getTime());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this, AdminEventDetails.class);
                                intent.putExtra("Name", event.getName());
                                intent.putExtra("Program", event.getProgram());
                                intent.putExtra("Description", event.getDescription());
                                intent.putExtra("Date", event.getDate());
                                intent.putExtra("Time", event.getTime());
                                intent.putExtra("Funds", event.getFunding());
                                intent.putExtra("Volunteers", event.getVolunteers());
                                intent.putExtra("VolunteersNeeded", event.getVolunteersNeeded());

                                startActivity(intent);

                            }
                        });

                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                            public boolean onLongClick(View v){
                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Admin Options");

                                builder.setNegativeButton("Delete Event", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String eventname = event.getName();
                                        Query eventquery = ref.orderByChild("name").equalTo(eventname);
                                        eventquery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot eventshot: dataSnapshot.getChildren()){
                                                    eventshot.getRef().removeValue();

                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });

                                builder.setPositiveButton("Modify Event", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
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
            case R.id.ActionCreate:
                finish();
                startActivity(new Intent(this, MakeEventActivity.class));
                return true;
            case R.id.ActionAdmin:
                Intent broswerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://console.firebase.google.com/u/0/project/charity-app-43ea3/authentication/users"));
                startActivity(broswerIntent);
                return true;
        }
        return true;
    }

}
