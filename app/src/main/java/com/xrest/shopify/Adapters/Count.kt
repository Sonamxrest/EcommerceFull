package com.xrest.shopify.Adapters

import android.content.Context
import android.widget.Toast

interface Count {

fun rSixe():Int{
    return 3
}

    fun toast(context: Context
              ,message:String){
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
    }
}