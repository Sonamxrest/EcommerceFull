package com.xrest.shopify.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.value.LottieRelativeFloatValueCallback
import com.google.android.material.button.MaterialButton
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Class.Product
import com.xrest.shopify.Retrofit.Repository.ProductRepository
import kotlinx.android.synthetic.main.fragment_add_product.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class AddProduct : Fragment(), View.OnClickListener {


    lateinit var textView:ImageView
    lateinit var btn:ImageButton
    lateinit var name:EditText
    lateinit var price:EditText
    lateinit var stock:EditText
    lateinit var description:EditText
    lateinit var spinner:Spinner
    lateinit var sumbit:Button
    var category=""
    var array = arrayOf("Clothes","Electronics","Furniture","Glasses")
    var image:String?=null
    var images:MutableList<String> = mutableListOf()
    var count =0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_add_product, container, false)
        initialize(view)
        spinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category =parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                category ="Clothes"
            }
        }
        btn.setOnClickListener(this)
        sumbit.setOnClickListener(this)
        return view
    }

    private fun addview(view:View) {
        count +=1
        val rootLayout: LinearLayout = requireView().findViewById(R.id.root)
        if(count==4)
        {
        btn.isVisible=false
        }
         textView = ImageView(requireContext())
        var params =LinearLayout.LayoutParams(250, 300)
        params.setMargins(5,5,5,5)
        textView.layoutParams =params
        textView.setImageResource(R.drawable.image)
        textView.maxWidth=200

        textView.id=100+1
        textView.scaleType=ImageView.ScaleType.FIT_XY
        textView.setOnClickListener(){
            val popup = PopupMenu(requireContext(),textView)
            popup.menuInflater.inflate(R.menu.camera_gallery,popup.menu)
            popup.setOnMenuItemClickListener {
                when(it?.itemId)
                {
                    R.id.gallery->{
                     val intent = Intent(Intent.ACTION_PICK)
                        intent.type ="image/*"
                        startActivityForResult(intent,0)

                    }
                    R.id.camera->{
                        val intent =Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent,1)
                    }
                }
                true
            }
            popup.show()
        }
        rootLayout.addView(textView)

        btn.isEnabled=false
        btn.setImageResource(R.drawable.heart_orange_fill)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK)
        {
            if(requestCode==0 && data!=null)
            {
                var currentImage = data.data!!
                var imagePath = arrayOf(MediaStore.Images.Media.DATA)
                var contentResolver = requireContext().contentResolver
                var cursor = contentResolver.query(currentImage,imagePath,null,null,null)
                cursor?.moveToFirst()
                val colIndex = cursor?.getColumnIndex(imagePath[0])
                image = cursor?.getString(colIndex!!)
                images.add(image!!)
                textView.setImageBitmap(BitmapFactory.decodeFile(image))
                cursor?.close()


            }
            else if(requestCode==1 &&data!=null)
            {
                var bitMap = data.extras?.get("data") as Bitmap
                var timeStamp = SimpleDateFormat("yyyyMMdd").format(Date())
                val file:File = bitmapToFile(bitMap,"${timeStamp}.jpg")
                image = file.absolutePath
                images.add(image!!)
                textView.setImageBitmap(BitmapFactory.decodeFile(image))

            }
            btn.isEnabled=true
            btn.setImageResource(R.drawable.add)
            Log.d("array",images.toString())
        }





    }

    private fun bitmapToFile(bitMap: Bitmap, s: String): File {
    var file:File?=null
        try {
            file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + File.separator + s)
            file.createNewFile()
            var bos = ByteArrayOutputStream()
            bitMap.compress(Bitmap.CompressFormat.PNG,0,bos)
            var data = bos.toByteArray()
            var fos = FileOutputStream(file)
            fos.write(data)
            fos.flush()
            fos.close()

        }
        catch (ex:java.lang.Exception){
            ex.printStackTrace()
        }


        return file!!
    }

    fun alert(){

        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.success_notification)
        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
var txt:TextView = dialog.findViewById(R.id.success)
        var lottie:LottieAnimationView = dialog.findViewById(R.id.lottie)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Main)
            {
                lottie.setAnimation(R.raw.success)
                lottie.playAnimation()
                txt.text ="Success"
                lottie.loop(false)

            }
        }
        lottie.setOnClickListener(){
            dialog.cancel()
        }
        dialog.setCancelable(false)
        dialog.show()



    }
    fun initialize(view:View){
        btn = view.findViewById(R.id.add)
        name = view.findViewById(R.id.name)
        price = view.findViewById(R.id.price)
        description = view.findViewById(R.id.description)
        stock = view.findViewById(R.id.stock)
        sumbit = view.findViewById(R.id.submit)
        spinner = view.findViewById(R.id.spinner)
        spinner.adapter = context?.let { ArrayAdapter(it,android.R.layout.simple_list_item_1,array) }

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.add->{
                view?.let { addview(it) }
            }
            R.id.submit->{

                register()
            }
        }
    }

    private fun register() {
        val product = Product(Name=name.text.toString(),Category = category,Price = price.text.toString().toInt(),Stock = stock.text.toString().toInt(),Description = description.text.toString())
        try {
CoroutineScope(Dispatchers.IO).launch {
    val repo = ProductRepository().addProduct(product)
    if(repo.success==true)
    {
        uploadImages(repo.message!!)


    }
}
        }
        catch (ex:Exception)
        {

        }
    }

    private fun uploadImages(id:String) {

        var body:MutableList<MultipartBody.Part> = mutableListOf()
        for(data in images)
        {
            var file = File(data)
            var extention = MimeTypeMap.getFileExtensionFromUrl(image)
            var mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
            var reqBody = RequestBody.create(MediaType.parse(mimeType),file)
            var data = MultipartBody.Part.createFormData("image",file.name,reqBody)
            body.add(data)
        }
        Log.d("brray",body.toString())
        CoroutineScope(Dispatchers.IO).launch {

            val repo = ProductRepository().uploadImages(id,body)
            if(repo.success==true)
            {
                withContext(Main)
                {
                    alert()
                }
            }


        }





    }


}