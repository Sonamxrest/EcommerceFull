package com.xrest.shopify.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.core.cartesian.series.Column
import com.anychart.core.cartesian.series.Line
import com.anychart.enums.*
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.xrest.shopify.Adapters.OrderAdapter
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Class.Order
import com.xrest.shopify.Retrofit.Repository.OrderRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.util.*


class AdminDaily : Fragment(), SensorListener {
lateinit  var rv:RecyclerView

var lst:MutableList<Order> = mutableListOf()
    var adapter:OrderAdapter?=null
    var electronics  = 0
    var furniture =0
    var clothes = 0
    var glasses =0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_daily, container, false)
        rv= view.findViewById(R.id.rv)


        var manager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
         manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE).also {
            manager.registerListener(this,it.type,SensorManager.SENSOR_DELAY_NORMAL)
        }
        manager.getDefaultSensor(Sensor.TYPE_PROXIMITY).also {
            manager.registerListener(this,it.type,SensorManager.SENSOR_DELAY_FASTEST)
        }



        var bar: CardView = view.findViewById(R.id.bar)
        var pie:CardView = view.findViewById(R.id.pie)
        bar.setOnClickListener(){
            viewGraph()
        }
        pie.setOnClickListener(){
            pieChart()
        }

        var shimmer:ShimmerFrameLayout = view.findViewById(R.id.shimmer)
        var img:ImageButton = view.findViewById(R.id.img)
        rv.layoutManager =LinearLayoutManager(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            val response = OrderRepository().getOrders()
            delay(3000)
            if(response.success==true)
            {
                withContext(Main){
                    shimmer.stopShimmer()
                    shimmer.isVisible =false
                    lst = response.data!!
                    adapter = OrderAdapter(requireContext(), lst)
                    rv.adapter = OrderAdapter(requireContext(), lst)
                    rv.smoothScrollToPosition(1)

                }
            }
        }

        img.setOnClickListener(){
            var date =""
            var dialog =Dialog(requireContext())
            dialog.setContentView(R.layout.calendar_dialog)
            dialog.window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            var timeStamp =Calendar.getInstance()
            var cv:DatePicker = dialog.findViewById(R.id.dp)
            cv.init(
                timeStamp.get(Calendar.YEAR), timeStamp.get(Calendar.MONTH), timeStamp.get(
                    Calendar.DAY_OF_MONTH
                )
            ){ view, year, month, day ->
                date ="${year}/${month+1}/${day}"
                Log.d("date", date)
                dialog.cancel()
                adapter!!.notifyDataSetChanged()
                var newList = sortByDate(date)
                adapter = OrderAdapter(requireContext(), newList)
                rv.adapter =adapter

            }
            dialog.show()
              dialog.setCancelable(true)


        }
        return view
    }

    private fun viewChart(lst: MutableList<Order>) {

        Log.d("orders", lst.toString())


        for(i  in 0 until lst.size)
        {
            for(j in 0 until lst[i].Product!!.size)
            {

                when {
                    lst[i]!!.Product!!.get(j).product!!.Category!!.equals(
                        "Electronics",
                        ignoreCase = true
                    ) -> {
                        electronics += (lst[i]!!.Product?.get(j)!!.product!!.Price!! * lst[i]!!.Product?.get(
                            j
                        )!!.qty!!)
                        Log.d(
                            "category", "Electronics ${
                                lst[i]!!.Product?.get(j)!!.product!!.Price!! * lst[i]!!.Product?.get(
                                    j
                                )!!.qty!!
                            }  " + electronics.toString()
                        )
                    }
                    lst[i]!!.Product!!.get(j).product!!.Category!!.equals(
                        "Furniture",
                        ignoreCase = true
                    ) -> {
                        furniture += (lst[i]!!.Product?.get(j)!!.product!!.Price!! * lst[i]!!.Product?.get(
                            j
                        )!!.qty!!)
                        Log.d(
                            "category", "Furniture ${
                                lst[i]!!.Product?.get(j)!!.product!!.Price!! * lst[i]!!.Product?.get(
                                    j
                                )!!.qty!!
                            }  " + furniture.toString()
                        )


                    }
                    lst[i]!!.Product!!.get(j).product!!.Category!!.equals(
                        "Glasses",
                        ignoreCase = true
                    ) -> {
                        glasses += (lst[i]!!.Product?.get(j)!!.product!!.Price!! * lst[i]!!.Product?.get(
                            j
                        )!!.qty!!)
                        Log.d(
                            "category", "Glasses ${
                                lst[i]!!.Product?.get(j)!!.product!!.Price!! * lst[i]!!.Product?.get(
                                    j
                                )!!.qty!!
                            }  " + glasses.toString()
                        )


                    }
                    lst[i]!!.Product!!.get(j).product!!.Category!!.equals(
                        "Clothes",
                        ignoreCase = true
                    ) -> {
                        clothes += (lst[i]!!.Product?.get(j)!!.product!!.Price!! * lst[i]!!.Product?.get(
                            j
                        )!!.qty!!)
                        Log.d(
                            "category", "Clothes ${
                                lst[i]!!.Product?.get(j)!!.product!!.Price!! * lst[i]!!.Product?.get(
                                    j
                                )!!.qty!!
                            }  " + clothes.toString()
                        )


                    }
                }

            }
        }

    }
    fun viewGraph(){
        viewChart(lst)
        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.any_chart)
        var chart:AnyChartView = dialog.findViewById(R.id.anyChart)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
        var cartesian = AnyChart.column()
        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("Furniture", furniture))
        data.add(ValueDataEntry("Clothes", clothes))
        data.add(ValueDataEntry("Glasses", glasses))
        data.add(ValueDataEntry("Electronics", electronics))
        val column: Column = cartesian.column(data)
        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("\${%Value}{groupsSeparator: }")
        cartesian.animation(true)
        cartesian.title("Most Sold Category")
        cartesian.yScale().minimum(0.0)
        cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)
        cartesian.xAxis(0).title("Product")
        cartesian.yAxis(0).title("Revenue")
        chart.setChart(cartesian)
        dialog.setCancelable(true)
        dialog.show()
    }

    fun sortByDate(date: String):MutableList<Order>{

        var list:MutableList<Order> = mutableListOf()

        for(i in 0..lst.size -1 )
        {
            if(lst[i].Date!! == date)
            {
                list.add(lst[i])
                Log.d("date", lst[i].Date!!)
                Log.d("date", date)
            }
        }
        return list

    }

    fun pieChart(){
        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.any_chart)
        var chart:AnyChartView = dialog.findViewById(R.id.anyChart)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)

        var pie = AnyChart.pie()
        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("Apples", 6371664))
        data.add(ValueDataEntry("Pears", 789622))
        data.add(ValueDataEntry("Bananas", 7216301))
        data.add(ValueDataEntry("Grapes", 1486621))
        data.add(ValueDataEntry("Oranges", 1200000))

        pie.data(data)

        pie.title("Fruits imported in 2015 (in kg)")

        pie.labels().position("outside")

        pie.legend().title().enabled(true)
        pie.legend().title()
            .text("Retail channels")
            .padding(0.0, 0.0, 10.0, 0.0)

        pie.legend()
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(Align.CENTER)

        chart.setChart(pie)
        dialog.setCancelable(true)
        dialog.show()
    }

    override fun onSensorChanged(sensor: Int, values: FloatArray?) {
if(sensor == Sensor.TYPE_PROXIMITY)
{
    var data = values?.get(0)
    Toast.makeText(requireContext(), "${data}", Toast.LENGTH_SHORT).show()

}
        else if(sensor ==Sensor.TYPE_GYROSCOPE){
            var data =values!![0]
    Toast.makeText(requireContext(), "${data}", Toast.LENGTH_SHORT).show()


}



    }

    override fun onAccuracyChanged(sensor: Int, accuracy: Int) {



    }
}