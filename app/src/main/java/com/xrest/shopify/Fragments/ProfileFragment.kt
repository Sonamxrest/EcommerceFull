package com.xrest.shopify.Fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textclassifier.TextClassification
import android.widget.*
import androidx.core.view.isVisible
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Repository.UserRepo
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class ProfileFragment : Fragment(), View.OnClickListener {

  lateinit  var username:TextView
    lateinit var name:TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
         username = view.findViewById(R.id.username)
         name = view.findViewById(R.id.name)
        var profile:CircleImageView = view.findViewById(R.id.profile)
        var updateDetails:Button = view.findViewById(R.id.updateDetails)
        var updatePassword:Button = view.findViewById(R.id.updatePassword)

        updateDetails.setOnClickListener(this)
        updatePassword.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.updatePassword -> {
                var dialog = Dialog(requireContext())
                dialog.setContentView(R.layout.password_input)
                dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                var one: EditText = dialog.findViewById(R.id.one)
                var two: EditText = dialog.findViewById(R.id.two)
                var three: EditText = dialog.findViewById(R.id.three)
                var confirm :Button = dialog.findViewById(R.id.send)
                confirm.setOnClickListener(){
                    CoroutineScope(Dispatchers.IO).launch {

                        var confirmation = UserRepo().verifyPassword(one.text.toString())
                        if (confirmation.success == true) {

                            if (two.text.toString().equals(three.text.toString())) {
                                var response = UserRepo().updatePassword(two.text.toString())
                                if (response.success == true) {
                                    withContext(Main)
                                    {
                                        dialog.cancel()
                                        Toast.makeText(requireContext(), "Password Changed", Toast.LENGTH_SHORT).show()
                                    }
                                } else {

                                    withContext(Main) {
                                        Toast.makeText(requireContext(), "Password Did not Matched", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                        } else {


                            withContext(Main) {
                                dialog.cancel()
                                Toast.makeText(requireContext(), "Old Password Did Not Matched", Toast.LENGTH_SHORT).show()
                            }
                        }


                    }

                }




                dialog.show()
                dialog.setCancelable(true)
            }
            R.id.updateDetails -> {
                var dialog = Dialog(requireContext())
                dialog.setContentView(R.layout.password_input)
                dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                var one: EditText = dialog.findViewById(R.id.one)
                var two: EditText = dialog.findViewById(R.id.two)
                var three: EditText = dialog.findViewById(R.id.three)
                var confirm: Button = dialog.findViewById(R.id.send)
                three.isVisible = false
                one.setText(RetrofitBuilder.user!!.Username)
                two.setText(RetrofitBuilder.user!!.Name)
                confirm.setOnClickListener() {
                    CoroutineScope(Dispatchers.IO).launch {

                        var response = UserRepo().update(one.text.toString(), two.text.toString())
                        if (response.success == true) {
                            withContext(Main) {
                                Toast.makeText(requireContext(), "Details Updated", Toast.LENGTH_SHORT).show()
                                username.text = one.text.toString()
                                name.text = two.text.toString()
                            }
                        }

                    }
                }


            }
        }
    }


}