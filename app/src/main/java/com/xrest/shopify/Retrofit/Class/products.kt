package com.xrest.shopify.Retrofit.Class

import java.io.Serializable


class products(

    val _id:String?=null,
    val Name:String?=null,
    val Category:String?=null,
    val Price:Int?=null,
    val Description:String?=null,
    val Images:MutableList<Image>?=null,
    val Stock:Int?=null,


): Serializable