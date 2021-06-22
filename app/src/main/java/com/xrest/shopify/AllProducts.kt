package com.xrest.shopify

import android.content.Context
import android.hardware.*
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xrest.shopify.Adapters.Count
import com.xrest.shopify.Adapters.ProductAdapter
import com.xrest.shopify.Adapters.ProductAdapter2
import com.xrest.shopify.Retrofit.Class.Product
import com.xrest.shopify.Retrofit.Repository.ProductRepository
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.*


class AllProducts : Fragment(), Count {
    var lst:MutableList<Product> = mutableListOf()
    val adapter = GroupAdapter<GroupieViewHolder>()
    lateinit var rv:RecyclerView
    lateinit var  swipe:SwipeRefreshLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_all_products, container, false)
         rv = view.findViewById(R.id.rv)
        val search:EditText = view.findViewById(R.id.search)
        val seekbar:SeekBar = view.findViewById(R.id.seekBar)
        val editText:TextView = view.findViewById(R.id.price)
        swipe = view.findViewById(R.id.swipe)
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
      //  rv.layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
        seekbar.max=10000
        seekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                editText.text = seekbar.progress.toString()!!

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                searchData(search.text.toString(),seekBar!!.progress)
            }
        })


        swipe.setOnRefreshListener(){
            adapter.clear()
            for(data in rSixe()..lst.size-1)
            {
                adapter.add(ProductAdapter2(requireContext(),lst[data]))
            }
            Handler().postDelayed(Runnable {
                swipe.isRefreshing = false
            },4000)
            rv.adapter =adapter
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            val response = ProductRepository().getData("")
            if(response.success==true)
            {

                withContext(Dispatchers.Main)
                {
                     lst = response.data!!
                    for(data in 0..rSixe())
                    {
                        adapter.add(ProductAdapter2(requireContext(),lst[data]))
                    }

                    rv.adapter =adapter
                }


            }



        }

        search.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
               searchData(search.text.toString(),0)
            }
        })




        return view
    }

    private fun searchData(toString: String, i: Int) {
        adapter.clear()
        for(data in lst)
        {
            if( data.Name!!.toLowerCase().contains(toString.toLowerCase()) && data.Price!!>i)
            {
                Log.d("name",data.Name!!)
                adapter.add(ProductAdapter2(requireContext(),data))
            }
        }
        rv.adapter =adapter
        adapter.notifyDataSetChanged()
    }



}