package com.xrest.shopify.Retrofit.Routes

import com.xrest.shopify.Retrofit.Class.Booking
import com.xrest.shopify.Retrofit.ResponseClass.BookingResponse
import com.xrest.shopify.Retrofit.ResponseClass.CommonClass
import retrofit2.Response
import retrofit2.http.*

interface BookingRoutes {

    @FormUrlEncoded
    @POST("/book/{id}")
    suspend fun  bookProduct(@Header("Authorization") token:String,@Path("id") id:String,@Field("qty") qty:String):Response<CommonClass>

    @GET("/showBooking")
    suspend fun showBooking(@Header("Authorization") token:String):Response<BookingResponse>

    @FormUrlEncoded
    @PUT("/updateBooking/{oid}")
    suspend fun updateBooking(@Path("oid")id:String,@Field("qty") qty:String):Response<CommonClass>

    @PUT("/deleteBooking/{oid}")
    suspend fun deleteBooking(@Header("Authorization") token:String,@Path("oid") id:String):Response<CommonClass>
}