package com.example.finalproject_mealmatchapp.Login_And_Registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finalproject_mealmatchapp.R
import com.example.finalproject_mealmatchapp.Utilities.UserManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {


    private lateinit var btnLogin : MaterialButton
    private lateinit var btnSignIn : MaterialButton
    private lateinit var checkBoxRestaurant :MaterialCheckBox
    private lateinit var checkBoxOrganization : MaterialCheckBox
    private lateinit var etName : TextInputEditText
    private lateinit var etEmail : TextInputEditText
    private lateinit var etPassword : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        enableEdgeToEdge()
        findViews()
        initViews()
        clearFieldInLogin()
    }

    private fun initViews() {
        checkBoxRestaurant.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBoxOrganization.isChecked = false
            }
        }
        checkBoxOrganization.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBoxRestaurant.isChecked = false
            }
        }
        btnSignIn.setOnClickListener{ startRegistration()}
        btnLogin.setOnClickListener{ login()}

    }

    private fun login() {
        val userManager = UserManager()
        val name =etName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        Log.d("UserInfo", "Name: $name, Email: $email, Password: $password")
        if(checkBoxRestaurant.isChecked){
            userManager.login(name,email,password,this,"restaurant_users")
        }
        if(checkBoxOrganization.isChecked){
            userManager.login(name,email,password,this,"organization_users")


        }
    }

    private fun clearFieldInLogin() {
        etName.setText("")
        etEmail.setText("")
        etPassword.setText("")
    }

    private fun startRegistration() {
        var intent = Intent(this, TypeUserActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun findViews() {
        btnLogin = findViewById(R.id.btnLogin)
        btnSignIn = findViewById(R.id.btnSignIn)
        checkBoxRestaurant = findViewById(R.id.checkBoxRestaurant)
        checkBoxOrganization = findViewById(R.id.checkBoxOrganization)
        etEmail = findViewById(R.id.etEmail)
        etName = findViewById(R.id.etName)
        etPassword = findViewById(R.id.etPassword)

    }
}