package com.xrest.shopify.Retrofit.Routes

import com.google.android.gms.common.internal.service.Common
import com.xrest.shopify.Retrofit.Class.Order
import com.xrest.shopify.Retrofit.ResponseClass.CommonClass
import com.xrest.shopify.Retrofit.ResponseClass.OrderResponse
import retrofit2.Response
import retrofit2.http.*

interface orderRoutes {



    @POST("/order")
    suspend fun Oorder(@Header("Authorization") token:String, @Body order: Order):Response<CommonClass>

    @GET("/allMyOrder")
    suspend fun getAllMyOrder(@Header("Authorization")token:String):Response<OrderResponse>
    @GET("/allOrder")
    suspend fun getAllOrder():Response<OrderResponse>

    @FormUrlEncoded
    @PUT("/cancelOrder")
    suspend fun cancelOrder(@Field("id") id:String):Response<CommonClass>
    @FormUrlEncoded
    @PUT("/accceptOrder")
    suspend fun acceptOrder(@Field("id")id:String):Response<CommonClass>
    @PUT("/deliverOrder/{id}")
    suspend fun deliver(@Header("Authorization") token:String,@Path("id")id:String):Response<CommonClass>

}