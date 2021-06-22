package com.xrest.shopify

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

class Notification(var context: Context) {
var channel ="Channel1"
    fun showNotification(){
        var manager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
           var channel = NotificationChannel("1",channel,NotificationManager.IMPORTANCE_HIGH)
            var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        } else {

        }


    }

}