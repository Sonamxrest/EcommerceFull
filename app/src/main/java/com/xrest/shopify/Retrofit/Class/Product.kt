package com.xrest.shopify.Retrofit.Class

import kotlinx.android.parcel.Parcelize
import java.io.Serializable


class Product(

val _id:String?=null,
val Name:String?=null,
val Category:String?=null,
val Price:Int?=null,
val Description:String?=null,
val Images:MutableList<Image>?=null,
val Stock:Int?=null,
val Comment:MutableList<Comment>?=null,
val Rating:MutableList<Rating>?=null

):Serializable

data class Comment(
        val _id:String?=null,
        val user:User?=null,
        var comment:String?=null
):Serializable

data class Rating(
        val _id:String?=null,
        val user:User?=null,
        var rating:Int?=null
):Serializable

data class Image(
        val _id:String?=null,
        var image:String?=null,

):Serializable
