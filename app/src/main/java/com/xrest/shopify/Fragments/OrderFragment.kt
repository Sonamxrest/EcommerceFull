package com.xrest.shopify.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xrest.shopify.Adapters.OrderAdapter
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Class.Order
import com.xrest.shopify.Retrofit.Repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OrderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var lst:MutableList<Order> = mutableListOf()
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        val rv:RecyclerView = view.findViewById(R.id.rv)
        val cart = view.findViewById<TextView>(R.id.cart)
        val views:TextView = view.findViewById(R.id.viewAll)
        rv.layoutManager =LinearLayoutManager(context)

        views.setOnClickListener(){
            rv.adapter  =OrderAdapter(requireContext(),lst)
            rv.adapter!!.notifyDataSetChanged()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val repo = OrderRepository().getOrder()
            if(repo.success==true)
            {
                withContext(Main)
                {
                    if(repo.data!!.size>0)
                    {
                        lst = repo.data!!
                        cart.isVisible=false
                        var index = repo.data!!.size - 1
                        rv.adapter = OrderAdapter(requireContext(),mutableListOf<Order>(repo.data!!.get(index)))

                    }
                    else if(repo.data.size==0){
                        cart.isVisible=true
                    }
                    }
            }

        }



        return view
    }


}