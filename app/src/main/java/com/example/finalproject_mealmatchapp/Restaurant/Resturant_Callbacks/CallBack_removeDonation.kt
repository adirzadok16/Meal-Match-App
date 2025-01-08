package com.example.finalproject_mealmatchapp.Restaurant.Resturant_Callbacks

import com.example.finalproject_mealmatchapp.Models.Donation

interface CallBack_removeDonation {
    fun removeDonation(donation : Donation, position:Int)
}