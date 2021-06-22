package com.xrest.shopify.Retrofit.Services

import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder

abstract class ApiRequest {

    suspend fun<T> handelApiRequest(call:suspend ()->Response<T>,):T{
        val response = call.invoke()
        if(response.isSuccessful)
        {
            return response.body()!!
        }
        else{
            val error = response.errorBody()!!.string()
            val message = StringBuilder()
            error.let {

              try {

                  message.append(JSONObject(it).getString("success"))

              }
              catch (ex:Exception)
              {

              }

            }
            message.append("ErrorCode "+ response.code().toString())
            throw IOException(message.toString())


        }




    }


}