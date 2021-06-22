package com.xrest.shopify.Retrofit.ResponseClass

import com.xrest.shopify.Retrofit.Class.Product

data class ProductResponse(
        val success:Boolean?=null,
        val data:MutableList<Product>?=null
) {
}