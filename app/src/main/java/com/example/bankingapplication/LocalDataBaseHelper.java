package com.example.bankingapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.Random;

public class LocalDataBaseHelper {

    public SQLiteDatabase db;
    public Context context;

    public String CREATE_USER_TABLE= "CREATE TABLE IF NOT EXISTS USER_DATA (" +
            "_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "FIRST_NAME VARCHAR, "+
        "LASTNAME VARCHAR, "+
        "AGE VARCHAR," +
        "GENDER VARCHAR,"+
        "BALANCE VARCHAR DEFAULT 0," +
        "CARDNUMBER VARCHAR);";


    public LocalDataBaseHelper(Context ct) {

        this.context= ct;
        this.db= context.openOrCreateDatabase("MyDb",Context.MODE_PRIVATE,null);
        this.db.execSQL(CREATE_USER_TABLE);
        this.db.close();
    }

    public String insertNewUser(String fName, String lName, String age, String gender)
    {
        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
        this.db= context.openOrCreateDatabase("MyDb",Context.MODE_PRIVATE, null);
        String cardNumber= generateCardNumber();
        ContentValues values= new ContentValues();
        values.put("FIRST_NAME", fName);
        values.put("LASTNAME", lName);
        values.put("AGE",age);
        values.put("GENDER", gender);
        values.put("CARDNUMBER", cardNumber);

        long r= this.db.insert("USER_DATA","_ID",values);
        this.db.close();
        if(r!= -1)
        {

            Toast.makeText(context, "New User Inserted", Toast.LENGTH_SHORT).show();
            return cardNumber;
        }
        else {
            Toast.makeText(context, "Insert not successful", Toast.LENGTH_SHORT).show();
            return "";
        }

    }

    public String generateCardNumber()
    {
        String cardNumber;
        Random rd= new Random();
        int number= rd.nextInt(999999);
        cardNumber= String.format("%s",number);
        return cardNumber;

    }

    public String login(String cardNUmber)
    {
        this.db= context.openOrCreateDatabase("MyDb",Context.MODE_PRIVATE,null);
        Cursor cursor= this.db.rawQuery("SELECT * FROM USER_DATA WHERE CARDNUMBER ="+cardNUmber,null);

        if(cursor.getCount()==0)
        {
            Toast.makeText(context, "Card number not found", Toast.LENGTH_SHORT).show();
            this.db.close();
            return "";
        }else {
            this.db.close();
            return cardNUmber;
        }

    }

    @SuppressLint("Range")
    public String getUserBalance(String cardNumber)
    {
        String balance;
        this.db= context.openOrCreateDatabase("MyDb",Context.MODE_PRIVATE,null);

        Cursor cursor= this.db.rawQuery("SELECT * FROM USER_DATA WHERE CARDNUMBER ="+cardNumber,null);
        cursor.moveToFirst();
        balance=cursor.getString(cursor.getColumnIndex("BALANCE"));
        this.db.close();

        return balance;
    }



    public boolean topUp(String cardNumber, String amount)
    {
        boolean done= false;
        float currentAmount= Float.parseFloat(this.getUserBalance(cardNumber));
        this.db= context.openOrCreateDatabase("MyDb",Context.MODE_PRIVATE,null);


        float newAmount= currentAmount+Float.parseFloat(amount);
        ContentValues values= new ContentValues();
        values.put("BALANCE",String.valueOf(newAmount));
       int r=  this.db.update("USER_DATA",values,"CARDNUMBER=?",new String[]{cardNumber});
        if(r>0)
        {
            done= true;
        }
        else {
            done= false;
            Toast.makeText(context, "No rows effected", Toast.LENGTH_SHORT).show();
        }
        this.db.close();

        return done;
    }

    public boolean updateUserBalance(String cardNumber, String amount){

        boolean done= false;
        this.db= context.openOrCreateDatabase("MyDb",Context.MODE_PRIVATE,null);

        ContentValues values= new ContentValues();
        values.put("BALANCE",amount);
      int r=  this.db.update("USER_DATA",values,"CARDNUMBER=?", new String[]{cardNumber});

      if(r> 0)
      {
          done= true;
      }
      else {
          done= false;
          Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
      }
      this.db.close();
      return  done;

    }
    public void transferMoney(String cardNumber, String targetCardNumber, String amount)
    {
        this.db= context.openOrCreateDatabase("MyDb",Context.MODE_PRIVATE,null);
        float sourceBalance= Float.parseFloat(this.getUserBalance(cardNumber));
        float targetBalance= Float.parseFloat(this.getUserBalance(targetCardNumber));
        float transferAmount= Float.parseFloat(amount);

        if(sourceBalance>=transferAmount)
        {
            sourceBalance= sourceBalance-transferAmount;
            targetBalance= targetBalance+transferAmount;

            if(updateUserBalance(cardNumber, String.valueOf(sourceBalance))) {
                if (updateUserBalance(targetCardNumber, String.valueOf(targetBalance)))
                {
                    Toast.makeText(context, "Transfer is done!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Transfer error!", Toast.LENGTH_SHORT).show();

                }

            }
            else {

                Toast.makeText(context, "Transfer error!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(context, "Not enough funds", Toast.LENGTH_SHORT).show();
        }

    }
}
