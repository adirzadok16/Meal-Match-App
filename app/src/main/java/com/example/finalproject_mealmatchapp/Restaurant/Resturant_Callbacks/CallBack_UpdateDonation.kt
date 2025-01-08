package com.example.finalproject_mealmatchapp.Restaurant.Resturant_Callbacks

import com.example.finalproject_mealmatchapp.Models.Donation

interface CallBack_UpdateDonation {
    fun updateDonation(donation : Donation, position:Int  )
}