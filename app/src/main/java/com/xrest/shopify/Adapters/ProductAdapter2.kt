package com.xrest.shopify.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Class.Product
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import com.xrest.shopify.SingleProduct
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ProductAdapter2(val context:Context, val product: Product):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val image: ImageView = viewHolder.itemView.findViewById(R.id.image)
        val name: TextView = viewHolder.itemView.findViewById(R.id.name)
        val price:TextView = viewHolder.itemView.findViewById(R.id.price)
        val transform = RoundedTransformationBuilder().borderColor(Color.BLACK)
                .borderWidthDp(3f)
                .cornerRadiusDp(30f)
                .oval(false).build()
        if(product.Images?.size!!>0)
        {
         //   Picasso.get().load("${RetrofitBuilder.BASE_URL}local/${product.Images?.get(0)?.image}}").into(image)
Glide.with(context).load("${RetrofitBuilder.BASE_URL}local/${product.Images?.get(0)?.image}").into(image)
        }
        else{

           // Picasso.get().load("${RetrofitBuilder.BASE_URL}local/no-image.jpg}").fit().transform(transform).into(image)
Glide.with(context).load("${RetrofitBuilder.BASE_URL}local/no-image.jpg").into(image)

        }
        name.text = product.Name!!
        price.text = "Rs "+product.Price!!.toString()

        image.setOnClickListener(){
            val intent = Intent(context,SingleProduct::class.java)
            intent.putExtra("product",product)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.pv
    }
}


