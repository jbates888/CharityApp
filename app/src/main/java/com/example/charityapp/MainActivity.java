package com.example.charityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;

    EditText emailTxt;
    EditText passTxt;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailTxt = findViewById(R.id.loginUserNameTxt);
        passTxt = findViewById(R.id.loginPassTxt);
        progressBar = findViewById(R.id.loginProgBar);


        findViewById(R.id.sendToLogin_btn).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);
        findViewById(R.id.guest_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendToLogin_btn:
                finish();
                Intent toSignIntent = new Intent(this, RegActivity.class);
                startActivity(toSignIntent);
                break;
            case R.id.loginBtn:
                login();
                break;
            case R.id.guest_btn:
                finish();
                Intent toGuestIntent = new Intent(this, GuestHomeActivty.class);
                startActivity(toGuestIntent);
                break;
        }
    }

    protected void onStart(){
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
    private void login(){
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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent;
                    FirebaseUser user = mAuth.getCurrentUser();
                    String username;
                    if(user != null){
                        if(user.getDisplayName() != null){
                            username = user.getDisplayName();
                            if(username.charAt(0) == 'A'){
                                finish();
                                Toast.makeText(getApplicationContext(),username, Toast.LENGTH_LONG).show();
                                intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else if (username.charAt(0) == 'V'){
                                finish();
                                Toast.makeText(getApplicationContext(),username, Toast.LENGTH_LONG).show();
                                intent = new Intent(MainActivity.this, VolunteerActivty.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else if (username.charAt(0) == 'D'){
                                finish();
                                Toast.makeText(getApplicationContext(),username, Toast.LENGTH_LONG).show();
                                intent = new Intent(MainActivity.this, DonorActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                finish();
                                Toast.makeText(getApplicationContext(),"Failed " + username, Toast.LENGTH_LONG).show();
                                intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"Failed to find user", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
