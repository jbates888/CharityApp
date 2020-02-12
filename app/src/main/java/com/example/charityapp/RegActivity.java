package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

        signup.setOnClickListener(this);
        login.setOnClickListener(this);

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
