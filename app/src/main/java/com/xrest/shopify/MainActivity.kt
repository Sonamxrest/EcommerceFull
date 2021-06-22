package com.xrest.shopify

import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorListener
import android.hardware.SensorManager
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.google.android.material.snackbar.Snackbar
import com.xrest.shopify.Fragments.*
import java.util.concurrent.CancellationException
import java.util.concurrent.Executor



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        if(NetworkConnection.isNetwworkConnected(this@MainActivity))
        {supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl, BlankFragment())
            commit()
        }

        }
        else{
            Toast.makeText(this, "No network Connection", Toast.LENGTH_SHORT).show()
        }




    }


}