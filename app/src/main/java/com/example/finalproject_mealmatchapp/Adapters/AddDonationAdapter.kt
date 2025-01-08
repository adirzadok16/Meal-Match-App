package com.example.finalproject_mealmatchapp.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject_mealmatchapp.Models.Donation
import com.example.finalproject_mealmatchapp.R
import com.example.finalproject_mealmatchapp.Restaurant.Resturant_Callbacks.CallBack_OpenImage
import com.example.finalproject_mealmatchapp.Restaurant.Resturant_Callbacks.CallBack_UpdateDonation
import com.example.finalproject_mealmatchapp.Restaurant.Resturant_Callbacks.CallBack_removeDonation
import com.example.finalproject_mealmatchapp.Utilities.ImageLoader
import com.example.finalproject_mealmatchapp.databinding.HorizontalDonationItemBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference




class AddDonationAdapter (
     var donations: List<Donation> = emptyList(),
    var restaurant_name :String = ""
) : RecyclerView.Adapter<AddDonationAdapter.DonationViewHolder>() {


    var updateDonationCallback : CallBack_UpdateDonation? =null
    var removeDoantionCallback : CallBack_removeDonation?= null
    var openImageCallback : CallBack_OpenImage?= null




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val binding = HorizontalDonationItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DonationViewHolder(binding)
    }

    fun updateDonations(newDonations: List<Donation>) {
        donations = newDonations
        notifyDataSetChanged() // Notify RecyclerView of dataset change
    }


    override  fun onBindViewHolder(holder: AddDonationAdapter.DonationViewHolder, position: Int) {
        val storage = FirebaseStorage.getInstance()
        with(holder){
            with(donations.get(position)){
                Log.d("VALUES:" , "$restaurant_name $title")
//                val storageReference =  storage.reference
//                    .child("restaurants")
//                    .child(restaurant_name)
//                    .child("$title.jpg")// Example path
//                    ImageLoader.getInstance().loadFromFirebaseStorage(storageReference,binding.donationIMGIcon)
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



    inner class DonationViewHolder(val binding: HorizontalDonationItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.donationBTNUpdate.setOnClickListener {
                if (updateDonationCallback != null)
                    updateDonationCallback!!.updateDonation(
                        getItem(adapterPosition),
                        adapterPosition
                    )
            }

            binding.donationBTNRemove.setOnClickListener{
                if(removeDoantionCallback != null){
                    removeDoantionCallback!!.removeDonation(
                        getItem(adapterPosition),
                        adapterPosition
                    )
                }

            }

            binding.donationBTNViewImage.setOnClickListener {
                if(openImageCallback != null){
                    openImageCallback!!.openImage(
                        getItem(adapterPosition),
                        adapterPosition
                    )
                }
            }

        }
    }
}