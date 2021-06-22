package com.xrest.shopify.Adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Class.BP
import com.xrest.shopify.Retrofit.Class.Booking
import com.xrest.shopify.Retrofit.Repository.BookingRepo
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookingAdapter(val context: Context, val bookings: MutableList<BP>):RecyclerView.Adapter<BookingAdapter.BookHOlder>() {

    class BookHOlder(view: View):RecyclerView.ViewHolder(view){
        var name: TextView = view.findViewById(R.id.name)
        var price: TextView = view.findViewById(R.id.price)
        var total: TextView = view.findViewById(R.id.total)
        var qty: TextView =view.findViewById(R.id.qty)
        var update: ImageButton = view.findViewById(R.id.update)
        var delete: ImageButton = view.findViewById(R.id.delete)
        var image: ImageView = view.findViewById(R.id.image)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHOlder {
        val view = LayoutInflater.from(context).inflate(R.layout.show_booking,parent,false)
        return BookHOlder(view)
    }

    override fun onBindViewHolder(holder: BookHOlder, position: Int) {
        val booking = bookings[position]
        holder.name.text = booking.product!!.Name
        holder.price.text = booking.product!!.Price.toString()
        holder.total.text =(booking.product!!.Price!! * booking.qty!!).toString()
        holder.qty.text = booking.qty.toString()
        Glide.with(context).load("${RetrofitBuilder.BASE_URL}local/${booking!!.product!!.Images!!.get(0).image}").into(holder.image)
        holder.update.setOnClickListener(){

            var dialog = Dialog(context)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.update_qty)
            dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            var plus:ImageButton = dialog.findViewById(R.id.plus)
            var minus:ImageButton = dialog.findViewById(R.id.minus)
            var cancel:ImageButton = dialog.findViewById(R.id.cancel)
            var done: Button = dialog.findViewById(R.id.done)
            var edt: EditText = dialog.findViewById(R.id.qty)
            edt.setText(booking.qty!!.toString())
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
                val qtys = edt.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    val repo = BookingRepo().updateBooking(booking._id!!,qtys)
                    if(repo.success==true)
                    {
                        withContext(Dispatchers.Main)
                        {

                            bookings[position].qty = qtys.toInt()
                            holder.total.text =(booking.product!!.Price!! + qtys.toInt()).toString()
                            dialog.cancel()
                            Toast.makeText(context, "One Item Updated", Toast.LENGTH_SHORT).show()


                        }
                    }



                }
            }




            dialog.show()



        }

       holder.delete.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                val repo = BookingRepo().deleteBooking(booking._id!!)
                if(repo.success==true)
                {
                    withContext(Dispatchers.Main)
                    {


                        bookings.removeAt(position)
                        notifyDataSetChanged()
                        Toast.makeText(context, "One Item Delete", Toast.LENGTH_SHORT).show()


                    }
                }



            }
        }
    }

    override fun getItemCount(): Int {
      return bookings.size
    }
}