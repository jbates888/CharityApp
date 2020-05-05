package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * @description class is for unrestricted donations from the donors
 *
 * @authors Jack Bates, AJ Thut
 * @date_created
 * @date_modified
 */
public class DonateActivity extends AppCompatActivity {
    //declare buttons and text views
    Button donateBtn;
    TextView donateAmount;
    int amount;
    DatabaseReference dataReference, vol;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        //set objects to their id
        donateAmount = findViewById(R.id.donateEditTxt);
        donateBtn = findViewById(R.id.donate_btn);
        //database reference to data
        dataReference = FirebaseDatabase.getInstance().getReference("Data");
        mAuth = FirebaseAuth.getInstance();

        //on click listener for the donate button
        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if entered amount is valid
                if(isValid(donateAmount.getText().toString())){
                    //if it is set amount to it
                    amount = Integer.parseInt(donateAmount.getText().toString());
                } else {
                    //otherwise tell user to enter a valid amount
                    Toast.makeText(getApplicationContext(), "Enter a valid amount", Toast.LENGTH_LONG).show();
                    return;
                }

                dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //update values in the data section of database for admins to view
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

                //finish the activity
                finish();
                //send the user back to the donor's main activity and thank them
                startActivity(new Intent(DonateActivity.this, DonorActivity.class));
                Toast.makeText(getApplicationContext(), "Thank you for donating!", Toast.LENGTH_LONG).show();
                //get refrence in database for the donor
                FirebaseUser user = mAuth.getCurrentUser();
                vol = FirebaseDatabase.getInstance().getReference("DonorDetails").child(user.getDisplayName().replace("Donor:", "")).child("donated");
                vol.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //add donated funds to stats for the donor
                        int donated = dataSnapshot.getValue(Integer.class);
                        donated += amount;
                        vol.setValue(donated);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    //method for checking users donation amount entered
    private boolean isValid(String value){
        //if the user entered a positive int return true
        try{
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException e){
            //otherwise return false
            return false;
        }

    }
}
