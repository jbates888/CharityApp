package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @description main activity where app starts, the log in page for users
 *
 * @authors Jack Bates and Felix Estrella
 * @date_created
 * @date_modified
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Fire base authentication object
    FirebaseAuth mAuth;
    Button passhide;
    //Text field objects and progress bar
    EditText emailTxt;
    EditText passTxt;
    ProgressBar progressBar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the screen to the login page
        setContentView(R.layout.activity_main);
        //instantiating the authentication object
        mAuth = FirebaseAuth.getInstance();
        //instantiating the text field objects
        emailTxt = findViewById(R.id.loginUserNameTxt);
        passTxt = findViewById(R.id.loginPassTxt);
        //instantiating the progress bar
        progressBar = findViewById(R.id.loginProgBar);
        passhide = findViewById(R.id.showpass);

        //setting an on click listener to every button on the screen
        findViewById(R.id.sendToLogin_btn).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);
        findViewById(R.id.guest_btn).setOnClickListener(this);

        // Will set the ontouchListener() method to the passhide button to determine if it is being held or not
        passhide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Call the method getAction to determine if the button is up or down
                switch (event.getAction()) {
                    // When the button is held show the password and display "hide"
                    case MotionEvent.ACTION_DOWN: {
                        passTxt.setTransformationMethod(null);
                        passhide.setText("Hide");
                        break;
                    }
                    // When the button is not pressed hide the password and display "show"
                    case MotionEvent.ACTION_UP: {
                        passTxt.setTransformationMethod(new PasswordTransformationMethod());
                        passhide.setText("Show");
                    }
                }
                return true;
            }
        });

        //edit listener for the password field
        passTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //check for valid key code
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                    login();
                }
                return false;
            }
        });
    }

    @Override
    /**
     * Function called when a button on the screen is clicked
     */
    public void onClick(View v) {
        switch (v.getId()){
            //if the button to create an account is clicked
            case R.id.sendToLogin_btn:
                //finish the current activity
                finish();
                //create a new intent to the register activity page and then start the activity
                Intent toSignIntent = new Intent(this, RegActivity.class);
                startActivity(toSignIntent);
                break;
            //if the login button is clicked
            case R.id.loginBtn:
                //go to login function
                login();
                break;
            //if the guest button is clicked
            case R.id.guest_btn:
                //finish the current activity
                finish();
                //create a new intent to the guest home page and then start the activity
                Intent toGuestIntent = new Intent(this, GuestHomeActivty.class);
                startActivity(toGuestIntent);
                break;
        }

    }

    /**
     * Function that checks if the currently logged in user is not null
     */
    protected void onStart(){
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    /**
     * Function that checks the credentials of the user. If valid, takes them to the correct
     * home page based on the type of user.
     */
    private void login(){
        //get the email and password from the text fields
        String email = emailTxt.getText().toString();
        String password = passTxt.getText().toString();

        //if the email or password text field is empty, prompt the user to enter all information
        if(email.isEmpty()){
            emailTxt.setError("Must enter email");
            emailTxt.requestFocus();
            return;
        }
        if (password.isEmpty()){
            passTxt.setError("Must enter password");
            passTxt.requestFocus();
            return;
        }

        //make the progress bar visible
        progressBar.setVisibility(View.VISIBLE);

        //sign in the user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            //if the user was signed
            public void onComplete(@NonNull Task<AuthResult> task) {
                //make the progress bar invisible
                progressBar.setVisibility(View.GONE);
                //if the user was signed in with a valid email and password
                if(task.isSuccessful()){
                    //create a new intent object
                    Intent intent;
                    //get the current logged in user
                    FirebaseUser user = mAuth.getCurrentUser();
                    //string for the username of the current user
                    String username;
                    //if the user is not null
                    if(user != null){
                        //if the user has a profile name
                        if(user.getDisplayName() != null){
                            //set the username string to the profile name of the user
                            username = user.getDisplayName();
                            //check if the user is an admin
                            if(username.charAt(0) == 'A'){
                                finish();
                                //create a pop up that displays the username
                                Toast.makeText(getApplicationContext(),username, Toast.LENGTH_LONG).show();
                                //make the intent object and send the user to the admin home page
                                intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                              //check if the user is a volunteer
                            } else if (username.charAt(0) == 'V'){
                                finish();
                                //create a pop up that displays the username
                                Toast.makeText(getApplicationContext(),username, Toast.LENGTH_LONG).show();
                                //make the intent object and send the user to the volunteer home page
                                intent = new Intent(MainActivity.this, VolunteerActivty.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                              //check if the user is a donor
                            } else if (username.charAt(0) == 'D'){
                                finish();
                                //create a pop up that displays the username
                                Toast.makeText(getApplicationContext(),username, Toast.LENGTH_LONG).show();
                                //make the intent object and send the user to the donor home page
                                intent = new Intent(MainActivity.this, DonorActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                //if for some reason the user doesn't have a valid profile name
                                //create page to create valid profile name?
                                finish();
                                Toast.makeText(getApplicationContext(),"Failed login" + username, Toast.LENGTH_LONG).show();
                                intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        } else{
                            Intent intentProf = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intentProf);
                        }
                    } else {
                        //prompt the user that there was a failed login
                        Toast.makeText(getApplicationContext(),"Failed to find user", Toast.LENGTH_LONG).show();
                    }


                }else{
                    //prompt an error message to the user
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
