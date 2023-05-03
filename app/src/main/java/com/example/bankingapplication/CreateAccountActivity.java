package com.example.bankingapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    EditText firstName, lastName, age;
   RadioGroup radioGroup;
    Button createButton;

    String m_fName,m_lName,m_age,m_gender;
    LocalDataBaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        firstName=findViewById(R.id.editTextTextPersonName);
        lastName = findViewById(R.id.editTextTextPersonName2);
        age= findViewById(R.id.editTextTextPersonName4);


        createButton= findViewById(R.id.button_create);
        radioGroup= findViewById(R.id.radioRg);
        db= new LocalDataBaseHelper(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                if(checkId==R.id.radioButton_male)
                {
                    m_gender= "male";
                    Toast.makeText(CreateAccountActivity.this, "Male clicked", Toast.LENGTH_SHORT).show();
                }
                else {
                    m_gender="female";
                    Toast.makeText(CreateAccountActivity.this, "Female selected", Toast.LENGTH_SHORT).show();
                }

            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                m_fName= firstName.getText().toString().trim();
                m_lName= lastName.getText().toString().trim();
                m_age= age.getText().toString().trim();
                String cardNumber =db.insertNewUser(m_fName,m_lName,m_age,m_gender);
                if(!cardNumber.equals(""))
                {
                    showAlertDialog(cardNumber);
                }
                else {
                    Toast.makeText(CreateAccountActivity.this, "Something went wrong with card number", Toast.LENGTH_SHORT).show();

                }


                // call new insert Method of LocalDatabase Helper class
                Toast.makeText(CreateAccountActivity.this, "Account created", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void showAlertDialog(String cardNumber)
    {
        AlertDialog alertDialog= new AlertDialog.Builder(this).create();
        alertDialog.setTitle("User Created");
        alertDialog.setMessage("Your card number is :"+ cardNumber);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        finish();

                    }
                });
        alertDialog.show();

    }
}
