package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailTxt;
    EditText passTxt;
    Button signup;
    Button login;
    Button passhide;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        mAuth = FirebaseAuth.getInstance();

        emailTxt = findViewById(R.id.email_txt);
        passTxt = findViewById(R.id.pass_txt);
        signup = findViewById(R.id.signup_btn);
        login = findViewById(R.id.sendToLogin_btn);
        progressBar = findViewById(R.id.regProgBar);
        passhide = findViewById(R.id.showpass);

        signup.setOnClickListener(this);
        login.setOnClickListener(this);

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

        passTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                    registerUser();
                }
                return false;
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signup_btn:
                registerUser();
                break;

            case R.id.sendToLogin_btn:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    private void registerUser() {
        String email = emailTxt.getText().toString();
        String password = passTxt.getText().toString();

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

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(RegActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
