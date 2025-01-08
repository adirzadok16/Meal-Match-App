package com.example.finalproject_mealmatchapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_mealmatchapp.Models.RestaurantUser
import com.example.finalproject_mealmatchapp.Organization.Organization_Callbacks.CallBack_selectRestaurant
import com.example.finalproject_mealmatchapp.databinding.HorizontalRestaurantItemBinding

class RestaurantAdapter (
    var restaurants: List<RestaurantUser> = emptyList()
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {


    var callBack_selectRestaurant : CallBack_selectRestaurant? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantViewHolder {
        val binding = HorizontalRestaurantItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantAdapter.RestaurantViewHolder, position: Int) {
        with(holder){
            with(restaurants.get(position)){
                binding.restaurantLBLTitle.text = name
                binding.restaurantLBLType.text = type
                binding.restaurantLBLAddress.text = address
                binding.restaurantLBLPhoneNumber.text = phoneNumner
                binding.openHours.setText("open : $open")
                binding.closeHours.setText("clsoe : $close")
            }
        }
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    fun getItem(position: Int) = restaurants.get(position)


    inner class RestaurantViewHolder(val binding: HorizontalRestaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.restaurantBTNSelect.setOnClickListener {
                if (callBack_selectRestaurant != null)
                    callBack_selectRestaurant!!.selectRestaurant(
                        getItem(adapterPosition),
                        adapterPosition
                    )
            }
        }
   }


}

