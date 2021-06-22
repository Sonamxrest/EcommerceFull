package com.xrest.shopify

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import com.xrest.shopify.Retrofit.Class.Comment
import com.xrest.shopify.Retrofit.Repository.ProductRepository
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.*
import java.lang.Exception
import java.net.Socket
import java.util.*


class CommentFragment(var id:String,var lst:MutableList<Comment>) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_comment, container, false)
        val rv:RecyclerView = view.findViewById(R.id.rv)
        val fb:FloatingActionButton = view.findViewById(R.id.addCommnet)
        var adapter = CommentAdapter(requireContext(),lst,id)
        fb.setOnClickListener(){
            var dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.update_edittet)
            dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            var button: Button = dialog.findViewById(R.id.comment)
            var edt:TextInputEditText = dialog.findViewById(R.id.edtComment)

            button.setOnClickListener(){
                try{
                    CoroutineScope(Dispatchers.IO).launch {
                        val reposen = ProductRepository().comment(id,edt.text.toString())
                        if(reposen.success==true)
                        {
                            withContext(Main)
                            {
                                lst.add(
                                    Comment(user= RetrofitBuilder.user!!,comment = edt.text.toString())
                                )
                                adapter= CommentAdapter(requireContext(),lst,id)
                                adapter.notifyDataSetChanged()
                                rv.adapter=adapter
                                dialog.cancel()

                            }



                        }

                    }
                }
                catch (ex:Exception){

                }
            }



            dialog.setCancelable(true)
            dialog.show()
        }
        rv.layoutManager =LinearLayoutManager(requireContext())
        if(lst.size>0)
        {

            rv.adapter =adapter
        }
    return view
    }


}

class CommentAdapter(val context: Context, val comment:MutableList<Comment>,var id:String):RecyclerView.Adapter<CommentAdapter.CommentHolder>(),TextToSpeech.OnInitListener{
    var tts:TextToSpeech?=null

    class CommentHolder(view:View):RecyclerView.ViewHolder(view)
    {

        val named:TextView = view.findViewById(R.id.name)
        val comment:TextView = view.findViewById(R.id.comment)
        val profile:CircleImageView = view.findViewById(R.id.profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.comments,parent,false)
        return CommentHolder(view)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val comments = comment[position]
        tts = TextToSpeech(context,this)
        holder.comment.text = comments.comment!!
        holder.named.text = comments.user?.Name
        Glide.with(context).load("${RetrofitBuilder.BASE_URL}local/${comments.user!!.Profile}").into(holder.profile)
        holder.comment.setOnLongClickListener(){
            tts!!.speak(comments.comment!!, TextToSpeech.QUEUE_FLUSH, null,"")

            val popUp = PopupMenu(context,holder.comment)
            popUp.menuInflater.inflate(R.menu.update_delete,popUp.menu)
            popUp.show()
            popUp.setOnMenuItemClickListener {
                when(it.itemId)
                {
                    R.id.update->{
                        var dialog = Dialog(context)
                        dialog.setContentView(R.layout.update_edittet)
                        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                        var button: Button = dialog.findViewById(R.id.comment)
                        var edt:TextInputEditText = dialog.findViewById(R.id.edtComment)
                        edt.setText(comments.comment!!)
                        button.setOnClickListener(){
                            try{
                                CoroutineScope(Dispatchers.IO).launch {
                                    val reposen = ProductRepository().updateComment(id,comments._id!!,edt.text.toString())
                                    if(reposen.success==true)
                                    {

                                        withContext(Main)
                                        {
                                            comment[position].comment=edt.text.toString()
                                            holder.comment.text = edt.text.toString()
                                            dialog.cancel()

                                        }

                                    }

                                }
                            }
                            catch (ex:Exception){

                            }
                        }



                        dialog.setCancelable(true)
                        dialog.show()

                    }
                    R.id.delete -> {
                        try {
                            CoroutineScope(Dispatchers.IO).launch {
                                val response = ProductRepository().deleteComment(id, comments._id!!)
                                if (response.success == true) {

                                    withContext(Main) {
                                        comment.removeAt(position)
                                        notifyDataSetChanged()

                                        Toast.makeText(context, "One Item Deleted", Toast.LENGTH_SHORT).show()
                                    }
                                }

                            }
                        } catch (ex: Exception) {

                        }
                    }

                }
                true
            }

            true
        }

    }

    override fun getItemCount(): Int {
     return comment.size
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {

            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }

}

