package com.xrest.shopify.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.xrest.shopify.Adapters.ChatAdapterLeft
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Class.InnerMessage
import com.xrest.shopify.Retrofit.Class.User
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okio.ByteString
import org.json.JSONException
import org.json.JSONObject

class ChatFragment : Fragment() {

    lateinit var socket: WebSocket
    var adapter = GroupAdapter<GroupieViewHolder>()
    lateinit var rv:RecyclerView
    lateinit var edt:EditText
    lateinit var send: Button
    var url ="ws://10.0.2.2:3000"
    lateinit var request:Request

    var okhttp = OkHttpClient()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_chat, container, false)
        edt = view.findViewById(R.id.message)
        rv = view.findViewById(R.id.rv)
        send = view.findViewById(R.id.send)
        request = Request.Builder().url(url).build()


        rv.layoutManager =LinearLayoutManager(requireContext())
        send.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                var json = JSONObject()
                try {
                    json.put("message",edt.text.toString())
                    json.put("profile","1620962438472logo (2).png")
                    socket.send(json.toString())
                }
                catch (ex:JSONException)
                {
                }
            }

        }
        return view
    }

    override fun onResume() {
        super.onResume()
        var okHttpClient = OkHttpClient()
   // socket =  okHttpClient.newWebSocket(request,SocketListener(requireContext()))
    }

    override fun onStart() {
        super.onStart()
        socket =  okhttp.newWebSocket(request,SocketListener(requireContext()))

        socket =  okhttp.newWebSocket(request,SocketListener(requireContext()))

    }




}
class SocketListener(val context: Context):WebSocketListener(){
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Main){
                Toast.makeText(context, "Client Connected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        var json = JSONObject(text)
        var message = json.getString("message")
        var profile = json.getString("profile")
        Log.d("xxxx",message)
        var user = User(Profile = profile)
        var innerMessage =InnerMessage(sender = user,message = message)
        ChatFragment().adapter.add(ChatAdapterLeft(innerMessage))
        ChatFragment().adapter.notifyDataSetChanged()
        ChatFragment().rv.adapter = ChatFragment().adapter
        ChatFragment().rv.smoothScrollToPosition(ChatFragment().adapter.itemCount-1)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }



}

