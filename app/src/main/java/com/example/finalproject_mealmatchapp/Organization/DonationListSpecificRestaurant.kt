package com.example.finalproject_mealmatchapp.Organization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_mealmatchapp.Adapters.PickDonationAdapter
import com.example.finalproject_mealmatchapp.Login_And_Registration.LoginActivity
import com.example.finalproject_mealmatchapp.Models.Donation
import com.example.finalproject_mealmatchapp.Models.Notification
import com.example.finalproject_mealmatchapp.Models.Order
import com.example.finalproject_mealmatchapp.Organization.Organization_Callbacks.CallBack_pickUpDonation
import com.example.finalproject_mealmatchapp.R
import com.example.finalproject_mealmatchapp.Utilities.DB_Manager
import com.example.finalproject_mealmatchapp.Utilities.SharedPreferencesManagerV3
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class DonationListSpecificRestaurant : AppCompatActivity() {

    private lateinit var main_BTN_close : MaterialButton
    private lateinit var main_RV_donationList : RecyclerView
    private lateinit var donation_BTN_sendToRestaurant : MaterialButton
    private lateinit var main_CARD_PickUpDonation: CardView
    private lateinit var donation_TIE_PickUpAmount : TextInputEditText
    private lateinit var donation_Spinner_PickUpTime : Spinner
    private lateinit var main_BTN_return_button : AppCompatImageButton
    private lateinit var main_BTN_LogOut : MaterialButton
    var restaurantUserName : String = ""
    var organizationUserName : String = ""
    var positionDonation : Int = 0
    var donationPicked : Donation? = null
    var donationAdapter = PickDonationAdapter()
    private lateinit var restaurantRef : DatabaseReference
    val storage = FirebaseStorage.getInstance()
    private var db_manager = DB_Manager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_donation_list_specific_restaurant)
        val bundle: Bundle? = intent.extras
        restaurantUserName = bundle?.getString("restaurant_user_name").toString()
        organizationUserName = bundle?.getString("organization_user_name").toString()
        Log.d("ORGANIZATION CHECK",organizationUserName)
        restaurantRef = db_manager.initDataBaseToRestaurant(restaurantUserName)
        findViews()
        initViews()
        getListOfDonationFromFireBase()
        setupAdapter()
    }


    private fun findViews() {
        main_BTN_close = findViewById(R.id.main_BTN_close)
        main_CARD_PickUpDonation = findViewById(R.id.main_CARD_PickUpDonation)
        donation_BTN_sendToRestaurant = findViewById(R.id.donation_BTN_sendToRestaurant)
        main_RV_donationList= findViewById(R.id.main_RV_donationList)
        donation_TIE_PickUpAmount= findViewById(R.id.donation_TIE_PickUpAmount)
        donation_Spinner_PickUpTime= findViewById(R.id.donation_Spinner_PickUpTime)
        main_BTN_return_button = findViewById(R.id.main_BTN_return_button)
        main_BTN_LogOut =  findViewById(R.id.main_BTN_LogOut)
    }

    private fun initViews() {
        main_BTN_close.setOnClickListener{donationWindowChanges("close")}
        donation_BTN_sendToRestaurant.setOnClickListener{sendDonationStatusToRestaurant()}
        main_BTN_return_button.setOnClickListener{returnToSearchRestaurantActivity()}
        main_BTN_LogOut.setOnClickListener{returnToLoginActivity()}
        setUpPickUpHours()
    }




    private fun returnToLoginActivity() {
        SharedPreferencesManagerV3.getInstance().putString("isLoggedIn","false")
        val intent = Intent(this, LoginActivity::class.java)
            // Add these flags to clear the back stack
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
            finish()
    }

    private fun returnToSearchRestaurantActivity() {
        val intent = Intent(this, OrganizationHomePage::class.java)
        val bundle = Bundle()
        bundle.putString("user_name", organizationUserName)
        bundle.putString("notFirstTime","yes")
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun sendDonationStatusToRestaurant() {
        Log.d("DONATION NAME CHECKK!!" , donationPicked?.title.toString())
        restaurantRef
            .child("donationList")
            .orderByChild("title")
            .equalTo(donationPicked?.title)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("SNAP CHECK!!!!" , snapshot.toString())
                    handleDonationUpdate(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("ERROR", "Failed to read value.", error.toException())
                }
            })
    }

    private fun handleDonationUpdate(snapshot: DataSnapshot) {
        for (donationSnapshot in snapshot.children) {
            Log.d("SNAP CHECK2!!!!" , donationSnapshot.toString())
            Log.d("snapshot.children",snapshot.children.toString())

            val amount = donationSnapshot.child("amount").getValue(Int::class.java)
            val pickUpAmount = donation_TIE_PickUpAmount.text.toString().toIntOrNull()

            if (amount != null && pickUpAmount != null) {
                val newAmount = amount - pickUpAmount
                Log.d("NEW AMOUNT",newAmount.toString())
                when {
                    newAmount == 0 -> {
                        removeFromDonationListInRestaurant()
                        createNewNotificationInRestaurantAndOrganization(pickUpAmount.toString())
                        donationWindowChanges("close")
                    }
                    newAmount < 0 -> NumberIsNotInRightRangeErrorAlert()
                    else -> {
                        // Update the amount in Firebase
                        donationSnapshot.ref.child("amount").setValue(newAmount)
                            .addOnSuccessListener {
                                donationAdapter.donations[positionDonation].amount = newAmount
                                donationAdapter.notifyItemChanged(positionDonation)
                                createNewNotificationInRestaurantAndOrganization(pickUpAmount.toString())
                                donationWindowChanges("close")
                            }
                            .addOnFailureListener { error ->
                                Log.e("FIREBASE", "Failed to update amount: ${error.message}")
                            }
                    }
                }
            } else {
                Log.w("INPUT_ERROR", "Invalid amount or pick-up value")
            }
        }
    }
    private fun createNewNotificationInRestaurantAndOrganization(pickUpAmount: String) {
        val currentDate = Date()
        // Correctly format the current date and time
        val time = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(currentDate)

        val title: String = "$organizationUserName On The Way To You!"
        Log.d("TITLE CHECK", title)

        val timeForPicking = donation_Spinner_PickUpTime.selectedItem.toString()


        val body: String = "The $organizationUserName comes to receive the following donation:\n" +
                " Name : ${donationPicked?.title}\n Amount : $pickUpAmount\n Time: $timeForPicking "

        // Create a new notification with the formatted time
        val newNotification = Notification(
            title = title,
            body = body,
            time = time
        )

        val newOrder = Order(
            restaurant = restaurantUserName,
            item = donationPicked!!.title,
            amount = pickUpAmount.toInt(),
            time = time
        )

        // Add the new notification to the database
        db_manager.addNotificationToRestaurant(newNotification, restaurantUserName)
        db_manager.addOrderToHistory(newOrder,organizationUserName)

    }


    private fun NumberIsNotInRightRangeErrorAlert() {
            MaterialAlertDialogBuilder(this)
                .setTitle("Invalid Number")
                .setMessage("The number you chose is not in the valid range")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun removeFromDonationListInRestaurant() {
        restaurantRef.child("donationList").child(donationPicked?.title.toString()).removeValue()
            .addOnSuccessListener {
                // Successfully deleted

                // Remove the donation from the local list in the adapter
                val updatedDonations = donationAdapter.donations.toMutableList()
                Log.d("LOOK!!!!",updatedDonations.toString())
                updatedDonations.removeAt(positionDonation)

                // Update the adapter with the new list
                donationAdapter.donations = updatedDonations

                // Notify the adapter to update the RecyclerView
                donationAdapter.notifyDataSetChanged()
                val photoRef: StorageReference = storage.reference.child("restaurants/$restaurantUserName/${donationPicked?.title}")
                photoRef.delete()
                    .addOnSuccessListener {
                        // File deleted successfully
                        Log.d("remove image from storage", "File successfully deleted!")
                    }
                    .addOnFailureListener { exception ->
                        // If an error occurred during deletion
                        Log.e("remove image from storage", "Error deleting file: ${exception.message}")
                    }


                Toast.makeText(this,"Donation deleted successfully", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.e("remove donation from database", "Failed to delete donation: ${exception.message}")
                Toast.makeText(this,"Failed to delete donation: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun donationWindowChanges(action: String) {
        if(action == "open"){
            main_CARD_PickUpDonation.visibility = View.VISIBLE
        }
        if(action == "close"){
            main_CARD_PickUpDonation.visibility = View.GONE
            donation_TIE_PickUpAmount.setText("")

        }
    }

    private fun setupAdapter() {
        Log.d("ADAPTER LIST",donationAdapter.donations.toString())
        donationAdapter.callBack_pickUpDination = object : CallBack_pickUpDonation{
            override fun pickUpDination(donation: Donation, position: Int) {
                donationWindowChanges("open")
                positionDonation = position
                donationPicked = donation
            }
        }
        main_RV_donationList.adapter = donationAdapter
        donationAdapter.restaurant_name = restaurantUserName
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        main_RV_donationList.layoutManager = linearLayoutManager
    }


    private fun getListOfDonationFromFireBase() {
//        val restaurantRef = initDataBaseToRestaurant(userName)
        restaurantRef.child("donationList").addListenerForSingleValueEvent(object :
            ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val donationsList = mutableListOf<Donation>()
                for (donationSnapshot in dataSnapshot.children) {
                    val donation = donationSnapshot.getValue<Donation>()
                    if (donation != null) {
                        donationsList.add(donation)
                    }
                }
                donationAdapter.donations = donationsList
                donationAdapter.notifyDataSetChanged()
                Log.d("LIST OF DONATIONS", donationAdapter.donations.toString())
            }


            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }

    private fun setUpPickUpHours() {
        restaurantRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Retrieve open and close times
                val openTime = dataSnapshot.child("open").getValue(String::class.java)
                val closeTime = dataSnapshot.child("close").getValue(String::class.java)

                // Check if open and close times are available
                if (openTime != null && closeTime != null) {
                    // Call the function to populate the spinner with times
                    makeSpinnerWithTimes(donation_Spinner_PickUpTime, openTime, closeTime)
                } else {
                    Log.w("Firebase", "Open and/or close time not found.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "Failed to read value.", error.toException())
            }
        })
    }

    private fun makeSpinnerWithTimes(spinner: Spinner, open: String, close: String) {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        // Convert the open and close times from strings to LocalTime objects
        val openTime = LocalTime.parse(open, timeFormatter)
        val closeTime = LocalTime.parse(close, timeFormatter)

        // Get the current time rounded up to the next 30-minute interval
        val now = LocalTime.now()
        val roundedNow = if (now.minute % 30 == 0) {
            now.withSecond(0).withNano(0)
        } else {
            now.withSecond(0).withNano(0).plusMinutes((30 - now.minute % 30).toLong())
        }

        // List to store the time intervals
        val timeList = mutableListOf<String>()

        // Start generating times from the later of openTime or roundedNow
        var currentTime = if (roundedNow.isBefore(openTime)) openTime else roundedNow
        while (!currentTime.isAfter(closeTime)) {
            timeList.add(currentTime.format(timeFormatter))
            currentTime = currentTime.plusMinutes(30) // Add 30 minutes
        }

        // Create a custom ArrayAdapter with a custom layout for the spinner items
        val adapter = object : ArrayAdapter<String>(spinner.context, R.layout.spinner_item, timeList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.spinnerItemText)
                textView.setTextColor(ContextCompat.getColor(context, R.color.black)) // Change selected item color
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_green))
                val textView = view.findViewById<TextView>(R.id.spinnerItemText)
                textView.setTextColor(ContextCompat.getColor(context, R.color.black)) // Change dropdown items color
                return view
            }
        }

        // Apply the adapter to the Spinner
        spinner.adapter = adapter
    }


}