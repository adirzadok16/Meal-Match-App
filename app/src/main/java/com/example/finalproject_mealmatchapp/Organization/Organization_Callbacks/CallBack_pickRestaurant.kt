package com.example.finalproject_mealmatchapp.Organization.Organization_Callbacks

import com.example.finalproject_mealmatchapp.Models.RestaurantUser

interface CallBack_selectRestaurant {
    fun selectRestaurant(restaurantUser : RestaurantUser , position : Int)
}