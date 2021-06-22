package com.xrest.shopify.Retrofit.Repository

import com.xrest.shopify.Retrofit.Class.User
import com.xrest.shopify.Retrofit.ResponseClass.CommonClass
import com.xrest.shopify.Retrofit.ResponseClass.LoginResponse
import com.xrest.shopify.Retrofit.Routes.UserRoutes
import com.xrest.shopify.Retrofit.Services.ApiRequest
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import okhttp3.MultipartBody
import okhttp3.Response

class UserRepo:ApiRequest() {

    var api = RetrofitBuilder.buildServices(UserRoutes::class.java)


    suspend fun login(username:String,password:String):LoginResponse{
        return handelApiRequest {
            api.login(username,password)
        }
    }
    suspend fun register(user: User):CommonClass{
        return handelApiRequest {
            api.RegisterUser(user)
        }
    }
    suspend fun uploadProfile(id:String,body:MultipartBody.Part):CommonClass
    {
        return handelApiRequest {
            api.updateProfile(id,body)
        }
    }

    suspend fun update(username:String,name:String):CommonClass{

        return handelApiRequest {
            api.updateDetails(RetrofitBuilder.token!!,name,username)
        }

    }

    suspend fun verifyPassword(password:String):CommonClass{
        return  handelApiRequest {
            api.verify(RetrofitBuilder.token!!,password)
        }
    }
    suspend fun updatePassword(password:String):CommonClass{
        return  handelApiRequest {
            api.updatePassword(RetrofitBuilder.token!!,password)
        }
    }


}