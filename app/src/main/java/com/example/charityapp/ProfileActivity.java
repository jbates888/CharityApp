package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.util.ArrayList;

/**
 * @description User decides what type of user they want to be, also creates their display name
 *
 * @authors AJ Thut, Jack Bates
 * @date_created
 * @date_modified
 */
public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText userNameTxt, adminPwordTxt;
    Spinner spinner;
    String type = "";
    FirebaseAuth mAuth;
    DatabaseReference mRefrence, dRefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        userNameTxt = findViewById(R.id.userName_txt);
        adminPwordTxt = findViewById(R.id.adminPword_txt);
        spinner = findViewById(R.id.userdrop);
        mRefrence = FirebaseDatabase.getInstance().getReference("VolHours");
        dRefrence = FirebaseDatabase.getInstance().getReference("DonorDetails");

        spinner.setOnItemSelectedListener(this);

        ArrayList<String> types = new ArrayList<>();
        types.add("Donor");
        types.add("Volunteer");
        types.add("Admin");
        //create dropdown menu for all of the user types
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(dataAdapter);

        loadInfo();

        findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = userNameTxt.getText().toString();
                if(userName.isEmpty()){
                    userNameTxt.setError("Must enter user name");
                    userNameTxt.requestFocus();
                    return;
                } else if(type.equals("Admin") && !adminPwordTxt.getText().toString().equals("AdminPword58")){
                    adminPwordTxt.setError("Must enter correct password to become an Admin");
                    adminPwordTxt.requestFocus();
                    return;
                }
                //creating the user for the database
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                    UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                            .setDisplayName(type + ": " + userName)
                            .build();
                    //updating information for the user in database
                    user.updateProfile(profile)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "User information Updated for " + type + ": " + userName, Toast.LENGTH_LONG).show();
                                    Intent intent;
                                    //start admin main page
                                    if(type.equals("Admin")){
                                        intent = new Intent(ProfileActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        //start donor main page
                                    } else if (type.equals("Donor")){
                                        intent = new Intent(ProfileActivity.this, DonorActivity.class);
                                        //creating donor object, setting all information
                                        Donor d = new Donor();
                                        d.setName(user.getDisplayName().replace("Donor:", ""));
                                        d.setHours(0);
                                        d.setDonated(0);
                                        //creating reference to donor in database
                                        dRefrence.child(d.getName()).setValue(d);
                                        startActivity(intent);
                                        //start volunteer main page
                                    } else if (type.equals("Volunteer")){
                                        intent = new Intent(ProfileActivity.this, VolunteerActivty.class);
                                        //creating volunteer object, setting all information
                                        Volunteer v = new Volunteer();
                                        v.setName(user.getDisplayName().replace("Volunteer:", ""));
                                        v.setHours(0);
                                        //creating reference to volunteer in database
                                        mRefrence.child(v.getName()).setValue(v);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });
    }

    protected void onStart(){
        super.onStart();

        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    /**
     * Set username of the user
     */
    private void loadInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        String userName = user.getDisplayName();

        if(user != null){
            if(user.getDisplayName() != null){
                userNameTxt.setText(userName);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    /**
     * Take user back to login page when logging out
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ActionLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));

        }
        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        type = parent.getItemAtPosition(position).toString();
        if(position != 2){
            adminPwordTxt.setVisibility(View.INVISIBLE);
        } else{
            adminPwordTxt.setVisibility(View.VISIBLE);
        }

    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }
}
