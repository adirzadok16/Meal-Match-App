package com.example.finalproject_mealmatchapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_mealmatchapp.Models.Order
import com.example.finalproject_mealmatchapp.databinding.HorizontalOrderhistoryItemBinding

class OrderAdapter(
    var orders: List<Order> = emptyList()
): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        val binding = HorizontalOrderhistoryItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderAdapter.OrderViewHolder, position: Int) {
        with(holder){
            with(orders.get(position)){
              binding.orderLBLTitle.text = "Restaurant name : $restaurant"
                binding.orderLBLBody.text = "Order : $item x$amount"
                binding.orderLBLTime.text = "Time : $time"

            }
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }




    inner class OrderViewHolder(val binding: HorizontalOrderhistoryItemBinding) :
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

