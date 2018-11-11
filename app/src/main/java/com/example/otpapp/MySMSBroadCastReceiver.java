package com.example.otpapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class MySMSBroadCastReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str ="";

        if (bundle != null)
        {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                Log.d("ON_RECEIVE", String.valueOf(smsm[i]));
               if( smsm[i].getMessageBody().contains("OUR")){
                   Log.d("ON_RECEIVE_OUR_APP", smsm[i].getMessageBody());
                   sms_str += smsm[i].getMessageBody().toString();
                    sms_str+= "\r\n";

                    String Sender = smsm[i].getOriginatingAddress();
                    //Check here sender is yours
                    Intent smsIntent = new Intent("otp");
                    smsIntent.putExtra("message",sms_str);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                    break;
                }
            }
        }
    }
}
