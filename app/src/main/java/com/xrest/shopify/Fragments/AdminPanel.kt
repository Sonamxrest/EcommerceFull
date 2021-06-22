package com.xrest.shopify.Fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ShareCompat
import coil.request.LoadRequest.Companion.Builder
import com.xrest.shopify.Dashboard
import com.xrest.shopify.Notification
import com.xrest.shopify.R
import java.text.SimpleDateFormat
import java.util.*

 lateinit var builder : android.app.Notification.Builder
class AdminPanel : Fragment() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_admin_panel2, container, false)
        var dp:DatePicker = view.findViewById(R.id.dp)


        var btn:Button = view.findViewById(R.id.btn)
        btn.setOnClickListener(){
            notifyUser()
        }
        var today = Calendar.getInstance()
        dp.init(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH)){view,year,month,day->
            var fragment = AdminDaily()
            var bundle =Bundle()

            bundle.putString("date","$year/$month/$day")
            fragment.arguments =bundle!!
                requireFragmentManager().beginTransaction().apply {
                replace(R.id.fl,fragment)
                    commit()
            }

            val month = month + 1
            val msg = "You Selected: $year/$month/$day"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        return view
    }




    @RequiresApi(Build.VERSION_CODES.O)
    fun notifyUser(){
         val channelId = "i.apps.notifications"
         val description = "Test notification"
        val contentView = RemoteViews("com.xrest.shopify", R.layout.activity_dashboard)
        var notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            var notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

             builder = android.app.Notification.Builder(requireContext(), channelId).setSmallIcon(R.drawable.cup).setContent(contentView)
        }
        else{
            var intent = Intent(requireContext(),Dashboard::class.java)
            var pendingIntent = PendingIntent.getService(requireActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

             builder = android.app.Notification.Builder(requireContext())
                    .setSmallIcon(R.drawable.cup)
                     .setContent(contentView)
                    .setContentIntent(pendingIntent)
        }

        notificationManager.notify(1234, builder.build())





    }


}