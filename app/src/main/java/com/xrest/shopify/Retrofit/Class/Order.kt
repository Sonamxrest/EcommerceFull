package com.xrest.shopify.Retrofit.Class

import java.io.Serializable

data class Order(
        var  _id:String?=null,
        var User:User?=null,
        var Product:MutableList<BP>?=null,
        var PhoneNumber:String?=null,
        var Longtitude:String?=null,
        var Latitude:String?=null,
        var Total:Int?=null,
        var orderStatus:Boolean?=null,
        var DeliveredStatus:Boolean?=null,
        var EmployeeId:User?=null,
        var Address:String?=null,
        var Date:String?=null
):Serializable
