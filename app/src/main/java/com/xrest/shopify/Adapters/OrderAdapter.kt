package com.xrest.shopify.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.xrest.shopify.OrderActivity
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Class.Order
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.ImageCarousel

class OrderAdapter(var context: Context,var lst:MutableList<Order>):RecyclerView.Adapter<OrderAdapter.OrderHolder>() {

class OrderHolder(view: View):RecyclerView.ViewHolder(view){


    val img:ImageCarousel = view.findViewById(R.id.imageView3)
    val total: TextView = view.findViewById(R.id.size)
    val date:TextView = view.findViewById(R.id.date)
    val address:TextView = view.findViewById(R.id.address)
    var base:CardView = view.findViewById(R.id.base)


}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.order_show,parent,false)
        return OrderHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
       var list:MutableList<CarouselItem> = mutableListOf()
        var data = lst[position]

        for(data in data.Product!!)
        {
            var image =data.product?.Images!!.get(0).image
            list.add(CarouselItem("${RetrofitBuilder.BASE_URL}local/${image}"))
        }
        holder.img.addData(list!!)
        holder.total.text = data!!.Total.toString()
        holder.date.text = data.Date!!
        holder.address.text =data.Address!!
        holder.base.setOnClickListener(){
            val intent = Intent(context , OrderActivity::class.java)
            intent.putExtra("order",data )
            context.startActivity(intent)
        }





    }

    override fun getItemCount(): Int {
       return lst.size
    }
}