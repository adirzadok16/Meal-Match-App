package com.example.finalproject_mealmatchapp.Models


data class OrganizationUser(
    var name: String = "",
    var email: String = "",
    var phoneNumner: String = "",
    var password: String = "",
    var address: String = "",
    val id: Int = getNextId() // Assign a unique ID to each object
) : Comparable<OrganizationUser> {

    override fun compareTo(other: OrganizationUser): Int {
        return this.name.compareTo(other.name)
    }

    companion object {
        private var idCounter: Int = 0

        private fun getNextId(): Int {
            return ++idCounter
        }
    }
}