package com.example.finalproject_mealmatchapp.Utilities

import android.app.Activity
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.example.finalproject_mealmatchapp.Models.OrganizationUser
import com.example.finalproject_mealmatchapp.Models.RestaurantUser
import com.example.finalproject_mealmatchapp.Organization.OrganizationHomePage
import com.example.finalproject_mealmatchapp.Restaurant.RestaurantHomePage
import com.example.finalproject_mealmatchapp.Restaurant.Resturant_Callbacks.RestaurantUsersFetchCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class UserManager {

    var database: FirebaseDatabase = Firebase.database
    var restaurantUsersFetchCallback: RestaurantUsersFetchCallback? = null
    var resturantList : List<RestaurantUser> = emptyList<RestaurantUser>()


    // Function to add a restaurant user
     fun addRestaurantUser(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        address: String,
        type: String,
        context: Context,
        open: String,
        close: String
    ): Boolean {

         val restaurants_ref = initDBConnectionRef("restaurant_users")


        val restaurantUser = RestaurantUser(
            name = name,
            email = email,
            password = password,
            phoneNumner = phoneNumber,
            address = address,
            open = open,
            close = close,
            type = type,
        )

        val checkValidation: Boolean = validateUser(restaurantUser, context)
        if (!checkValidation) {
            return false
        }
//        fetchRestaurantUsers(restaurantUsersFetchCallback,restaurants_ref)
//        Log.d("RESTAURANT USERS",resturantList.toString())
            setToDatabase(restaurantUser, restaurants_ref.child(restaurantUser.name), context)
            return true
    }

    private fun fetchRestaurantUsers(restaurantUsersFetchCallback: RestaurantUsersFetchCallback? , ref : DatabaseReference) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<RestaurantUser>()

                for (userSnapshot in snapshot.children) {
                    Log.d("USER SNAPCHOT" , userSnapshot.toString())
                    val user = userSnapshot.getValue(RestaurantUser::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }

                // Pass the user list to the callback
                restaurantUsersFetchCallback?.onUsersFetched(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error fetching data: ${error.message}")
            }
        })
    }


    /////////////////////////////////////////////////RESTAURANT////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////ORGANIZATION////////////////////////////////////////////////////////////////////////

    // Function to add an organization user
    fun addOrganizationUser(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        address: String,
        context: Context
    ): Boolean {


        val organiaztions_ref = initDBConnectionRef("organization_users")


        val organizationUser = OrganizationUser(
            name = name,
            email = email,
            password = password,
            phoneNumner = phoneNumber,
            address = address
        )

        val checkValidation: Boolean = validateUser(organizationUser, context)
        if (!checkValidation) {
            return false
        }
            setToDatabase(organizationUser, organiaztions_ref.child(organizationUser.name), context)

        return true
    }

    /////////////////////////////////////////////////ORGANIZATION////////////////////////////////////////////////////////////////////////

    private fun setToDatabase(user: Any, ref: DatabaseReference, context: Context) {
        ref.setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "USER ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "USER NOT ADDED, FIREBASE FAILED!", Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun login(name:String,email: String, password: String, context: Context, userType: String) {
        Log.d("SNAPCHOT USER" ,"Im Here!!!!'")
        database.getReference(userType).child(name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("SNAPCHOT USER" ,snapshot.toString())
                    if (snapshot.exists()) {
                        val storedEmail = snapshot.child("email").getValue(String::class.java)
                        val storedPassword = snapshot.child("password").getValue(String::class.java)
                        val storedName = snapshot.child("name").getValue(String::class.java)
                        Log.d("STORED INFO" , "$storedName $storedEmail $storedPassword")
                        Log.d("USER INPUT", "$name $email $password")
                        if (storedName != name || storedEmail != email || storedPassword != password) {
                            // If no match is found
                            Toast.makeText(context, "Invalid name ,email or password", Toast.LENGTH_SHORT).show()
                            // Login successful, move to the next screen
                        } else{
                            navigateToNextScreen(name,userType, context)
                        }
                    } else{
                        if(userType == "restaurant_users"){
                            vibrate(context)
                            Toast.makeText(context, "There is no restaurant as : $name", Toast.LENGTH_SHORT).show()
                        }
                        if(userType == "organization_users"){
                            vibrate(context)
                            Toast.makeText(context, "\"There is no organization as : $name\"", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun navigateToNextScreen(name:String,userType: String, context: Context) {
        if (userType == "restaurant_users" && name.isNotEmpty()) {
            val intent = Intent(context,RestaurantHomePage::class.java)
            val bundle = Bundle()
            bundle.putString("user_name",name)
            intent.putExtras(bundle)
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()  // Only works if context is Activity
            }
        }
        if (userType == "organization_users") {
            val intent = Intent(context, OrganizationHomePage::class.java)
            val bundle = Bundle()
            bundle.putString("user_name",name)
            intent.putExtras(bundle)
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }
        }
    }


    private fun validateUser(user: Any, context: Context): Boolean {
        val nameRegex = "^.{5,30}$".toRegex()
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{4,20}$".toRegex()
        val emailRegex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+\$".toRegex()
        val phoneRegex = "^05\\d{8}$".toRegex()
        val addressRegex = "^[^,]+,\\d+,[^,]+$".toRegex()


        var errorMessage = ""

        // Common validation for both RestaurantUser and OrganizationUser
        val name = when (user) {
            is RestaurantUser -> user.name
            is OrganizationUser -> user.name
            else -> ""
        }

        val password = when (user) {
            is RestaurantUser -> user.password
            is OrganizationUser -> user.password
            else -> ""
        }

        val email = when (user) {
            is RestaurantUser -> user.email
            is OrganizationUser -> user.email
            else -> ""
        }

        val phone = when (user) {
            is RestaurantUser -> user.phoneNumner
            is OrganizationUser -> user.phoneNumner
            else -> ""
        }

        val address = when (user) {
            is RestaurantUser -> user.address
            is OrganizationUser -> user.address
            else -> ""
        }

        // Validate name
        if (!name.matches(nameRegex)) {
            errorMessage += "Name must be between 5 and 30 characters.\n"
        }

        // Validate password
        if (!password.matches(passwordRegex)) {
            errorMessage += "Password must be 4 to 20 characters long.\n"
        }

        // Validate email
        if (!email.matches(emailRegex)) {
            errorMessage += "Email must contain @ and be in a valid format.\n"
        }

        // Validate phone number
        if (!phone.matches(phoneRegex)) {
            errorMessage += "Phone number must be 10 digits, and contain only numbers.\n"
        }

        // Validate address
        if (!address.matches(addressRegex)) {
            errorMessage += "Address must be in the format: 'Street,Number,City'.\n"
        }


        // If there were any validation errors, show the message
        return if (errorMessage.isEmpty()) {
            true
        } else {
            Toast.makeText(
                context,
                "Adding Failed. Errors in the Following Fields:",
                Toast.LENGTH_SHORT
            ).show()
//            Toast.makeText(context, errorMessage.trim(), Toast.LENGTH_LONG).show()
            val sentences = errorMessage.trim().split("\n")
            for (sentence in sentences) {
                Toast.makeText(context, sentence, Toast.LENGTH_SHORT).show()
            }
            false
        }
    }


    private fun initDBConnectionRef(path: String) :DatabaseReference{
        return database.getReference(path)
    }


    private fun vibrate(context : Context) {
        val v = context.getSystemService(VIBRATOR_SERVICE) as Vibrator

        // Vibrate for VIBRATE_DURATION milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(
                VibrationEffect.createOneShot(
                    300,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            //deprecated in API 26
            v.vibrate(300)
        }

    }

}

