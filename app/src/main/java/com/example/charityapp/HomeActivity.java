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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class HomeActivity extends AppCompatActivity {

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
        ref = database.getReference("Events");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Event, recycleAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Event, recycleAdapter>(Event.class, R.layout.row, recycleAdapter.class, ref){
                    protected void populateViewHolder(recycleAdapter holder, Event event, int i){
                        holder.setView(getApplicationContext(), event.getName(), event.getProgram(), event.getDate(), event.getTime());
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
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));

        }
        return true;
    }
}

//public class HomeActivity extends AppCompatActivity {
//
//    RecyclerView recyclerView;
//    String s1[], s2[];
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//
//        Toolbar toolbar = findViewById(R.id.appbar);
//        setSupportActionBar(toolbar);
//
//        recyclerView = findViewById(R.id.recycler_view);
//
//        s1 = getResources().getStringArray(R.array.languages);
//        s2 = getResources().getStringArray(R.array.description);
//
//        recycleAdapter recycleAdapter = new recycleAdapter(this, s1, s2);
//        recyclerView.setAdapter(recycleAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.ActionLogout:
//                FirebaseAuth.getInstance().signOut();
//                finish();
//                startActivity(new Intent(this, MainActivity.class));
//
//        }
//        return true;
//    }
//}
//
