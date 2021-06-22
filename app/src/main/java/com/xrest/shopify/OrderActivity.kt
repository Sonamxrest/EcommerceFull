package com.xrest.shopify

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
//import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.xrest.shopify.Adapters.BookingAdapter
import com.xrest.shopify.Retrofit.Class.Order
import com.xrest.shopify.Retrofit.Repository.OrderRepository
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class OrderActivity : AppCompatActivity(),OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {
    lateinit var googleMap:GoogleMap
    var orders:Order?=null
    lateinit var cm: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        var txt:TextView = findViewById(R.id.txt)
        orders = intent.getSerializableExtra("order") as Order
        val rv: RecyclerView = findViewById(R.id.rv)
        var order: LinearLayout = findViewById(R.id.order)
        var price: TextView = findViewById(R.id.price)
        supportActionBar!!.setTitle("Order")
        if(orders!!.orderStatus!!)
        {
            order.isVisible =false
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        rv.layoutManager = LinearLayoutManager(this)
        var adapter = BookingAdapter(applicationContext, orders!!.Product!!)
        price.text = orders!!.Total.toString()
        rv.adapter =adapter
        if(RetrofitBuilder.user!!.UserType=="Admin")
        {

            if(orders!!.orderStatus==true)
            {
                order.isVisible =false
            }
            order.setBackgroundResource(R.drawable.add_to_cart)
            order.setOnClickListener(this)
            txt.text ="Accept Order :-"

        }
        else if(RetrofitBuilder.user!!.UserType=="Customer"){
            if(orders!!.orderStatus==true)
            {
                order.isVisible =false
            }
            order.setBackgroundResource(R.drawable.add_to_cart2)
            order.setOnClickListener(){
                cancellOrder(orders!!._id)
            }

        }
        else{

            if(orders!!.orderStatus==true && orders!!.DeliveredStatus==true)
            {
                order.isVisible =false
            }
            order.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    deliverOrder(orders!!._id!!)
                }

            })
            txt.text ="Deliver Order :-"

        }
    }

    private fun deliverOrder(_id: String) {
CoroutineScope(Dispatchers.IO).launch {

    val response = OrderRepository().deliver(_id)
    if(response.success==true)
    {
        withContext(Main)
        {
            val intent = Intent(this@OrderActivity,DirectionActivity::class.java)
            RetrofitBuilder.lat = LatLng(orders!!.Latitude!!.toDouble(),orders!!.Longtitude!!.toDouble())
            startActivity(intent)
        }
    }

}
    }

    private fun cancellOrder(_id: String?) {

        CoroutineScope(Dispatchers.IO).launch {

            val response = OrderRepository().cancelOrder(_id!!)
            if(response.success==true) {

                withContext(Main)
                {
                    Snackbar.make(findViewById(R.id.view),"Order Cancelled",Snackbar.LENGTH_LONG)
                    var intent = Intent(this@OrderActivity,Dashboard::class.java)
                    startActivity(intent)
                }


            }



        }


    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        googleMap=p0

         var latlng = LatLng(orders!!.Latitude!!.toDouble(), orders!!.Longtitude!!.toDouble())

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 20f), 5000, null)
        googleMap.uiSettings.isZoomControlsEnabled =true
                   cm = googleMap.addMarker(MarkerOptions().position(latlng).title(orders!!.Address).draggable(false))
        cm!!.showInfoWindow()
        googleMap.setOnMapClickListener(this)

    }

    override fun onMapClick(p0: LatLng) {
        googleMap.addMarker(MarkerOptions().position(p0).title("Destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_shopping_cart_24)))
        var latLng=  LatLng(orders!!.Latitude!!.toDouble(), orders!!.Longtitude!!.toDouble())
        var destination = p0
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p0, 14f))
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.order->{
try {

CoroutineScope(Dispatchers.IO).launch {

    val response = OrderRepository().acceptOrder(orders!!._id!!)
    if(response.success==true)
    {
        withContext(Main)
        {
            Snackbar.make(findViewById(R.id.view),"Order Accepted",Snackbar.LENGTH_LONG)
            var intent = Intent(this@OrderActivity,Dashboard::class.java)
            startActivity(intent)
        }
    }


}
}
catch (ex:Exception){

}
            }
        }
    }
}