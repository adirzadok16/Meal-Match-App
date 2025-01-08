package com.example.finalproject_mealmatchapp.Utilities

import android.content.Context
import android.util.Log
import com.example.finalproject_mealmatchapp.Models.Donation
import com.example.finalproject_mealmatchapp.Models.Notification
import com.example.finalproject_mealmatchapp.Models.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DB_Manager {

    var database: FirebaseDatabase = Firebase.database


    fun addDonationToRestaurant(newDonation: Donation, name: String, context: Context) {
        val restaurantRef = initDataBaseToRestaurant(name)
//        restaurantRef.child("donationList").get().addOnSuccessListener { snapshot ->
//            // Check if donationList exists
//            if (snapshot.exists()) {
//                // Create a mutable list to hold donations
//                val currentDonations = mutableListOf<Donation>()
//
//                // Add existing donations if any
//                snapshot.children.forEach { donationSnapshot ->
//                    donationSnapshot.getValue(Donation::class.java)?.let {
//                        currentDonations.add(it)
//                    }
//                }
//
//                // Add the new donation
//                currentDonations.add(newDonation)
//
//                // Update Firebase with the new list
//                restaurantRef.child("donationList").setValue(currentDonations)
//                    .addOnSuccessListener {
//                        // Success handling
//                        Log.d("Firebase", "Donation added successfully")
//                    }
//                    .addOnFailureListener { e ->
//                        // Error handling
//                        Log.e("Firebase", "Error adding donation", e)
//                    }
//            } else {
//                // If donationList doesn't exist, create a new list and add the donation
//                val newDonationList = mutableListOf(newDonation)
//                restaurantRef.child("donationList").setValue(newDonationList)
//                    .addOnSuccessListener {
//                        // Success handling
//                        Log.d("Firebase", "Donation list created and donation added successfully")
//                    }
//                    .addOnFailureListener { e ->
//                        // Error handling
//                        Log.e("Firebase", "Error creating donation list", e)
//                    }
//            }
//        }

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


//    fun addNotificationToRestaurant(newNotification: Notification, name: String) {
//        val restaurantRef = initDataBaseToRestaurant(name)
//
//        // Get the reference to the notification list and the notification count
//        val notificationListRef = restaurantRef.child("notificationList")
//        val notificationCountRef = restaurantRef.child("newNotificationCount")
//
//        // Add the new notification to the notification list
//        val notificationRef = notificationListRef.push()
//        notificationRef.setValue(newNotification)
//            .addOnSuccessListener {
//                // Success handling for adding notification
//                Log.d("Firebase", "Notification added successfully")
//
//                // Check if notificationCount exists, if yes increment by 1, otherwise set to 1
//                notificationCountRef.get().addOnSuccessListener { snapshot ->
//                    if (snapshot.exists()) {
//                        // If notificationCount exists, increment it
//                        incrementNotificationCount(notificationCountRef)
//                    } else {
//                        // If notificationCount doesn't exist, set it to 1
//                        notificationCountRef.setValue(1)
//                            .addOnSuccessListener {
//                                Log.d("Firebase", "Notification count initialized to 1")
//                            }
//                            .addOnFailureListener { e ->
//                                Log.e("Firebase", "Error initializing notification count", e)
//                            }
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//                // Error handling
//                Log.e("Firebase", "Error adding notification", e)
//            }
//    }
//
//    // Function to increment the notification count by 1
//    private fun incrementNotificationCount(notificationCountRef: DatabaseReference) {
//        notificationCountRef.runTransaction(object : Transaction.Handler {
//            override fun doTransaction(currentData: MutableData): Transaction.Result {
//                // If the data doesn't exist, start with 0
//                val currentCount = currentData.getValue(Int::class.java) ?: 0
//                currentData.value = currentCount + 1 // Increment by 1
//                return Transaction.success(currentData)
//            }
//
//            override fun onComplete(
//                error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?
//            ) {
//                if (committed) {
//                    Log.d("Firebase", "Notification count updated successfully")
//                } else {
//                    Log.e("Firebase", "Failed to update notification count")
//                }
//            }
//        })
//    }



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
//        restaurantRef.child("notificationList").get().addOnSuccessListener { snapshot ->
//            // Check if notificationList exists
//            if (snapshot.exists()) {
//                // Create a mutable list to hold notifications
//                val currentNotifications = mutableListOf<Notification>()
//
//                // Add existing notifications if any
//                snapshot.children.forEach { notificationSnapshot ->
//                    notificationSnapshot.getValue(Notification::class.java)?.let {
//                        currentNotifications.add(it)
//                    }
//                }
//
//                // Add the new notification
//                currentNotifications.add(newNotification)
//
//                // Update Firebase with the new list
//                restaurantRef.child("notificationList").setValue(currentNotifications)
//                    .addOnSuccessListener {
//                        // Success handling
//                        Log.d("Firebase", "Notification added successfully")
//                    }
//                    .addOnFailureListener { e ->
//                        // Error handling
//                        Log.e("Firebase", "Error adding notification", e)
//                    }
//            } else {
//                // If notificationList doesn't exist, create a new list and add the notification
//                val newNotificationList = mutableListOf(newNotification)
//                restaurantRef.child("notificationList").setValue(newNotificationList)
//                    .addOnSuccessListener {
//                        // Success handling
//                        Log.d("Firebase", "Notification list created and notification added successfully")
//                    }
//                    .addOnFailureListener { e ->
//                        // Error handling
//                        Log.e("Firebase", "Error creating notification list", e)
//                    }
//            }
//        }

}






