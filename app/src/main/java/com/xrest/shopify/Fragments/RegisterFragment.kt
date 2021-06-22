package com.xrest.shopify.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Class.User
import com.xrest.shopify.Retrofit.Repository.UserRepo
import kotlinx.android.synthetic.main.success_notification.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment(), View.OnClickListener {


    lateinit var name:EditText
    lateinit var username:EditText
    lateinit var password:EditText
    lateinit var spinner: Spinner
    lateinit var btn: Button
    lateinit var img:ImageView
    var image:String?=null
    lateinit var txt:TextView
    var userType=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var array = arrayOf("Admin","Customer")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        initialize(view)



        spinner.adapter=ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,array)
        spinner!!.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                userType = parent!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


         btn.setOnClickListener(this)
        img.setOnClickListener()
        {
            val popupMenu = PopupMenu(requireContext(),img)
            popupMenu.menuInflater.inflate(R.menu.camera_gallery,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId)
                {
                    R.id.gallery->{
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type="image/*"
                       startActivityForResult(intent,0)

                    }
                    R.id.camera->{
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent,1)
                    }
                }
                true
            }

            popupMenu.show()


        }

        return view
    }
    private fun initialize(view:View)
    {
     name = view.findViewById(R.id.name)
     username = view.findViewById(R.id.username)
     password = view.findViewById(R.id.password)
     spinner = view.findViewById(R.id.spinner)
     btn = view.findViewById(R.id.register)
        img = view.findViewById(R.id.profile)
        txt = view.findViewById(R.id.text)

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.register->{
                val user = User(Name=name.text.toString(),Username =username.text.toString(),Password = password.text.toString(),UserType = userType)
                registerUser(user)
            }

            R.id.profile->{



            }




        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK)
        {


            if(requestCode==0 && data!=null)
            {
                val newImage = data.data
                val path = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = context?.contentResolver
                val cursor =contentResolver?.query(newImage!!,path,null,null,null)
                cursor?.moveToFirst()
                val colIndex= cursor?.getColumnIndex(path[0])
                image = cursor?.getString(colIndex!!)
                img.setImageBitmap(BitmapFactory.decodeFile(image))
                cursor?.close()


            }
            else if(requestCode==1 && data!=null){
                val bitMap = data.extras?.get("data") as Bitmap
                val name = SimpleDateFormat("yyyyMMdd").format(Date()).toString()
                val file: File = bitmapToFile(bitMap,"$name.jpg")
                image = file.absolutePath
                img.setImageBitmap(BitmapFactory.decodeFile(image))


            }



        }


    }

    private fun bitmapToFile(bitMap: Bitmap, s: String): File {
var file:File?=null
        try {

            file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()+File.separator + s)
            file.createNewFile()
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitMap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream)
            val data = byteArrayOutputStream.toByteArray()
            val file = FileOutputStream(file)
            file.write(data)
            file.flush()
            file.close()


        }
        catch (ex:java.lang.Exception)
        {
            ex.printStackTrace()
        }

        return file!!
    }




    fun registerUser(user:User)
    { var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.success_notification)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
        var lottie:LottieAnimationView = dialog.findViewById(R.id.lottie)
        var textView:TextView = dialog.findViewById(R.id.success)
        dialog.show()
        dialog.setCancelable(false)

        CoroutineScope(Dispatchers.IO).launch {

            val response = UserRepo().register(user)
            if(response.success==true)
            {
                if(image!=null)
                {
uploadImage(response.message!!)

                }
                else{
                    withContext(Main)
                    {

                        lottie.setAnimation(R.raw.success)
                        success.text ="User Have Been Registered"
                          lottie.setOnClickListener(){
                              changeFragment(LoginFragment())
                          }
                    }
                }



            }




        }
    }


    fun changeFragment(fragment:Fragment){
        requireFragmentManager().beginTransaction().apply {
            replace(R.id.fl,fragment)
            commit()
            addToBackStack(null)
        }
    }

    fun uploadImage(id:String){

        if(image!==null)
        {
          try {

              CoroutineScope(Dispatchers.IO).launch {
                  val file = File(image)
                  var extention = MimeTypeMap.getFileExtensionFromUrl(image)
                  var mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
                  var reqbody = RequestBody.create(MediaType.parse(mimeType),file)
                  var body = MultipartBody.Part.createFormData("profile",file.name,reqbody)
                  val response = UserRepo().uploadProfile(id,body)
                  if(response.success==true)
                  {
                      withContext(Main)
                      {
                          Toast.makeText(requireContext(), "User Added", Toast.LENGTH_SHORT).show()
                          changeFragment(LoginFragment())
                      }

                  }
                  else{
                      withContext(Main)
                      {
                          Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                          Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                          changeFragment(LoginFragment())
                      }

                  }              }

          }
          catch (ex:Exception){

          }



        }
        else{
            Toast.makeText(requireContext(), "User Added", Toast.LENGTH_SHORT).show()
            changeFragment(LoginFragment())
        }


    }

}