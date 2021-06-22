package com.xrest.shopify

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.directions.route.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import java.util.*


class DirectionActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener,GoogleApiClient.OnConnectionFailedListener,
    RoutingListener, TextToSpeech.OnInitListener {

    private lateinit var mMap: GoogleMap
    var origin:LatLng?=null
    //var destination:LatLng?=null

    var count = 0
    private var polylines: MutableList<Polyline>? = null
    var tts:TextToSpeech?=null

    var latitude:Double?=null
        var longtitude:Double?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direction)
        var places :FloatingActionButton = findViewById(R.id.places)
        tts = TextToSpeech(this,this)
     places.setOnClickListener(){
          if(!Places.isInitialized())
          {
              Places.initialize(this,getString(R.string.google_maps_key))
          }
      //   fused = LocationServices.getFusedLocationProviderClient(this)

         latitude = intent.getStringExtra("latitude")!!.toDouble()
          longtitude = intent.getStringExtra("longtitude")!!.toDouble()

         var lst :MutableList<Place.Field> = mutableListOf()
         lst.add(Place.Field.LAT_LNG)
         lst.add(Place.Field.OPENING_HOURS)
         lst.add(Place.Field.ADDRESS)
         lst.add(Place.Field.VIEWPORT)

         var intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,lst).setCountries(
             arrayListOf("Nepal","India")).build(this)
         startActivityForResult(intent,200)
        }


if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
{
    ActivityCompat.requestPermissions(
        this, arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ), 1
    )
}
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    if(resultCode == RESULT_OK && requestCode == 200)
    {
        var place = Autocomplete.getPlaceFromIntent(data!!)
        var latlng = place.latLng
        mMap.addMarker(MarkerOptions().position(latlng).snippet("${place.address}").draggable(false).title("${place.viewport}"))
        tts!!.speak(place.address,TextToSpeech.QUEUE_FLUSH,null,"")

    }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        mMap.isMyLocationEnabled = true
        mMap.setOnMyLocationChangeListener { location->
            mMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    ).zoom(14f).build()
                )
            )
            origin = LatLng(location.latitude, location.longitude)
            //destination =LatLng(latitude!!, longtitude!!)
            Log.d("llll",origin.toString())
           // Log.d("llll",destination.toString())
            findRoutes(origin!!,RetrofitBuilder.lat!!)

        }




//
      //  mMap.setOnMapClickListener(this)
    }

    override fun onMapClick(p0: LatLng) {
        var markers:Marker?=null
        if(count ==0)
        {
            count = count +1
            markers = mMap.addMarker(
                MarkerOptions().title("Destination").position(p0).icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                    )
                ).draggable(true)
            )
           // destination = LatLng(p0.latitude, p0.longitude)
        }
        else{
            mMap.clear()
            markers = mMap.addMarker(
                MarkerOptions().title("Destination").position(p0).icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                    )
                ).draggable(true)
            )
           // destination = LatLng(p0.latitude, p0.longitude)
        }
        var results:FloatArray= FloatArray(10)
//        Location.distanceBetween(
//            origin!!.latitude,
//            origin!!.longitude,
//            destination!!.latitude,
//            destination!!.longitude,
//            results
//        )

        val m = results[0].toInt()
        val strM = m.toString() + ""
        var kkm:Float =0f
        if (strM.length > 6)
        {
             kkm = m * 1.0f / (1000 * 1000)
            kkm = Math.round(kkm * 100) * 1.0f / 10

        }
        else if (strM.length > 3)
        {
             kkm = m * 1.0f / 1000
            kkm = Math.round(kkm * 100) * 1.0f / 10
        }
        Toast.makeText(this, "${kkm}", Toast.LENGTH_SHORT).show()
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(p0: Marker) {
            }

            override fun onMarkerDrag(p0: Marker) {
            }

            override fun onMarkerDragEnd(p0: Marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(p0.position.latitude, p0.position.longitude), 16f))
            }

        })
        
//        findRoutes(origin!!, destination!!)


    }

    private fun findRoutes(origin: LatLng, destination: LatLng) {


        if(origin ==null || destination ==null)
        {
            Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show()
        }
        else{
            var routes = Routing.Builder().
            travelMode(AbstractRouting.TravelMode.DRIVING)
                .key(getString(R.string.google_maps_key))
                .withListener(this)
                .waypoints(origin, destination)
                .build()
            routes.execute()
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
//        findRoutes(origin!!,destination!!)

    }

    override fun onRoutingFailure(p0: RouteException?) {

    }

    override fun onRoutingStart() {

    }

    override fun onRoutingSuccess(p0: ArrayList<Route>?, p1: Int) {
        if (polylines != null) {
            polylines!!.clear()
        }
        var polyOptions = PolylineOptions()
        var polylineStartLatLng: LatLng? = null
        var polylineEndLatLng: LatLng? = null
        polylines = mutableListOf()
     for( i in 0..p0!!.size -1 ) {
         if (i == p1)
         {
             polyOptions.color(resources.getColor(R.color.black))
             polyOptions.width(7f)
             polyOptions.addAll(p0.get(p1).points)
             var pLine = mMap.addPolyline((polyOptions))
             polylineStartLatLng =pLine.points.get(0)
             var k:Int = pLine.points.size
             polylineEndLatLng = pLine.points.get(k - 1)
             polylines!!.add(pLine)

         }
         else{


         }


     }
        val startMarker = MarkerOptions()
        startMarker.position(polylineStartLatLng)
        startMarker.title("My Location")
        mMap.addMarker(startMarker)


        val endMarker = MarkerOptions()
        endMarker.position(polylineEndLatLng)
        endMarker.title("Destination")
        mMap.addMarker(endMarker)
    }

    override fun onRoutingCancelled() {
//findRoutes(origin!!,destination!!)
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS)
        {
            var result = tts!!.setLanguage(Locale.UK)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
            }
        }
    }
}