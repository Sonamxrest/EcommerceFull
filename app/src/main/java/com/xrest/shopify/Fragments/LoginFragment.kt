package com.xrest.shopify.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.*
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.common.Feature
import com.xrest.shopify.Dashboard
import com.xrest.shopify.FrontViewFragment
import com.xrest.shopify.R
import com.xrest.shopify.Retrofit.Repository.UserRepo
import com.xrest.shopify.Retrofit.Services.RetrofitBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.MediaType
import java.util.concurrent.Executor


class LoginFragment : Fragment(), View.OnClickListener,SensorEventListener {


    lateinit var etUsername:EditText
    lateinit var etPassword:EditText
    lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var sensorManager:SensorManager
    lateinit var sensor:Sensor
    lateinit var sensor2:Sensor


    var bio:Boolean?=null
    var cancellListener:CancellationSignal?=null

    val cb :BiometricPrompt.AuthenticationCallback
    get() = @RequiresApi(Build.VERSION_CODES.P)
    object :BiometricPrompt.AuthenticationCallback(){
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
            super.onAuthenticationError(errorCode, errString)
            Toast.makeText(requireContext(), "Could Not Verify ", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            var preferences = requireActivity().getSharedPreferences("Login", MODE_PRIVATE)
            login(preferences.getString("username","")!!,preferences.getString("password","")!!)
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(requireContext(), "Could Not Verify ", Toast.LENGTH_SHORT).show()

        }
    }


    val permission = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_FINE_LOCATION)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_login, container, false)
        cancellListener = CancellationSignal()
            sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
                 sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
                    sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)




        cancellListener!!.setOnCancelListener {

        }

        executor = ContextCompat.getMainExecutor(requireContext())
        var biometrics:LinearLayout = view.findViewById(R.id.biometric)
        biometrics.isVisible=false
        var pref = requireActivity().getSharedPreferences("Login", MODE_PRIVATE)
         bio = pref.getBoolean("biometric",false)
        if(bio!!)
        {
            biometrics.isVisible=true
        }
        if(!checkPermission())
        {
            askPermission()
        }
         etUsername = view.findViewById(R.id.username) as EditText
         etPassword = view.findViewById<EditText>(R.id.password)
        val login: Button = view.findViewById(R.id.login)
        val register = view.findViewById<TextView>(R.id.register)


        login.setOnClickListener(this)
        register.setOnClickListener(this)
        biometrics.setOnClickListener(this)



        return view
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onClick(v: View?) {
        when(v?.id)
        {

            R.id.login->{
login(etUsername.text.toString(),etPassword.text.toString())
            }
            R.id.register->{

                requireFragmentManager().beginTransaction().apply {
                    replace(R.id.fl,RegisterFragment())
                    commit()
                    addToBackStack(null)
                }

            }
            R.id.biometric->{


                biometricPrompt = BiometricPrompt.Builder(requireContext()).setTitle("Biometric Login").setSubtitle("Please Place Your Finger on the Scanner").
                setNegativeButton("Cancel",executor,DialogInterface.OnClickListener(){dialogInterface, i ->
                    dialogInterface.cancel()
                }).build()
                biometricPrompt.authenticate(cancellListener!!,executor,cb)



            }
        }
    }


 fun checkPermission():Boolean{
     for (p in permission)
     {
         if(ActivityCompat.checkSelfPermission(requireContext(),p)!=PackageManager.PERMISSION_GRANTED)
         {
             return false
         }
     }
     return true
 }
    fun askPermission(){
        ActivityCompat.requestPermissions(requireActivity(),permission,1)
    }


    fun login(username:String,password:String)
    {

        CoroutineScope(Dispatchers.IO).launch {

            val response = UserRepo().login(username,password)
            if(response.success==true)
            {
                RetrofitBuilder.token ="Bearer "+response.token
                RetrofitBuilder.user = response.data!!
                withContext(Main)
                {

                    if(!bio!!)
                    {
                        var alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.
                        setTitle("FingerPrint Verification")

                        alertDialog.setMessage("Do you want to enable fingerprint Login?").setPositiveButton(
                            "Yes",
                            DialogInterface.OnClickListener() { dialog, which ->

                                if (check()!=true) {
                                    Toast.makeText(requireContext(), "FingerPrint Not Supported", Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    var sharedPreferences = requireActivity().getSharedPreferences(
                                        "Login",
                                        MODE_PRIVATE
                                    )
                                    var editor = sharedPreferences.edit()
                                    editor.putBoolean("biometric", true)
                                    editor?.putString("username", etUsername.text.toString())
                                    editor?.putString("password", etPassword.text.toString())
                                    editor.apply()
                                    editor?.commit()
                                }


                            })
                        alertDialog.setNegativeButton("No",DialogInterface.OnClickListener(){dialog,which->{
                            dialog.cancel()
                        }})

                        var dialog = alertDialog.create()
                        dialog.show()
                        dialog.setCancelable(false)
                    }

                   requireActivity().startActivity(Intent(requireContext(),Dashboard::class.java))
                }

            }

        }
    }


    fun check():Boolean{

        var keyGuard = requireContext().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if(!keyGuard.isKeyguardSecure)
        {
            return false
        }
        if(!(requireActivity().packageManager.hasSystemFeature(Context.FINGERPRINT_SERVICE)))
        {
            return false
        }
        return true
    }


    fun checkSensor(sensor: Sensor?):Boolean{
        if(sensorManager.getDefaultSensor(sensor!!.type)==null)
        {
            return false
        }
        return true
    }
    override fun onSensorChanged(event: SensorEvent?) {

        if(event?.sensor?.type==Sensor.TYPE_PROXIMITY)
        {
            Toast.makeText(requireContext(), "${event?.values?.get(0)}", Toast.LENGTH_SHORT).show()

        }
        else if(event?.sensor?.type ==Sensor.TYPE_AMBIENT_TEMPERATURE)
        {
            Toast.makeText(requireContext(), "${event?.values?.get(0)}", Toast.LENGTH_SHORT).show()

        }



    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        if(checkSensor(sensor))
        {
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this,sensor2,SensorManager.SENSOR_DELAY_NORMAL)


        }
    }



}