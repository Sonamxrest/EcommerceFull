package com.xrest.shopify.Retrofit.Class

import java.io.Serializable

data class User(
    val _id:String?=null,
    val Name:String?=null,
    val Username:String?=null,
    val Password:String?=null,
    val Profile:String?=null,
    val UserType:String?=null

):Serializable {
}