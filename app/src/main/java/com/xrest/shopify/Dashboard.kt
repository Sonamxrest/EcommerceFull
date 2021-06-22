package com.xrest.shopify

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.google.android.material.navigation.NavigationView
import com.xrest.shopify.Fragments.*
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import de.hdodenhof.circleimageview.CircleImageView

class Dashboard : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    val permisstions = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.ACCESS_FINE_LOCATION)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
       change(FrontViewFragment())

        var notificationListeer = Notification(this)
        var notificationManager = NotificationManagerCompat.from(this)
        var notification = NotificationCompat.Builder(this,notificationListeer.channel)
                .setPriority(android.app.Notification.PRIORITY_HIGH).setContentTitle("Hello World").build()
//        notificationManager.notify(1,notification)



        if(!checkPermission())
        {
            askPermission()
        }
        drawerLayout = findViewById(R.id.container)
        navigationView = findViewById(R.id.sideNav)

        if(RetrofitBuilder.user!!.UserType != "Admin")
        {
            //navigationView.menu.getItem(3).isVisible = false

        }
        val header = navigationView.getHeaderView(0)
        var profile:CircleImageView = header.findViewById(R.id.a)
        var name:TextView = header.findViewById(R.id.name)
        Glide.with(applicationContext).load("${RetrofitBuilder.BASE_URL}local/${RetrofitBuilder.user!!.Profile}").into(profile)
        name.text = RetrofitBuilder!!.user!!.Name!!
//Log.d("username",RetrofitBuilder!!.user!!.Name!!)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        toggle.syncState()
        drawerLayout.addDrawerListener(toggle)
         supportActionBar?.title="Home"
          supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {

            when(it.itemId)
            {
R.id.profile->{
  change(AddProduct())
}
                R.id.home->{
                    change(FrontViewFragment())
                }
                R.id.cart ->{
                    change(BookingFragment())

                }
                R.id.order->{
                    change(OrderFragment())
                }
                R.id.logout->{
//
                    var pref = getSharedPreferences("Login", MODE_PRIVATE).edit()
                    pref.clear()
                    pref.apply()
                    pref.commit()
                    var intent = Intent(this,MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                R.id.admin->{
                    change(AdminDaily())
                }
                R.id.ccare->{
                    change(ChatFragment())
                }
            }
            true
        }

    }

    private fun change(allProducts: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, allProducts)
            commit()
            addToBackStack(null)
        }
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(this,permisstions,1)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    fun checkPermission():Boolean
    {
        for(p in permisstions)
        {
            if(ActivityCompat.checkSelfPermission(this,p)!=PackageManager.PERMISSION_GRANTED)
            {
                return false
            }
        }
        return true
    }

}