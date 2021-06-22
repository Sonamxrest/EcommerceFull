package com.xrest.shopify

import android.Manifest
import android.R.attr.apiKey
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xrest.shopify.Retrofit.Class.Order
import com.xrest.shopify.Retrofit.Repository.OrderRepository
import kotlinx.android.synthetic.main.fragment_add_product.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener{

    lateinit var googleMap: GoogleMap
    var cm:Marker?=null

    var fusedLocationProviderClient: FusedLocationProviderClient?=null
    var currentLocation:Location? =null
    var permission = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    var finalLocation:LatLng?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        var search:FloatingActionButton = findViewById(R.id.search)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        val placesClient = Places.createClient(this)
search.setOnClickListener(){
    var field :MutableList<Place.Field> = mutableListOf()
    field.add(Place.Field.ID)
    field.add(Place.Field.NAME)
    try {
        var intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, field).build(this)
        startActivityForResult(intent, 200)
    }
    catch (ex: GooglePlayServicesNotAvailableException)
    {
ex.printStackTrace()
    }
//    var intent = Plac
//            .accessToken(getString(R.string.access_token)).placeOptions(PlaceOptions.builder().limit(10).backgroundColor(Color.parseColor("#EEEEEE"))
//            .build(PlaceOptions.MODE_CARDS)).build(this)
//    startActivityForResult(intent,7171)

}
        supportActionBar!!.hide()
        var btn:Button = findViewById(R.id.order)
        btn.setOnClickListener(){
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Order Confirmation").setIcon(R.drawable.ic_baseline_shopping_cart_24).setMessage(
                "Are You Sure To Proceed?"
            )
            alertDialog.setPositiveButton("Yes"){ dialog, which->

                var dialog = Dialog(this@MapsActivity)
                dialog.setContentView(R.layout.success_notification)
                dialog.window?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                var lottie:LottieAnimationView = dialog.findViewById(R.id.lottie)
                var loading:TextView = dialog.findViewById(R.id.success)
                dialog.setCancelable(false)
                dialog.show()

            CoroutineScope(Dispatchers.IO).launch {
                val repo = OrderRepository().placeOrder(
                    Order(
                        Latitude = finalLocation?.latitude.toString(),
                        Longtitude = finalLocation?.longitude.toString(),
                        Address = getAddress(
                            finalLocation!!.latitude,
                            finalLocation!!.longitude
                        )
                    )
                )
               delay(2500)
                if(repo.success==true)
                {
                    withContext(Main){
                        lottie.setAnimation(R.raw.success)
                        loading.text="Success"
                       delay(2000)
                    }

                }
            }
                dialog.cancel()
            }

            alertDialog.setNegativeButton("No", DialogInterface.OnClickListener() { dialog, which ->

                dialog.cancel()


            })
            val dialog = alertDialog.create()
            dialog.show()
            dialog.setCancelable(true)
        }


    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
         ActivityCompat.requestPermissions(this, permission, 1)
        }
        var track = fusedLocationProviderClient!!.lastLocation
        track.addOnSuccessListener { location->
            if(location!=null)
            { this.currentLocation =location
                Log.d("location", location.toString())
                var map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                map.getMapAsync(this)
            }
        }
        track.addOnFailureListener{ ex->
            ex.printStackTrace()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode ==200)
        {
                var place:Place = Autocomplete.getPlaceFromIntent(data!!)
                var name = place.name.toString()
                var location = place.getLatLng()
                drawMarker(location!!)






        }
    }
    @SuppressWarnings("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        googleMap=p0!!
//        googleMap.isMyLocationEnabled = true
//        googleMap.setOnMyLocationChangeListener(){ location ->
//            var latlng = LatLng(location.latitude,location.longitude)
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,18f),4000,null)
//        }
        var location = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(location))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16f), 4000, null)
        cm = googleMap.addMarker(
            MarkerOptions().position(location).title("Current Location").draggable(
                true
            ).icon(BitmapDescriptorFactory.defaultMarker(5f))
        )
      //  cm = googleMap.addMarker(MarkerOptions().position(location).title("Your Location").draggable(true).snippet(getAddress(location!!.latitude,location.longitude)).icon(BitmapDescriptorFactory.defaultMarker()))
        cm!!.showInfoWindow()
        googleMap.uiSettings.isZoomControlsEnabled =true
        googleMap.setOnMapClickListener(this)


        googleMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(p0: Marker) {

            }

            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(p0: Marker) {

                finalLocation = LatLng(p0.position.latitude, p0.position.longitude)
                cm!!.remove()
                drawMarker(latlng = finalLocation!!)
            }

        })

    }




    fun drawMarker(latlng: LatLng){
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16f), 5000, null)
        googleMap.uiSettings.isZoomControlsEnabled =true
        cm = googleMap.addMarker(
            MarkerOptions().position(latlng).title("Current Location").draggable(
                true
            ).snippet(getAddress(latlng!!.latitude, latlng.longitude))
        )
        cm!!.showInfoWindow()

    }


    private fun getAddress(latitude: Double, longitude: Double):String {
        val gc= Geocoder(this, Locale.getDefault())
        val arrayAddress= gc.getFromLocation(latitude, longitude, 1)
        return arrayAddress[0].getAddressLine(0).toString()
    }


    override fun onMapClick(p0: LatLng) {
        var drawable = BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_baseline_shopping_cart_24
        )
        googleMap.addMarker(MarkerOptions().title("Destination").position(p0).draggable(false))
        var data:FloatArray = FloatArray(10)
        Location.distanceBetween(
            currentLocation!!.latitude,
            currentLocation!!.longitude!!,
            p0.latitude,
            p0.longitude,
            data
        )
        Log.d("distance", data[0].toString())

    }

fun checkPermission():Boolean{

    for(p in permission)
    {
        if(ActivityCompat.checkSelfPermission(this, p)!=PackageManager.PERMISSION_GRANTED)
        {
            return false
        }
    }
    return true
}
    fun requestPermission(){
        ActivityCompat.requestPermissions(this, permission, 1)
    }
    override fun onStart() {

        super.onStart()
        if(!checkPermission())
        {
            requestPermission()
        }
        else{
            getCurrentLocation()
        }
        if(!Places.isInitialized())
        {
            Places.initialize(applicationContext, "AIzaSyD6rJ4q1UyoXB_ONxzT1-WAewnOd9lexjA")
        }

    }



}

































