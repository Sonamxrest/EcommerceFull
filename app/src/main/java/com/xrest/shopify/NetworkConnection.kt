package com.xrest.shopify

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast

object NetworkConnection{

    fun isNetwworkConnected(context: Context):Boolean
    {
        var result = false
        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            var network = connectivityManager.activeNetwork ?: return false
            var networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            result = when{
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                else -> false
            }
        }
        else{

            connectivityManager.run {

                connectivityManager.activeNetworkInfo?.run {

                result =    when(type){

                    ConnectivityManager.TYPE_WIFI->true
                    ConnectivityManager.TYPE_MOBILE->true

                    else ->false
                    }

                }

            }


        }

       return result
    }



}