package com.xrest.shopify.Retrofit.ResponseClass

import com.xrest.shopify.Retrofit.Class.User

data class LoginResponse(
    val success:Boolean?=null,
    val token:String?=null,
val data: User?=null



) {
}