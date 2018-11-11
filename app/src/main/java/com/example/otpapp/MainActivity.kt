package com.example.otpapp

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.nio.file.Files.size
import android.support.v4.app.ActivityCompat
import android.Manifest.permission
import android.Manifest.permission.SEND_SMS
import android.content.pm.PackageManager
import android.Manifest.permission.READ_SMS
import android.Manifest.permission.RECEIVE_MMS
import android.support.v4.content.ContextCompat
import android.Manifest.permission.RECEIVE_SMS
import java.util.ArrayList
import android.widget.TextView
//import com.sun.xml.internal.bind.v2.schemagen.Util.equalsIgnoreCase
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.support.v4.content.LocalBroadcastManager
import android.content.IntentFilter
import android.view.View


class MainActivity : AppCompatActivity() {
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }

    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissionSendMessage = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        )

        val receiveSMS = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECEIVE_SMS
        )

        val readSMS = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_SMS
        )
        val listPermissionsNeeded = ArrayList<String>()

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS)
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS)
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action!!.equals("otp", ignoreCase = true)) {
                val message = intent.getStringExtra("message")

                val tv = findViewById<View>(R.id.txtview) as TextView
                tv.text = message
            }
        }
    }

    public override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("otp"))
        super.onResume()
    }

    public override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }
}
