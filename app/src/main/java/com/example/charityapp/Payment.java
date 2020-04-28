package com.example.charityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @description
 *
 * @authors
 * @date_created
 * @date_modified
 */
public class Payment extends AppCompatActivity {
    //this class is not currently used
    private EditText nameInput, cityInput, stateInput, numberInput, CVVInput;
    private String name, city, state, number, CVV;
    Button donate_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        nameInput = (EditText) findViewById(R.id.card_name);
        numberInput = (EditText) findViewById(R.id.card_number);
        CVVInput = (EditText) findViewById(R.id.card_CVV);
        cityInput = (EditText) findViewById(R.id.city);
        stateInput = (EditText) findViewById(R.id.state);

        donate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString();
                number = numberInput.getText().toString();
                CVV = CVVInput.getText().toString();
                city = nameInput.getText().toString();
                state = nameInput.getText().toString();

                if (isAlpha(name) == true && name.length() >= 3) {
                    if (TextUtils.isDigitsOnly(number) && (number.length() == 15 || number.length() == 16)) {
                        if(TextUtils.isDigitsOnly(CVV) && (CVV.length() == 3 || CVV.length() == 4)) {
                            if (isAlpha(city) == true) {
                                if (isAlpha(state) == true) {
                                    finish();
                                    //startActivity(new Intent(Payment.this, DonorEventDetails.class));
                                    back();
                                    Toast.makeText(getApplicationContext(), "Thank you for donating!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Enter a valid state.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter a valid city.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter a valid CVV.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter a valid card number.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter a valid name", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isAlpha(String str) {
        char[] letters = str.toCharArray();

        for (char c : letters) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    private void back() {
        Intent intent = new Intent(this, DonorEventDetails.class);
        startActivity(intent);
    }
}
