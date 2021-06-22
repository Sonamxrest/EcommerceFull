package com.xrest.shopify.Retrofit.Services

import com.google.android.gms.maps.model.LatLng
import com.xrest.shopify.Retrofit.Class.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder
{
    var token:String?=null
    val BASE_URL ="http://192.168.0.109:3000/"
    var user: User?=null
    var lat:LatLng?=null
    val okHttpClient = OkHttpClient.Builder()
    val retrofitBuilder = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient.build())
val retrofit = retrofitBuilder.build()

     fun<T> buildServices(services:Class<T>):T{
        return retrofit.create(services)
    }


}