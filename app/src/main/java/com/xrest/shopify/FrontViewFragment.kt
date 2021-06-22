package com.xrest.shopify

import ViewPagerAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.xrest.shopify.Adapters.ProductAdapter

import com.xrest.shopify.Fragments.BlankFragment
import com.xrest.shopify.Fragments.LoginFragment
import com.xrest.shopify.Retrofit.Class.Product
import com.xrest.shopify.Retrofit.Repository.ProductRepository
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.*
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.json.JSONObject


class FrontViewFragment : Fragment() {

    val adapter = GroupAdapter<GroupieViewHolder>()
    lateinit var pb:LottieAnimationView
    lateinit var rv:RecyclerView
    var lst:MutableList<Product> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_front_view, container, false)




        rv = view.findViewById(R.id.rv)
        pb = view.findViewById(R.id.pb)
        var viewAll :TextView = view.findViewById(R.id.viewAll)
        viewAll.setOnClickListener(){
        requireFragmentManager().beginTransaction().apply {
        replace(R.id.fragment,AllProducts())
        commit()
}
        }



        var json =JSONObject()
        json.put("message","Hello")
       // socket.send(json.toString())
        rv.layoutManager =LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        pb.isVisible=true
        CoroutineScope(Dispatchers.IO).launch {
        delay(2000)
        val response = ProductRepository().getData("")
            if(response.success==true)
            {
                delay(2000)
                withContext(Main)
                {
                lst = response.data!!
                    for(i in 1..6)
                    {
                        var linearLayout = view.findViewById<GridLayout>(R.id.gallery)
                        var image = ImageView(requireContext())
                        var params =LinearLayout.LayoutParams(515,500)
                        params.setMargins(10,10,10,10)
image.layoutParams = params
                        image.scaleType =ImageView.ScaleType.FIT_XY
                        image.setImageResource(R.drawable.chair)
                        Glide.with(requireContext()).load("${RetrofitBuilder.BASE_URL}local/${lst[i]!!.Images?.get(2)?.image}").into(image)
                        linearLayout.addView(image)


                    }

                    addToAdapter("Electronics")
                    pb.isVisible=false

                }


            }
        }

        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)
        val bottomNavigationView:BottomNavigationView = view.findViewById(R.id.bnv)
        val ImageCarousel:ImageCarousel = view.findViewById(R.id.imageCarousel)
        val list = mutableListOf<CarouselItem>()
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.clothes->{
                    addToAdapter("Clothes")
                    adapter.notifyDataSetChanged()
                }
                R.id.electronics->{
                    addToAdapter("Electronics")
                    adapter.notifyDataSetChanged()

                }
                R.id.furniture->{
                    addToAdapter("Furniture")
                    adapter.notifyDataSetChanged()

                }
                R.id.glass->{
                    addToAdapter("Glasses")
                    adapter.notifyDataSetChanged()

                }
            }
            true
        }

        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080",
                caption = ""
            )
        )
        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080",
                caption = ""
            )
        )
        ImageCarousel.addData(list)
        return view
    }



    fun addToAdapter(cat:String){
        pb.isVisible=true
        adapter.clear()
        var count =0
            if(lst.size>5)
            {
                for(i in 0..5)
                {
                    if(lst[i].Category?.toLowerCase() ==cat.toLowerCase()!!)
                    { adapter.add(ProductAdapter(requireContext(),lst[i]))
                        count +=1
                        if(count ==3)
                        {
                            break
                        }

                    }
                }
            }
            else if(lst.size<4){
                for(i in 0..lst.size-1)
                {
                    if(lst[i].Category?.toLowerCase() ==cat.toLowerCase()!!)
                    {

                        Log.d("xxxx",lst[i].Name.toString())
                        Log.d("xxxx",cat)
                        adapter.add(ProductAdapter(requireContext(),lst[i]))
                        count +=1
                        if(count ==3)
                        {
                            break
                        }

                    }

                }
            }
            pb.isVisible=false


        CoroutineScope(Dispatchers.IO).launch {
            delay(2500)
        }
        rv.adapter=adapter
        pb.isVisible=false
        adapter.notifyDataSetChanged()

    }


    override fun onResume() {
        super.onResume()

    }


}



