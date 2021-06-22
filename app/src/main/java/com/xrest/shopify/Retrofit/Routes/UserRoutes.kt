package com.xrest.shopify.Retrofit.Routes

import com.xrest.shopify.Retrofit.Class.User
import com.xrest.shopify.Retrofit.ResponseClass.CommonClass
import com.xrest.shopify.Retrofit.ResponseClass.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserRoutes {
@FormUrlEncoded
@POST("/login")
suspend fun login(@Field("username")username:String,@Field("password")password:String):Response<LoginResponse>

@POST("/register")
suspend fun RegisterUser(@Body user:User):Response<CommonClass>

@Multipart
@PUT("/update/profile/{id}")
suspend fun updateProfile(@Path("id")id:String,@Part body:MultipartBody.Part):Response<CommonClass>


@FormUrlEncoded
@PUT("/updatePassword")
suspend fun updatePassword(@Header("Authorization")id:String,@Field("Password")password:String):Response<CommonClass>

@POST("verifyPassword/")
suspend fun verify(@Header("Authorization")id:String,@Field("Password") password: String):Response<CommonClass>


@FormUrlEncoded
@PUT("/update/user")
suspend fun updateDetails(@Header("Authorization") token:String, @Field("Username") username:String,@Field("Name") name:String):Response<CommonClass>

}