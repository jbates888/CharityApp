package com.example.charityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminEventDetails extends AppCompatActivity {

    FirebaseDatabase mFirebasedatabase;
    DatabaseReference mRefrence;

    TextView nameTxt;
    TextView progTxt;
    TextView descTxt;
    TextView dateTxt;
    TextView timeTxt;
    TextView fundsTxt;
    TextView volsTxt;
    TextView volsNeededTxt;

    Button deleteBtn;

    String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_details);


        nameTxt = findViewById(R.id.event_details_title);
        progTxt = findViewById(R.id.event_details_prog);
        descTxt = findViewById(R.id.event_details_desc);
        dateTxt = findViewById(R.id.event_details_date);
        timeTxt = findViewById(R.id.event_details_time);
        fundsTxt = findViewById(R.id.event_details_funds);
        volsTxt = findViewById(R.id.event_details_vols);
        volsNeededTxt = findViewById(R.id.event_details_volsNeeded);

        deleteBtn = findViewById(R.id.delete_btn);


        Bundle extras = getIntent().getExtras();
        nameTxt.setText( extras.getString("Name"));
        progTxt.setText("Program: "  + extras.getString("Program"));
        descTxt.setText("Description: "  +  extras.getString("Description"));
        dateTxt.setText("Date: "  +  extras.getString("Date"));
        timeTxt.setText("Time: "  +  extras.getString("Time"));
        fundsTxt.setText("Funding: "  +  extras.getString("Funds"));
        volsTxt.setText("Volunteers: "  +  extras.getString("Volunteers"));
        volsNeededTxt.setText("Volunteers Needed: "  +  extras.getInt("VolunteersNeeded", 0));

//        index = Integer.toString(extras.getInt("index") + 1);
//
//        Toast.makeText(getApplicationContext(), index, Toast.LENGTH_LONG).show();
//
//        mRefrence = mFirebasedatabase.getInstance().getReference().child("Events");
//
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mRefrence.child(index).removeValue();
//
//                Toast.makeText(getApplicationContext(), "Event Deleted", Toast.LENGTH_LONG).show();
//                finish();
//                startActivity(new Intent(AdminEventDetails.this, HomeActivity.class));
//            }
//        });
    }
}
