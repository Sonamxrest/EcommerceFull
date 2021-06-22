package com.xrest.shopify.Adapters

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.*
import com.anychart.core.cartesian.series.Line
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Class.Product
import com.xrest.shopify.Retrofit.Repository.ProductRepository
import com.xrest.shopify.Retrofit.Repository.UserRepo
import com.xrest.shopify.Retrofit.Routes.BookingRoutes
import com.xrest.shopify.Retrofit.Routes.UserRoutes
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import com.xrest.shopify.SingleProduct
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProductAdapter(val context:Context,val product: Product):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val image: ImageView = viewHolder.itemView.findViewById(R.id.image)
        val name: TextView = viewHolder.itemView.findViewById(R.id.name)
        val price:TextView = viewHolder.itemView.findViewById(R.id.price)
        val dprice:TextView = viewHolder.itemView.findViewById(R.id.dprice)
        val rb:RatingBar = viewHolder.itemView.findViewById(R.id.rb)
        var delete:ImageButton = viewHolder.itemView.findViewById(R.id.delete)
        var update:ImageButton = viewHolder.itemView.findViewById(R.id.update)

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
        var total =0
        for (data in product.Rating!!)
        {
            total += data.rating!!
        }
        name.text = product.Name!!
        price.text = "Rs "+product.Price!!.toString()
        dprice.text = "Rs "+(product.Price +50).toString()
        if(product.Rating.size>0)
        {
            rb.rating = (total/product.Rating.size).toFloat()

        }
        else{
            rb.rating = 3f

        }


        image.setOnClickListener(){
            try{
                val intent = Intent(context,SingleProduct::class.java)
                intent.putExtra("product",product)
                context.startActivity(intent)
            }
            catch (ex:Exception)
            {
                ex.printStackTrace()
            }


        }

        delete.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
             val response = ProductRepository().deleteProduct(product._id!!)
             if(response.success ==true)
             {
                 withContext(Main)
                 {
                     notifyChanged()
                     Toast.makeText(context, "One Product Deleted", Toast.LENGTH_SHORT).show()

                 }
             }
            }
        }


        update.setOnClickListener(){

            var dialog = Dialog(context)
            dialog.setContentView(R.layout.update_product)
            dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
            for(i in 0..product.Images.size - 1 )
            {
                var linearLayout = dialog.findViewById<LinearLayout>(R.id.root)
                var image = ImageView(context)
                var params =LinearLayout.LayoutParams(250,300)
                params.setMargins(5,5,5,5)
                image.layoutParams = params
                image.scaleType =ImageView.ScaleType.FIT_XY
                image.setImageResource(R.drawable.chair)
                Glide.with(context).load("${RetrofitBuilder.BASE_URL}local/${product!!.Images?.get(2)?.image}").into(image)
                linearLayout.addView(image)
            }
            var category=""
            var array = arrayOf("Clothes","Electronics","Furniture","Glasses")
             var name:EditText = dialog.findViewById(R.id.name)
             var price:EditText= dialog.findViewById(R.id.price)
             var stock:EditText= dialog.findViewById(R.id.stock)
             var description:EditText= dialog.findViewById(R.id.description)
             var spinner:Spinner= dialog.findViewById(R.id.spinner)
             var sumbit:Button= dialog.findViewById(R.id.submit)
            spinner.adapter =ArrayAdapter(context,android.R.layout.simple_list_item_1,array)
            spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    category = parent!!.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }


            }

            val products = Product(Name=name.text.toString(),Category = category,Price = price.text.toString().toInt(),Stock = stock.text.toString().toInt(),Description = description.text.toString())


            sumbit.setOnClickListener(){

                try {
                    CoroutineScope(Dispatchers.IO).launch {

                        var response = ProductRepository().update(products,product._id!!)
                        if(response.success==true)
                        {
                         withContext(Main)
                         {
                             Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()

                         }
                        }

                    }

                }
                catch (ex:Exception){
                    ex.printStackTrace()
                }
            }



            dialog.show()
            dialog.setCancelable(true)

        }

    }

    override fun getLayout(): Int {
        return R.layout.product_category
    }

}


