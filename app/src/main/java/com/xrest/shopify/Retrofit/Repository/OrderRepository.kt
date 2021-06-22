package com.xrest.shopify.Retrofit.Repository

import com.xrest.shopify.Retrofit.Class.Order
import com.xrest.shopify.Retrofit.ResponseClass.CommonClass
import com.xrest.shopify.Retrofit.ResponseClass.OrderResponse
import com.xrest.shopify.Retrofit.Routes.orderRoutes
import com.xrest.shopify.Retrofit.Services.ApiRequest
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder

class OrderRepository:ApiRequest(){

    var api = RetrofitBuilder.buildServices(orderRoutes::class.java)


    suspend fun placeOrder(order: Order):CommonClass{

        return handelApiRequest {
            api.Oorder(RetrofitBuilder.token!!,order)
        }
    }
    suspend fun getOrder():OrderResponse{
        return handelApiRequest {
            api.getAllMyOrder(RetrofitBuilder.token!!)
        }
    }
    suspend fun getOrders():OrderResponse{
        return handelApiRequest {
            api.getAllOrder()
        }
    }

    suspend fun acceptOrder(id:String):CommonClass{
        return handelApiRequest {
            api.acceptOrder(id)
        }
    }
    suspend fun cancelOrder(id:String):CommonClass{
        return handelApiRequest {
            api.cancelOrder(id)
        }
    }
    suspend fun deliver(id:String):CommonClass{
        return handelApiRequest {
            api.deliver(RetrofitBuilder.token!!,id)
        }
    }
}