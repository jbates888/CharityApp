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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;

    EditText emailTxt;
    EditText passTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailTxt = findViewById(R.id.loginUserNameTxt);
        passTxt = findViewById(R.id.loginPassTxt);

        findViewById(R.id.sendToLogin_btn).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);
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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
