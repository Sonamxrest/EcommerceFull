package com.xrest.shopify.Retrofit.Repository

import com.xrest.shopify.Retrofit.Class.Booking
import com.xrest.shopify.Retrofit.ResponseClass.BookingResponse
import com.xrest.shopify.Retrofit.ResponseClass.CommonClass
import com.xrest.shopify.Retrofit.Routes.BookingRoutes
import com.xrest.shopify.Retrofit.Services.ApiRequest
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder

class BookingRepo:ApiRequest() {


    val api = RetrofitBuilder.buildServices(BookingRoutes::class.java)
    suspend fun book(id:String,qty:String): CommonClass {
        return handelApiRequest{
            api.bookProduct(RetrofitBuilder.token!!,id,qty)
        }
    }
    suspend fun getBookings(): BookingResponse {
        return handelApiRequest {
            api.showBooking(RetrofitBuilder.token!!)
        }

    }

    suspend fun updateBooking(id:String,qty:String):CommonClass{
        return handelApiRequest {
            api.updateBooking(id,qty)
        }
    }
    suspend fun deleteBooking(id:String):CommonClass{

        return handelApiRequest {
            api.deleteBooking(RetrofitBuilder.token!!,id)
        }
    }


}