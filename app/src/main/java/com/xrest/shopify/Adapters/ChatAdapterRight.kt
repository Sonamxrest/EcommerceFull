package com.xrest.shopify.Adapters

import android.widget.TextView
import com.squareup.picasso.Picasso
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Class.InnerMessage
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapterRight( var innerMessage: InnerMessage ):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    var profile:CircleImageView = viewHolder.itemView.findViewById(R.id.profile)
        var txt:TextView = viewHolder.itemView.findViewById(R.id.textMessage)

        Picasso.get().load("${RetrofitBuilder.BASE_URL}local/${innerMessage.sender!!.Profile}").into(profile)
        txt.text = innerMessage!!.message!!

    }

    override fun getLayout(): Int {
return  R.layout.chat_right
    }
}