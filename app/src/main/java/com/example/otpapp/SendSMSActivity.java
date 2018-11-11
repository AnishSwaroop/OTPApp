package com.example.otpapp;
import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SendSMSActivity extends Activity {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    Button buttonSend;
    Button buttonVerify;
    EditText textPhoneNo;
    int randomNumber = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }
        buttonSend = (Button) findViewById(R.id.buttonSend);
        textPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        buttonVerify = (Button) findViewById(R.id.buttonVerify);


        buttonSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String phoneNo = textPhoneNo.getText().toString();
                Random rand = new Random();
                randomNumber = rand.nextInt(10000);
                String sms = "Welcome to OUR app.Do not share this code anyone for security reasons. Your Unique OTP is :"+randomNumber;

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "SMS Sent!",
                            Toast.LENGTH_LONG).show();
                    LinearLayout linearLayoutSendingText=(LinearLayout)findViewById(R.id.linearLayoutSendingText);
                    linearLayoutSendingText.setVisibility(LinearLayout.GONE);

                    LinearLayout linearLayoutOtp=(LinearLayout)findViewById(R.id.layout_otp);
                    linearLayoutOtp.setVisibility(LinearLayout.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        buttonVerify.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String otpFromEditField="";
                EditText et1 = (EditText) findViewById(R.id.editTextone);
                otpFromEditField+=et1.getText();

                EditText et2 = (EditText) findViewById(R.id.editTexttwo);
                otpFromEditField+=et2.getText();

                EditText et3 = (EditText) findViewById(R.id.editTextthree);
                otpFromEditField+=et3.getText();

                EditText et4 = (EditText) findViewById(R.id.editTextfour);
                otpFromEditField+=et4.getText();

                TextView tv = (TextView) findViewById(R.id.txtview);
                LinearLayout linearLayoutOtp=(LinearLayout)findViewById(R.id.layout_otp);
                linearLayoutOtp.setVisibility(LinearLayout.GONE);
                if(Integer.parseInt(otpFromEditField) == randomNumber) {
                    tv.setText("Authentication Succesfully");
                }
                else {
                    tv.setText("Authentication Failed");
                }
            }
        });
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

                Log.d("RECEIVER_MESSAGE",message);
                String otpFromMessage = message.split(":")[1];
                Log.d("RECEIVER_MESSAGE_OTP",otpFromMessage);
                EditText et1 = (EditText) findViewById(R.id.editTextone);
                et1.setText(""+otpFromMessage.charAt(0));

                EditText et2 = (EditText) findViewById(R.id.editTexttwo);
                et2.setText(""+otpFromMessage.charAt(1));

                EditText et3 = (EditText) findViewById(R.id.editTextthree);
                et3.setText(""+otpFromMessage.charAt(2));

                EditText et4 = (EditText) findViewById(R.id.editTextfour);
                et4.setText(""+otpFromMessage.charAt(3));

            }
        }
    };

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}