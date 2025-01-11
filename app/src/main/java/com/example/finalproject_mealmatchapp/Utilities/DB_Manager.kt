package com.example.finalproject_mealmatchapp.Utilities

import android.content.Context
import android.util.Log
import com.example.finalproject_mealmatchapp.Models.Donation
import com.example.finalproject_mealmatchapp.Models.Notification
import com.example.finalproject_mealmatchapp.Models.Order
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DB_Manager {

    var database: FirebaseDatabase = Firebase.database


    fun addDonationToRestaurant(newDonation: Donation, name: String, context: Context) {
        val restaurantRef = initDataBaseToRestaurant(name)
        restaurantRef.child("donationList").child(newDonation.title).setValue(newDonation)
            .addOnSuccessListener {
                // Success handling
                Log.d("Firebase", "Donation ${newDonation.title} added successfully")
            }
            .addOnFailureListener { e ->
                // Error handling
                Log.e("Firebase", "Error adding donation ${newDonation.title}", e)
            }
    }


    fun addNotificationToRestaurant(newNotification: Notification, name: String) {
        val restaurantRef = initDataBaseToRestaurant(name)

        val notificationRef = restaurantRef.child("notificationList").push()
        notificationRef.setValue(newNotification)
            .addOnSuccessListener {
                // Success handling
                Log.d("Firebase", "Notification added successfully")
            }
            .addOnFailureListener { e ->
                // Error handling
                Log.e("Firebase", "Error adding notification", e)
            }
    }


    fun addOrderToHistory(newOrder: Order, name: String) {
        val organizationRef = initDataBaseToOrganization(name)

        val orderRef = organizationRef.child("orderHistory").push()
        orderRef.setValue(newOrder)
            .addOnSuccessListener {
                // Success handling
                Log.d("Firebase", "Notification added successfully")
            }
            .addOnFailureListener { e ->
                // Error handling
                Log.e("Firebase", "Error adding notification", e)
            }
    }


    fun initDataBaseToRestaurant(name: String): DatabaseReference {
        return database.getReference("restaurant_users").child(name)
    }

    fun initDataBaseToOrganization(name: String): DatabaseReference {
        return database.getReference("organization_users").child(name)
    }

}






