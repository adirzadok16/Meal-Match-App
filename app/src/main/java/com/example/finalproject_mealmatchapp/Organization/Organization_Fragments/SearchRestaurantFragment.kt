package com.example.finalproject_mealmatchapp.Organization.Organization_Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalproject_mealmatchapp.Login_And_Registration.LoginActivity
import com.example.finalproject_mealmatchapp.Organization.Organization_Callbacks.CallBack_returnToLoginActivity
import com.example.finalproject_mealmatchapp.Organization.Organization_Callbacks.Callback_SearchRestaurantItemClicked
import com.example.finalproject_mealmatchapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class SearchRestaurantFragment : Fragment() {

    private lateinit var main_TIE_searchRestaurant : TextInputEditText
    private lateinit var main_BTN_search : MaterialButton
    var callbackSearchRestaurantItemClicked : Callback_SearchRestaurantItemClicked? = null
    var callback_returnToLoginActivity : CallBack_returnToLoginActivity?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_search_restaurant, container, false)
        findViews(v)
        initViews(v)
        return v
    }

    private fun findViews(v : View) {
        main_TIE_searchRestaurant = v.findViewById(R.id.main_TIE_searchRestaurant)
        main_BTN_search = v.findViewById(R.id.main_BTN_search)

    }

    private fun initViews(v : View) {
        main_BTN_search.setOnClickListener {itemClicked()}

    }


    private fun itemClicked() {
        val restaurantName : String = main_TIE_searchRestaurant.text.toString()
        callbackSearchRestaurantItemClicked?.searchRestaurant(restaurantName)
    }


}