package com.example.finalproject_mealmatchapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_mealmatchapp.Models.Notification
import com.example.finalproject_mealmatchapp.Models.RestaurantUser
import com.example.finalproject_mealmatchapp.Organization.Organization_Callbacks.CallBack_selectRestaurant
import com.example.finalproject_mealmatchapp.databinding.HorizontalNotificationItemBinding
import com.example.finalproject_mealmatchapp.databinding.HorizontalRestaurantItemBinding

class NotificationAdapter(
    var notifications: List<Notification> = emptyList()
): RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {
        val binding = HorizontalNotificationItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.NotificationViewHolder, position: Int) {
        with(holder){
            with(notifications.get(position)){
                binding.notificationLBLTitle.text = title
                binding.notificationLBLBody.text = body
                binding.notificationLBLTimeForPickUp.text = time
            }
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }




    inner class NotificationViewHolder(val binding: HorizontalNotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
//        init {
//            binding.restaurantBTNSelect.setOnClickListener {
//                if (callBack_selectRestaurant != null)
//                    callBack_selectRestaurant!!.selectRestaurant(
//                        getItem(adapterPosition),
//                        adapterPosition
//                    )
//            }
//        }
    }


}

