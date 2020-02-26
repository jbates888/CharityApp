//Activity to create a new event

package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MakeEventActivity extends AppCompatActivity {

    FirebaseDatabase mFirebasedatabase;
    DatabaseReference mRefrence;
    EditText Name, Program, Description, Date, Time, VolsNeeded;
    int maxId = 0;
    Button btn;
    Button cancelBtn;

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_event);

        Name = findViewById(R.id.name_edit);
        Program = findViewById(R.id.program_edit);
        Description = findViewById(R.id.description_edit);
        Date = findViewById(R.id.date_edit);
        Time = findViewById(R.id.time_edit);
        VolsNeeded = findViewById(R.id.volsNeeded_edit);
        btn = findViewById(R.id.create_btn);
        cancelBtn = findViewById(R.id.cancel_btn);

        event = new Event();

        mRefrence = mFirebasedatabase.getInstance().getReference().child("Events");

        mRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxId = (int) dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.setName(Name.getText().toString());
                event.setProgram(Program.getText().toString());
                event.setDescription(Description.getText().toString());
                event.setDate(Date.getText().toString());
                event.setTime(Time.getText().toString());
                event.setFunding("$0");
                event.setVolunteers("");
                event.setVolunteersNeeded(Integer.parseInt(VolsNeeded.getText().toString()));

                mRefrence.child(String.valueOf(maxId+1)).setValue(event);

                Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_LONG).show();

                finish();
                Intent intent = new Intent(MakeEventActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(MakeEventActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
