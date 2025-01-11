package com.example.finalproject_mealmatchapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_mealmatchapp.Models.Donation
import com.example.finalproject_mealmatchapp.Organization.Organization_Callbacks.CallBack_pickUpDonation
import com.example.finalproject_mealmatchapp.Utilities.ImageLoader
import com.example.finalproject_mealmatchapp.databinding.HorizontalDonationRetaurantItemBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PickDonationAdapter(
    var donations: List<Donation> = emptyList(),
    var restaurant_name : String = ""
) : RecyclerView.Adapter<PickDonationAdapter.DonationViewHolder>(){


    var callBack_pickUpDination : CallBack_pickUpDonation? =null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val binding = HorizontalDonationRetaurantItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DonationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val storage = FirebaseStorage.getInstance()
         with(holder){
            with(donations.get(position)){
                val storageReference : StorageReference = storage.reference
                    .child("restaurants")
                    .child(restaurant_name)
                    .child(title)// Example path
                ImageLoader.getInstance().loadFromFirebaseStorage(storageReference,binding.donationIMGIcon)
                binding.donationLBLTitle.text = title
                binding.donationLBLTitle.text = title
                binding.donationLBLAmount.text = buildString {
                    append("x")
                    append(amount.toString())
                }
                binding.donationLBLDescription.text = description
            }
        }
    }



    override fun getItemCount(): Int {
        return donations.size
    }

    fun getItem(position: Int) = donations.get(position)

    inner class DonationViewHolder(val binding: HorizontalDonationRetaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
        init {

            binding.donationBTNPickUp.setOnClickListener{
                if(callBack_pickUpDination != null){
                    callBack_pickUpDination!!.pickUpDination(
                        getItem(adapterPosition),
                        adapterPosition
                    )
                }

            }

        }
    }



}

