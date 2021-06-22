
package com.xrest.shopify

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.xrest.shopify.Adapters.PagerAdapter
import com.xrest.shopify.Retrofit.Class.Product
import com.xrest.shopify.Retrofit.Repository.BookingRepo
import com.xrest.shopify.Retrofit.Repository.ProductRepository
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_single_product.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.ImageCarousel


class SingleProduct : AppCompatActivity() {

    var icon:MutableList<Int> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_product)
        var tabLayout:TabLayout = findViewById(R.id.tabs)
        var vp2:ViewPager2 = findViewById(R.id.vp2)
        var lst: MutableList<CarouselItem> = mutableListOf()
        var cart:LinearLayout = findViewById(R.id.addToCart)
        val carousel = findViewById<ImageCarousel>(R.id.carousel)
        val product = intent.getSerializableExtra("product") as Product
        icon.add(R.drawable.ic_baseline_exit_to_app_24)
        icon.add(R.drawable.profile)
        var fragment =mutableListOf(Description(product.Description!!),CommentFragment(product._id!!,product.Comment!!))
        vp2.adapter =PagerAdapter(fragment,supportFragmentManager,lifecycle)
        TabLayoutMediator(tabLayout,vp2){tabs,position->
            tabs.setIcon(icon[position])
        }.attach()

        var ratingBar: RatingBar = findViewById(R.id.ratingBar)

        var name: TextView = findViewById(R.id.name)
        var price: TextView = findViewById(R.id.price)
        var rating: TextView = findViewById(R.id.rating)

        for (data in product?.Images!!) {
            lst.add(CarouselItem("${RetrofitBuilder.BASE_URL}local/${data.image}", ""))
        }
        if (product.Rating!!.size > 0)
        {
            var total =0
            for (data in product.Rating!!)
            {
                total += data.rating!!
            }
            val rate = (total/product.Rating!!.size).toFloat()
            ratingBar.rating = rate
            rating.text =(total/product.Rating!!.size).toString()
        }
        else{
            ratingBar.rating = 3f
        }

        name.text = product.Name!!
        price.text ="Rs ${product.Price}"
        carousel.addData(lst)
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
        this.rating.text =rating.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val response = ProductRepository().rate(product._id,rating.toString())
                if(response.success==true)
                {
                    withContext(Main)
                    {
                        Toast.makeText(this@SingleProduct, "Rated Successfullt", Toast.LENGTH_SHORT).show()
                    }
                }
            }
     }

        cart.setOnClickListener(){

        var dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.update_qty)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

            var plus:ImageButton = dialog.findViewById(R.id.plus)
            var minus:ImageButton = dialog.findViewById(R.id.minus)
            var cancel:ImageButton = dialog.findViewById(R.id.cancel)
            var done:Button = dialog.findViewById(R.id.done)
            var edt:EditText = dialog.findViewById(R.id.qty)
            plus.setOnClickListener(){
                edt.setText((edt.text.toString().toInt()+1).toString())
            }
            minus.setOnClickListener(){

                if(edt.text.toString() =="1")
                {

                }
                else{
                    edt.setText((edt.text.toString().toInt()-1).toString())
                }
            }
            cancel.setOnClickListener(){
                dialog.cancel()
            }
            done.setOnClickListener(){
                val qty = edt.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    val repo = BookingRepo().book(product._id!!,qty)
                    if(repo.success==true)
                    {
                        withContext(Main)
                        {dialog.cancel()
                            Toast.makeText(this@SingleProduct, "One Item Added To Cart", Toast.LENGTH_SHORT).show()

                        }
                    }



                }
            }



            dialog.show()

        }

    }
}