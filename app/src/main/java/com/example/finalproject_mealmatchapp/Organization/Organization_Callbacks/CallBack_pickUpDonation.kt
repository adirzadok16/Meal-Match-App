package com.example.finalproject_mealmatchapp.Organization.Organization_Callbacks

import com.example.finalproject_mealmatchapp.Models.Donation

interface CallBack_pickUpDonation {
    fun pickUpDination(donation: Donation, position : Int)
}