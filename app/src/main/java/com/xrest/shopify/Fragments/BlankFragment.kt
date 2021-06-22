package com.xrest.shopify.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.xrest.shopify.R
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class BlankFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_blank, container, false)
        var bar = requireActivity().actionBar
        var container :FrameLayout = view.findViewById(R.id.container)

//        if(NetworkConnection.checkNetworkConnection(requireContext()))
//        {
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                withContext(Main){
                    val transiction = requireFragmentManager()!!.beginTransaction()
                    transiction.apply {
                        replace(R.id.fl, LoginFragment())
                        commit()
                    }
                }


            }
//        }
//        else{
//            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
//       //    Snackbar.make(container,"Opps No Connection Detected",Snackbar.LENGTH_LONG)
//        }

        return view
    }


}