package com.example.finalproject_mealmatchapp.Login_And_Registration

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.finalproject_mealmatchapp.Models.RestaurantUser
import com.example.finalproject_mealmatchapp.Restaurant.Resturant_Callbacks.RestaurantUsersFetchCallback
import com.example.finalproject_mealmatchapp.Utilities.UserManager
import com.example.finalproject_mealmatchapp.databinding.ActivityRegistrationBinding
import com.google.firebase.storage.FirebaseStorage

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegistrationBinding
    private lateinit var userManager : UserManager
//    val REQUEST_CODE_UPLOAD_PDF = 1001
//    var selectedPdfUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userType = intent.getStringExtra("USER_TYPE")
        inituserManager()
        fitLayout(userType!!)
        initSignIn(userType)
    }

    private fun inituserManager() {
        userManager = UserManager()
        userManager.restaurantUsersFetchCallback = object : RestaurantUsersFetchCallback{
            override  fun onUsersFetched(users: List<RestaurantUser>){
               userManager.resturantList = users
            }
        }
    }

    private fun initSignIn(userType : String) {
        binding.btnSignUp.setOnClickListener { registerUser(userType) }
        binding.registrationBTNReturnButton.setOnClickListener{returnLastView()}
    }

    private fun returnLastView() {
        val intent = Intent(this,TypeUserActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun registerUser(userType: String) {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val phoneNumber = binding.etPhoneNumber.text.toString()
        val address = binding.etAddress.text.toString()


        if(userType =="ORGANIZATION"){
             val addedOrganization = userManager.addOrganizationUser(
                name = name,
                email = email,
                password = password,
                address = address,
                phoneNumber = phoneNumber,
                context = this
            )
            if(addedOrganization){
                changeActivityToLogin()
            }
        }

        if(userType == "RESTAURANT"){
            val open = binding.openHourSpinner.selectedItem.toString()
            val close =  binding.closeHourSpinner.selectedItem.toString()
            val type = binding.etType.text.toString()
            val addedRestaurant = userManager.addRestaurantUser(
                name = name,
                email = email,
                password = password,
                address = address,
                phoneNumber = phoneNumber,
                type = type,
                open = open,
                close = close,
                context = this
            )
            if(addedRestaurant){
                changeActivityToLogin()
            }
        }
    }

    private fun changeActivityToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun fitLayout(userType: String) {
        if(userType == "ORGANIZATION"){
            binding.tilRestaurantName.hint = "Organization Name"
//            binding.btnUploadCertificate.text =  "Upload your organization certificate"
            binding.tiltype.visibility = View.GONE
            binding.OpenAndClose.visibility = View.GONE
//            binding.btnUploadCertificate.setOnClickListener { uploadOrganizaionCertificate(binding.etName.text.toString()) }
        } else if (userType == "RESTAURANT"){
            binding.OpenAndClose.visibility = View.VISIBLE
            binding.tilRestaurantName.hint =  "Restaurant Name"
//            binding.btnUploadCertificate.text = "Upload your restaurant certificate"
            setupTimeSpinners()
//            binding.btnUploadCertificate.setOnClickListener { uploadResturantCertificate(binding.etName.text.toString()) }
        }
    }

    private fun setupTimeSpinners() {
        // Create lists for OPEN and CLOSE hours
        val openHours = generateHours(10, 21)  // Open hours from 12:00 to 21:00
        val closeHours = generateHours(11, 22) // Close hours from 13:00 to 00:00

        // Create ArrayAdapters for both spinners
        val openAdapter = object : ArrayAdapter<String>(binding.openHourSpinner.context, com.example.finalproject_mealmatchapp.R.layout.spinner_item, openHours) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(com.example.finalproject_mealmatchapp.R.id.spinnerItemText)
                textView.setTextColor(ContextCompat.getColor(context, com.example.finalproject_mealmatchapp.R.color.black)) // Change selected item color
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                view.setBackgroundColor(ContextCompat.getColor(context, com.example.finalproject_mealmatchapp.R.color.light_green))
                val textView = view.findViewById<TextView>(com.example.finalproject_mealmatchapp.R.id.spinnerItemText)
                textView.setTextColor(ContextCompat.getColor(context, com.example.finalproject_mealmatchapp.R.color.black)) // Change dropdown items color
                return view
            }
        }

        val closeAdapter = object : ArrayAdapter<String>(binding.closeHourSpinner.context, com.example.finalproject_mealmatchapp.R.layout.spinner_item, closeHours) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(com.example.finalproject_mealmatchapp.R.id.spinnerItemText)
                textView.setTextColor(ContextCompat.getColor(context, com.example.finalproject_mealmatchapp.R.color.black)) // Change selected item color
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                view.setBackgroundColor(ContextCompat.getColor(context, com.example.finalproject_mealmatchapp.R.color.light_green))
                val textView = view.findViewById<TextView>(com.example.finalproject_mealmatchapp.R.id.spinnerItemText)
                textView.setTextColor(ContextCompat.getColor(context, com.example.finalproject_mealmatchapp.R.color.black)) // Change dropdown items color
                return view
            }
        }

        binding.openHourSpinner.adapter = openAdapter
        binding.closeHourSpinner.adapter = closeAdapter
    }

    private fun generateHours(start: Int, end: Int): List<String> {
        val hours = mutableListOf<String>()
        for (i in start until end) {
            hours.add("$i:00")
        }
        return hours
    }


}