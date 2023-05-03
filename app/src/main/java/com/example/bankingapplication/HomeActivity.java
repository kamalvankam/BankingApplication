package com.example.bankingapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView balanceView;
    Button button_topUp, button_transfer;
    String cardNumber;
    LocalDataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        balanceView= findViewById(R.id.textView_accountBalance);
        button_topUp= findViewById(R.id.button_Top);
        button_transfer= findViewById(R.id.button_Trans);

        cardNumber= getIntent().getExtras().getString("cardNumber");
        db= new LocalDataBaseHelper(this);
        balanceView.setText("$ "+db.getUserBalance(cardNumber));

        button_topUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTopUpDialog(cardNumber);
            }
        });
        button_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTransferDialog(cardNumber);
            }
        });



    }

    public void showTopUpDialog(final String sourceCardNumber)
    {
        final AlertDialog dialogBuilder= new AlertDialog.Builder(this).create();
        LayoutInflater inflater= this.getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.activity_topupdialogue,null);

        final EditText amountEt= dialogView.findViewById(R.id.editText_topupEnter);
        Button buttonTopUp= dialogView.findViewById(R.id.button_sendMoney);

        buttonTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.topUp(cardNumber,amountEt.getText().toString())){
                    Toast.makeText(HomeActivity.this, "Top up Complete", Toast.LENGTH_SHORT).show();
                    balanceView.setText("$ "+db.getUserBalance(cardNumber));
                    dialogBuilder.dismiss();
                }

            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public void showTransferDialog(final String cardNumber)
    {
        final AlertDialog alertDialog= new AlertDialog.Builder(this).create();
        LayoutInflater inflater= this.getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.activity_transferdialogue,null);

        final EditText transferAcc= dialogView.findViewById(R.id.editText_transferAccount);
        final EditText transferAmt= dialogView.findViewById(R.id.editText_transferAmount);
        Button trButton= dialogView.findViewById(R.id.button_transferbuttonlayout);
        trButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount= transferAmt.getText().toString().trim();
                String targetCardNumber= transferAcc.getText().toString().trim();

                db.transferMoney(cardNumber,targetCardNumber,amount);
                balanceView.setText("$ "+db.getUserBalance(cardNumber));
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();

    }


}
