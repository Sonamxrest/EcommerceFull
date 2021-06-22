package com.xrest.shopify.Retrofit.Repository

import com.xrest.shopify.Retrofit.Class.Product
import com.xrest.shopify.Retrofit.ResponseClass.CommonClass
import com.xrest.shopify.Retrofit.ResponseClass.ProductResponse
import com.xrest.shopify.Retrofit.Routes.ProductRoutes
import com.xrest.shopify.Retrofit.Services.ApiRequest
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import okhttp3.MultipartBody

class ProductRepository:ApiRequest() {
    val api = RetrofitBuilder.buildServices(ProductRoutes::class.java)


    suspend fun getData(c:String):ProductResponse{
        return handelApiRequest {
            api.getProduct(c)
        }
    }
    suspend fun addProduct(p:Product):CommonClass{
return handelApiRequest {
    api.addProduct(p)
}
    }
    suspend fun uploadImages(id:String,body:MutableList<MultipartBody.Part>):CommonClass{
return handelApiRequest {
    api.uploadImages(id,body)
}
    }
    suspend fun rate(id:String,rate:String):CommonClass{
        return handelApiRequest {
            api.rate(RetrofitBuilder.token!!,id,rate)
        }
    }

    suspend fun comment(id:String,comment:String):CommonClass{
        return handelApiRequest {
            api.comment(RetrofitBuilder.token!!,id,comment)
        }
    }

    suspend fun updateComment(pid:String,oid:String,comment:String):CommonClass{
        return handelApiRequest {
            api.updateComment(RetrofitBuilder.token!!,pid,oid,comment)
        }
    }

    suspend fun deleteComment(pid:String,oid:String):CommonClass
    {
        return handelApiRequest {
            api.deleteComment(RetrofitBuilder.token!!,pid,oid)
        }
    }

    suspend fun deleteProduct(id:String):CommonClass{
        return handelApiRequest {
            api.delete(id)
        }
    }

    suspend fun update(product:Product,id:String):CommonClass{
        return handelApiRequest {
            api.updateProducts(product,id)
        }
    }

}