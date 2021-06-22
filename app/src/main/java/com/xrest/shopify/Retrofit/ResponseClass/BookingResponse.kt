package com.xrest.shopify.Retrofit.ResponseClass

import com.xrest.shopify.Retrofit.Class.Booking

data class BookingResponse(
    val success:Boolean?=null,
    val data: Booking?=null,
    val total: String?=null
) {

}