//Activity to create a new event

package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MakeEventActivity extends AppCompatActivity {

    FirebaseDatabase mFirebasedatabase;
    DatabaseReference mRefrence;
    EditText Name, Program, Description, VolsNeeded;
    TextView dateView, timeView;
    int maxId = 0;
    Button btn, Date, Time;
    Button cancelBtn;

    Event event;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_event);

        Name = findViewById(R.id.name_edit);
        Program = findViewById(R.id.program_edit);
        Description = findViewById(R.id.description_edit);
        Date = findViewById(R.id.dateBtn);
        Time = findViewById(R.id.timeBtn);
        dateView = findViewById(R.id.dateViewTxt);
        timeView = findViewById(R.id.timeViewTxt);
        VolsNeeded = findViewById(R.id.volsNeeded_edit);
        btn = findViewById(R.id.create_btn);
        cancelBtn = findViewById(R.id.cancel_btn);

        event = new Event();

        mRefrence = FirebaseDatabase.getInstance().getReference("Events");

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(MakeEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = month + "/" + dayOfMonth + "/" + year;
                dateView.setText(date);
            }
        };

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.setName(Name.getText().toString());
                event.setProgram(Program.getText().toString());
                event.setDescription(Description.getText().toString());
                event.setDate(dateView.getText().toString());
                event.setTime(Time.getText().toString());
                event.setFunding(0);
                event.setVolunteers("");
                event.setVolunteersNeeded(Integer.parseInt(VolsNeeded.getText().toString()));
                event.setNumVolunteers(0);

                mRefrence.child(event.getName()).setValue(event);
                Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_LONG).show();

                finish();
                Intent intent = new Intent(MakeEventActivity.this, HomeActivity.class);
                startActivity(intent);


//                if(Name.getText().toString() != "" && Program.getText().toString() != "" && Description.getText().toString() != "" && Date.getText().toString() != "" && Time.getText().toString() != ""){
//
//                }
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
