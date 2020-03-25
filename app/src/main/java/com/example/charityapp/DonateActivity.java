package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DonateActivity extends AppCompatActivity {

    Button donateBtn;
    TextView donateAmount;
    int amount;
    DatabaseReference dataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        donateAmount = findViewById(R.id.donateEditTxt);
        donateBtn = findViewById(R.id.donate_btn);
        dataReference = FirebaseDatabase.getInstance().getReference("Data");

        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValid(donateAmount.getText().toString())){
                    amount = Integer.parseInt(donateAmount.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Enter a valid amount", Toast.LENGTH_LONG).show();
                    return;
                }

                dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int value = dataSnapshot.child("Total").getValue(Integer.class);
                        dataReference.child("Total").setValue(value + amount);
                        int value2 = dataSnapshot.child("curTotal").getValue(Integer.class);
                        dataReference.child("curTotal").setValue(value2 + amount);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Code
                    }
                });

                finish();
                startActivity(new Intent(DonateActivity.this, DonorActivity.class));
                Toast.makeText(getApplicationContext(), "Thank you for donating!", Toast.LENGTH_LONG).show();
            }
        });


    }

    private boolean isValid(String value){
        try{
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException e){
            return false;
        }

    }
}
