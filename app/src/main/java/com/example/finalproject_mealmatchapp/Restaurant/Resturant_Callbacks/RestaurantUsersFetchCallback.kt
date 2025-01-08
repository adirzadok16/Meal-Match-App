package com.example.finalproject_mealmatchapp.Restaurant.Resturant_Callbacks

import com.example.finalproject_mealmatchapp.Models.RestaurantUser

interface RestaurantUsersFetchCallback {
    fun onUsersFetched(users: List<RestaurantUser>)
}