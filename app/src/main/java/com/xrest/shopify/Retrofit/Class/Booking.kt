package com.xrest.shopify.Retrofit.Class

import java.io.Serializable

data class Booking(
        val _id:String?=null,
        val User:String?=null,
        val Product:MutableList<BP>?=null,
        val date:String?=null
)
data class BP(
        val _id:String?=null,
        var qty:Int?=null,
        val product: products?=null
):Serializable