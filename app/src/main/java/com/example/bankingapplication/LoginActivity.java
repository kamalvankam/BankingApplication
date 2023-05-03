package com.example.bankingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText cardNumberEt;
    TextView createAccount;
    Intent intent;
    LocalDataBaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardNumberEt = findViewById(R.id.editTextTextPersonName3);
        loginButton= findViewById(R.id.button5);
        createAccount= (TextView) findViewById(R.id.textView2);
        db= new LocalDataBaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call login activity
                String cardNumber= cardNumberEt.getText().toString().trim();
                String result= db.login(cardNumber);
                if(!result.equals(""))
                {
                    Intent homeAc= new Intent(LoginActivity.this,HomeActivity.class);
                    homeAc.putExtra("cardNumber",result);
                    startActivity(homeAc);
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 intent= new Intent(LoginActivity.this,CreateAccountActivity.class);
                 startActivity(intent);
            }
        });



    }




}