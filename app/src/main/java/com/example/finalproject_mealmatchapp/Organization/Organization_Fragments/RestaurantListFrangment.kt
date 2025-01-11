package com.example.finalproject_mealmatchapp.Organization.Organization_Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_mealmatchapp.Adapters.RestaurantAdapter
import com.example.finalproject_mealmatchapp.Models.RestaurantUser
import com.example.finalproject_mealmatchapp.Organization.DonationListSpecificRestaurant
import com.example.finalproject_mealmatchapp.Organization.OrganizationHomePage
import com.example.finalproject_mealmatchapp.Organization.Organization_Callbacks.CallBack_selectRestaurant
import com.example.finalproject_mealmatchapp.databinding.FragmentRestaurantListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class RestaurantListFrangment : Fragment() {
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var binding: FragmentRestaurantListBinding
    private var restaurants = mutableListOf<RestaurantUser>()
    val database = FirebaseDatabase.getInstance()
    var organizationUserName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantListBinding.inflate(inflater, container, false)
        return binding.root
    }



    fun showRestaurants(restaurantName: String, context: OrganizationHomePage) {
        // Clear the previous results
        restaurants.clear()

        val database = FirebaseDatabase.getInstance().reference
        database.child("restaurant_users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Loop through the snapshot to get all restaurants
                    for (restaurantSnapshot in snapshot.children) {
                        val restaurant = restaurantSnapshot.getValue(RestaurantUser::class.java)
                        if (restaurant != null && restaurant.name.contains(restaurantName, ignoreCase = true)) {
                            restaurants.add(restaurant)
                        }
                    }

                    // Move these inside onDataChange
                    restaurantAdapter = RestaurantAdapter(restaurants)
                    restaurantAdapter.callBack_selectRestaurant = object : CallBack_selectRestaurant {
                        override fun selectRestaurant(restaurantUser: RestaurantUser, position: Int) {
//                            checkTimeForPickUp(restaurantUser , context)
                            // for test the project i put here also moving to next page even if the time is after closing
                            val intent = Intent(context, DonationListSpecificRestaurant::class.java)
                            val bundle = Bundle()
                            bundle.putString("restaurant_user_name", restaurantUser.name)
                            bundle.putString("organization_user_name", organizationUserName)
                            intent.putExtras(bundle)
                            context.startActivity(intent)
                        }
                    }

                    binding.mainRVList.adapter = restaurantAdapter
                    val linearLayoutManager = LinearLayoutManager(context)
                    linearLayoutManager.orientation = RecyclerView.VERTICAL
                    binding.mainRVList.layoutManager = linearLayoutManager
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle any errors
                    println("Error fetching data: ${error.message}")
                }
            })
    }

    private fun checkTimeForPickUp(restaurantUser: RestaurantUser, context: Context) {
        val reference = database.getReference("restaurant_users")
        val userReference = reference.child(restaurantUser.name)

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val openTime = dataSnapshot.child("open").getValue(String::class.java)
                    val closeTime = dataSnapshot.child("close").getValue(String::class.java)
                    Log.d("Hours checking", "HERE!!!!!!!!!!!")

                    if (openTime != null && closeTime != null) {
                        if (isCurrentTimeInRangeOrBeforeOpen(openTime, closeTime)) {
                            Log.d("Hours checking Pass", "HERE!!!!!!!!!!!")
                            val intent = Intent(context, DonationListSpecificRestaurant::class.java)
                            val bundle = Bundle()
                            bundle.putString("restaurant_user_name", restaurantUser.name)
                            bundle.putString("organization_user_name", organizationUserName)
                            intent.putExtras(bundle)
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(
                                context,
                                "The restaurant is closed. Please try again tomorrow.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        println("Open or Close time is missing.")
                    }
                } else {
                    println("User not found.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error retrieving data: ${databaseError.message}")
            }
        })
    }

    private fun isCurrentTimeInRangeOrBeforeOpen(openTime: String, closeTime: String): Boolean {
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormatter.format(Date())

        val openTimeParsed = timeFormatter.parse(openTime)
        val closeTimeParsed = timeFormatter.parse(closeTime)
        val currentTimeParsed = timeFormatter.parse(currentTime)

        // Calculate one hour before the opening time
        val calendar = Calendar.getInstance()
        if (openTimeParsed != null) {
            calendar.time = openTimeParsed
            calendar.add(Calendar.HOUR_OF_DAY, -1)
        }
        val oneHourBeforeOpen = calendar.time

        // Validate parsed times
        if (openTimeParsed == null || closeTimeParsed == null || currentTimeParsed == null) {
            return false
        }

        return if (closeTimeParsed.before(openTimeParsed)) {
            // Case: Closing time is past midnight (e.g., 22:00 to 02:00)
            currentTimeParsed.after(oneHourBeforeOpen) &&
                    (currentTimeParsed.after(openTimeParsed) || currentTimeParsed.before(closeTimeParsed))
        } else {
            // Case: Regular operating hours (e.g., 08:00 to 22:00)
            currentTimeParsed.after(oneHourBeforeOpen) &&
                    currentTimeParsed.before(closeTimeParsed)
        }
    }




}