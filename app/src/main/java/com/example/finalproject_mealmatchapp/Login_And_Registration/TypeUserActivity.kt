package com.example.finalproject_mealmatchapp.Login_And_Registration

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.example.finalproject_mealmatchapp.R
import com.google.android.material.button.MaterialButton

class TypeUserActivity : AppCompatActivity() {

    private lateinit var btnRestaurant : MaterialButton
    private lateinit var btnOrganization : MaterialButton
    private lateinit var registration_BTN_return_button : AppCompatImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_type_user)
        findViews()
        initViews()

    }

    private fun initViews() {
        btnOrganization.setOnClickListener{organizationPicked()}
        btnRestaurant.setOnClickListener{restaurantPicked()}
        registration_BTN_return_button.setOnClickListener{returnLastView()}
    }

    private fun restaurantPicked() {
        val intent = Intent(this, RegistrationActivity::class.java)
        intent.putExtra("USER_TYPE", "RESTAURANT")
        startActivity(intent)
        finish()
    }

    private fun organizationPicked() {
        val intent = Intent(this, RegistrationActivity::class.java)
        intent.putExtra("USER_TYPE", "ORGANIZATION")
        startActivity(intent)
        finish()
    }

    private fun returnLastView() {
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }



    private fun findViews() {
        btnRestaurant = findViewById(R.id.btnRestaurant)
        btnOrganization = findViewById(R.id.btnOrganization)
        registration_BTN_return_button = findViewById(R.id.registration_BTN_return_button)
    }
}