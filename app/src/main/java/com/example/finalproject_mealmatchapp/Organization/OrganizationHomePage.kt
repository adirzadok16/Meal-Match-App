package com.example.finalproject_mealmatchapp.Organization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_mealmatchapp.Adapters.OrderAdapter
import com.example.finalproject_mealmatchapp.Login_And_Registration.LoginActivity
import com.example.finalproject_mealmatchapp.Models.Order
import com.example.finalproject_mealmatchapp.Organization.Organization_Callbacks.Callback_SearchRestaurantItemClicked
import com.example.finalproject_mealmatchapp.Organization.Organization_Fragments.RestaurantListFrangment
import com.example.finalproject_mealmatchapp.Organization.Organization_Fragments.SearchRestaurantFragment
import com.example.finalproject_mealmatchapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class OrganizationHomePage : AppCompatActivity() {

    private lateinit var main_FRAME_search : FrameLayout
    private lateinit var main_FRAME_restaurantList : FrameLayout
    private lateinit var searchRestaurantFragment : SearchRestaurantFragment
    private lateinit var restaurantListFragment : RestaurantListFrangment
    private lateinit var main_BTN_LogOut : MaterialButton
    private lateinit var main_LBL_headline : MaterialTextView
    private  lateinit var main_RV_orderList : RecyclerView
    private  lateinit var main_BTN_MyOrders : MaterialButton
    private lateinit var organizationRef : DatabaseReference
    var database: FirebaseDatabase = Firebase.database
    var organizationUserName :String = ""
    var orderAdapter = OrderAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_organization)
        enableEdgeToEdge()
        val bundle: Bundle? = intent.extras
        organizationUserName = bundle?.getString("user_name").toString()
        findViews()
        initViews()
        bringOrdersFromFirebaseToOrderAdapter()
    }

//    override fun onResume() {
//        super.onResume()
//        bringOrdersFromFirebaseToOrderAdapter() // Fetch the orders again
//    }

    private fun findViews() {
        main_FRAME_search = findViewById(R.id.main_FRAME_search)
        main_FRAME_restaurantList = findViewById(R.id.main_FRAME_restaurantList)
        main_BTN_LogOut = findViewById(R.id.main_BTN_LogOut)
        main_RV_orderList = findViewById(R.id.main_RV_orderList)
        main_BTN_MyOrders = findViewById(R.id.main_BTN_MyOrders)
        main_LBL_headline = findViewById(R.id.main_LBL_headline)
    }

    private fun initViews() {
        searchRestaurantFragment = SearchRestaurantFragment()
        organizationRef = database.getReference("organization_users").child(organizationUserName)
        searchRestaurantFragment.callbackSearchRestaurantItemClicked = object :
            Callback_SearchRestaurantItemClicked {
                override fun searchRestaurant(restaurantName:String){
                    restaurantListFragment.showRestaurants(restaurantName,this@OrganizationHomePage)
                }
            }

        main_BTN_MyOrders.setOnClickListener { WindowActions() }



//        searchRestaurantFragment.callback_returnToLoginActivity = object :
//            CallBack_returnToLoginActivity {
//            override fun returnToLoginActivity() {
//              var intent =Intent(this@OrganizationHomePage,LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }

        restaurantListFragment = RestaurantListFrangment()
        restaurantListFragment.organizationUserName = this.organizationUserName

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_search, searchRestaurantFragment)
            .commit()


        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_restaurantList, restaurantListFragment)
            .commit()

        main_BTN_LogOut.setOnClickListener{returnToLoginActivity()}


    }

    private fun WindowActions() {

//        private fun moveToNotifications() {
//        val intent = Intent(this,RestaurantNotifications::class.java)
//        val bundle = Bundle()
//        bundle.putString("user_name",restaurantUserName)
//        intent.putExtras(bundle)
//        startActivity(intent)
//        finish()
            if(main_RV_orderList.visibility == View.VISIBLE) {
                main_RV_orderList.visibility = View.GONE
                main_FRAME_search.visibility = View.VISIBLE
                main_FRAME_restaurantList.visibility = View.VISIBLE
                main_LBL_headline.visibility = View.VISIBLE

            } else if(main_RV_orderList.visibility == View.GONE) {
//                bringOrdersFromFirebaseToOrderAdapter()
                main_RV_orderList.visibility = View.VISIBLE
                main_FRAME_search.visibility = View.GONE
                main_FRAME_restaurantList.visibility = View.GONE
                main_LBL_headline.visibility = View.GONE

            }
        }
//    }

    private fun returnToLoginActivity() {
        val intent =Intent(this,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    private fun bringOrdersFromFirebaseToOrderAdapter() {
        Log.d("ORDER LIST","HERE!!!!!!!!!!!!")
        // Set up the RecyclerView with the adapter
        main_RV_orderList.adapter = orderAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        main_RV_orderList.layoutManager = linearLayoutManager

        // Fetch notifications from Firebase
        organizationRef.child("orderHistory").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val orderList = mutableListOf<Order>()

                for (orderSnapshot in dataSnapshot.children) {
                    val order = orderSnapshot.getValue<Order>()
                    if (order != null) {
                        orderList.add(order)
                    }
                }

                // Sort the notifications by the 'time' property in descending order (latest first)
                val sortedNotifications = orderList.sortedByDescending { it.time }

                // Update adapter with sorted notifications
                orderAdapter.orders = sortedNotifications
//                main_RV_orderList.visibility = View.VISIBLE
                orderAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors, such as permission issues
            }
        })
    }
}