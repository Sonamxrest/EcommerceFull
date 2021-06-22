package com.xrest.shopify.Retrofit.Routes

import com.xrest.shopify.Retrofit.Class.Product
import com.xrest.shopify.Retrofit.ResponseClass.CommonClass
import com.xrest.shopify.Retrofit.ResponseClass.ProductResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ProductRoutes {

    @FormUrlEncoded
    @POST("/showAll")
    suspend fun getProduct(@Field("category") cate: String): Response<ProductResponse>

    @POST("/insertProduct")
    suspend fun addProduct(@Body product: Product): Response<CommonClass>


    @Multipart
    @PUT("/upload/image/{id}")
    suspend fun uploadImages(
        @Path("id") id: String,
        @Part data: MutableList<MultipartBody.Part>
    ): Response<CommonClass>

    @FormUrlEncoded
    @PUT("/comment/{id}")
    suspend fun comment(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Field("comment") comment:String
    ): Response<CommonClass>

    @FormUrlEncoded
    @PUT("/updateComment/{pid}/{oid}")
    suspend fun updateComment(
        @Header("Authorization") token: String,
        @Path("pid") id: String,
        @Path("oid") oid:String,
        @Field("comment") comment:String
    ): Response<CommonClass>

    @PUT("/deleteComment/{pid}/{oid}")
    suspend fun deleteComment(
        @Header("Authorization") token: String,
        @Path("pid") id: String,
        @Path("oid") oid:String
    ): Response<CommonClass>
    @FormUrlEncoded
    @PUT("/rate/{id}")
    suspend fun rate(@Header("Authorization") token:String,@Path("id") id:String,@Field("Rating") rate:String):Response<CommonClass>
@DELETE("/deleteProduct/{id}")
suspend fun delete(@Path("id") id:String):Response<CommonClass>

@PUT("/updateProduct/{id}")
suspend fun updateProducts(@Body product:Product,@Path("id")id:String):Response<CommonClass>

}