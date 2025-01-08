package com.example.finalproject_mealmatchapp.Models

data class RestaurantUser(
    var name: String = "",
    var email: String = "",
    var phoneNumner: String = "",
    var password: String = "",
    var address: String = "",
    var type: String = "",
    var open : String = "",
    var close :String = "",
    val id: Int = getNextId() // Assign a unique ID to each object
) : Comparable<RestaurantUser> {

    override fun compareTo(other: RestaurantUser): Int {
        return this.name.compareTo(other.name)
    }

    companion object {
        private var idCounter: Int = 0

        private fun getNextId(): Int {
            return ++idCounter
        }
    }

//    fun getId() : Int{
//        return this.id
//    }


}
