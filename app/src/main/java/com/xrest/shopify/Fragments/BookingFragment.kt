package com.xrest.shopify.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xrest.shopify.Adapters.BookingAdapter
import com.xrest.shopify.MapsActivity
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Repository.BookingRepo
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookingFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_booking, container, false)
        val rv:RecyclerView = view.findViewById(R.id.rv)
        var order: LinearLayout = view.findViewById(R.id.order)
        var price:TextView = view.findViewById(R.id.price)
        var book:TextView = view.findViewById(R.id.cart)

        order.setOnClickListener(){
            var intent = Intent(requireContext(),MapsActivity::class.java)
            startActivity(intent)

        }
        rv.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            val repo = BookingRepo().getBookings()
            if(repo.success==true)
            {

                    withContext(Main)
                    {

                        if (repo.data==null)
                        {
                            book.isVisible = true
                        }
                         else
                        {
                            book.isVisible =false
                            var adapter = BookingAdapter(requireContext(),repo.data!!.Product!!)
                            rv.adapter =adapter
                        }
                        price.text = repo.total
                }
            }

        }





        return view
    }


}