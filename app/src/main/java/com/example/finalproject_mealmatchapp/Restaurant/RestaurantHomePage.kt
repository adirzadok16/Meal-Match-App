package com.example.finalproject_mealmatchapp.Restaurant

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_mealmatchapp.Adapters.AddDonationAdapter
import com.example.finalproject_mealmatchapp.Adapters.NotificationAdapter
import com.example.finalproject_mealmatchapp.Login_And_Registration.LoginActivity
import com.example.finalproject_mealmatchapp.Models.Donation
import com.example.finalproject_mealmatchapp.Models.Notification
import com.example.finalproject_mealmatchapp.R
import com.example.finalproject_mealmatchapp.Restaurant.Resturant_Callbacks.CallBack_OpenImage
import com.example.finalproject_mealmatchapp.Restaurant.Resturant_Callbacks.CallBack_UpdateDonation
import com.example.finalproject_mealmatchapp.Restaurant.Resturant_Callbacks.CallBack_removeDonation
import com.example.finalproject_mealmatchapp.Utilities.DB_Manager
import com.example.finalproject_mealmatchapp.Utilities.ImageLoader
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class RestaurantHomePage : AppCompatActivity() {

    // DB variables
    private var db_manager = DB_Manager()
    private lateinit var restaurantRef : DatabaseReference
    var adapter = AddDonationAdapter()
    var notificationAdapter = NotificationAdapter()
    // View
    private lateinit var main_RV_donationList : RecyclerView
    private lateinit var main_FAB_insertNewDonation : FloatingActionButton
    private lateinit var main_BTN_LogOut : MaterialButton
    private lateinit var main_LBL_headline : MaterialTextView
    // Card View
    private lateinit var main_BTN_close : MaterialButton
    private lateinit var main_CARD_newDonation : CardView
    private lateinit var donation_BTN_update : MaterialButton
    private lateinit var donation_LBL_headline : MaterialTextView
    private lateinit var donation_BTN_add : MaterialButton
    private lateinit var donation_TIE_title : TextInputEditText
    private lateinit var donation_TIE_amount : TextInputEditText
    private lateinit var donation_TIE_description : TextInputEditText
    private lateinit var donation_BTN_addPhoto : MaterialButton

    // Notifocation
    private lateinit var main_BTN_Notification : MaterialButton
    private lateinit var main_RV_notificationList : RecyclerView
    private lateinit var main_MTV_notificationalert : MaterialTextView

    // Welcome Card
    private lateinit var main_CARD_welcomeCard : CardView
    private lateinit var main_MTV_welcomeText :  MaterialTextView
    private lateinit var main_BTN_closeWelcomeScreen : MaterialButton

    // Showing Image

    private lateinit var main_CARD_imageView :CardView
    private lateinit var main_IV_imegeView : ShapeableImageView

    //Update Amount Card
    private lateinit var main_BTN_closeUpdateScreen : MaterialButton
    private lateinit var main_CARD_UpdateAmount : CardView
    private lateinit var donation_TIE_UpdateAmount: TextInputEditText

    var positionDonation : Int = 0
    var donationPicked : Donation? = null
    var restaurantUserName :String = ""
    var NewNotificationCount = 0
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_restaurant)
        val bundle: Bundle? = intent.extras
        restaurantUserName = bundle?.getString("user_name").toString()
        restaurantRef = db_manager.initDataBaseToRestaurant(restaurantUserName)
        findViews()
        initViews()
        updateNotificationNumber()
//        bringDonationsFromFireBaseToAdapter()
//        bringNotificationsFromFirebaseToAdapter()
    }



    private fun updateNotificationNumber() {
        restaurantRef.child("notificationList").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (notificationSnapshot in dataSnapshot.children) {
                    val notification = notificationSnapshot.getValue<Notification>()
                    if (notification != null) {
                        if (notification.new) {
                            main_MTV_notificationalert.visibility = View.VISIBLE
                            NewNotificationCount += 1
                            notification.new = false
                            // Update the notification in the database
                            notificationSnapshot.ref.setValue(notification)
                        }
                    }
                }

                // Perform further actions with notificationsList or num if needed
                // For example: update a UI element with the new num value
                if(NewNotificationCount ==0){
                    main_BTN_Notification.text = ""
                } else {
                    main_BTN_Notification.text = NewNotificationCount.toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if needed
                Log.e("FirebaseError", "Failed to read notifications: ${databaseError.message}")
            }
        })
    }

    private fun findViews() {
        // View
        main_RV_donationList = findViewById(R.id.main_RV_donationList)
        main_FAB_insertNewDonation = findViewById(R.id.main_FAB_insertNewDonation)
        main_BTN_LogOut = findViewById(R.id.main_BTN_LogOut)
        main_LBL_headline = findViewById(R.id.main_LBL_headline)

        // Card View
        main_CARD_newDonation = findViewById(R.id.main_CARD_newDonation)
        main_BTN_close = findViewById(R.id.main_BTN_close)
        donation_BTN_update = findViewById(R.id.donation_BTN_update)
        donation_LBL_headline = findViewById(R.id.donation_LBL_headline)
        donation_BTN_add = findViewById(R.id.donation_BTN_add)
        donation_TIE_title = findViewById(R.id.donation_TIE_title)
        donation_TIE_amount = findViewById(R.id.donation_TIE_amount)
        donation_TIE_description = findViewById(R.id.donation_TIE_description)
        donation_BTN_addPhoto = findViewById(R.id.donation_BTN_addPhoto)

        // Notifocation
        main_BTN_Notification = findViewById(R.id.main_BTN_Notification)
        main_RV_notificationList = findViewById(R.id.main_RV_notificationList)
        main_MTV_notificationalert =  findViewById(R.id.main_MTV_notificationalert)

        // Welcome Card
        main_CARD_welcomeCard = findViewById(R.id.main_CARD_welcomeCard)
        main_MTV_welcomeText = findViewById(R.id.main_MTV_welcomeText)
        main_BTN_closeWelcomeScreen = findViewById(R.id.main_BTN_closeWelcomeScreen)


        // Showing Image
        main_CARD_imageView= findViewById(R.id.main_CARD_imageView)
        main_IV_imegeView= findViewById(R.id.main_IV_imegeView)

        //Update Amount Card
        main_BTN_closeUpdateScreen= findViewById(R.id.main_BTN_closeUpdateScreen)
        main_CARD_UpdateAmount = findViewById(R.id.main_CARD_UpdateAmount)
        donation_TIE_UpdateAmount= findViewById(R.id.donation_TIE_UpdateAmount)

    }

    private fun initViews() {
        main_FAB_insertNewDonation.setOnClickListener{donationWindowChanges("open")}
        main_BTN_close.setOnClickListener{donationWindowChanges("close")}
        donation_BTN_add.setOnClickListener{createNewDonation()}
        donation_BTN_update.setOnClickListener{updateDonationClicked()}
        main_BTN_LogOut.setOnClickListener{returnToLoginActivity()}
        donation_BTN_addPhoto.setOnClickListener{uploadPhotoToFirebaseStorage()}
        main_BTN_Notification.setOnClickListener { moveToNotifications() }
        main_MTV_welcomeText.text = "Welcome ${this.restaurantUserName}"
        main_BTN_closeWelcomeScreen.setOnClickListener {
            main_CARD_welcomeCard.visibility = View.GONE
            main_RV_donationList.visibility = View.VISIBLE
            bringDonationsFromFireBaseToAdapter()
//            getNotificationCount()
        }

        main_IV_imegeView.setOnClickListener{
            main_CARD_imageView.visibility = View.GONE
            main_IV_imegeView.setImageDrawable(null)
        }

        main_BTN_closeUpdateScreen.setOnClickListener {
            main_CARD_UpdateAmount.visibility = View.GONE

        }
//        main_BTN_NotificationClose.setOnClickListener{notificationWindowChanges("close")}
//        donation_BTN_pickUp.setOnClickListener{}

    }

//        private fun getNotificationCount() {
//
//            val notificationCountRef = restaurantRef.child("newNotificationCount")
//
//            notificationCountRef.get().addOnSuccessListener { snapshot ->
//                if (snapshot.exists()) {
//                    val notificationCount = snapshot.getValue(Int::class.java) ?: 0
//                    main_BTN_Notification.text = notificationCount.toString()
//                    notificationCountRef.setValue(0)
//                } else {
//                    Log.d("Firebase ERROR", "Notification count does not exist.")
//                }
//            }.addOnFailureListener { e ->
//                Log.e("Firebase ERROR", "Error fetching notification count", e)
//            }
//        }

    private fun moveToNotifications() {
//        val intent = Intent(this,RestaurantNotifications::class.java)
//        val bundle = Bundle()
//        bundle.putString("user_name",restaurantUserName)
//        intent.putExtras(bundle)
//        startActivity(intent)
//        finish()
        if(main_RV_notificationList.visibility == View.VISIBLE) {
            main_RV_notificationList.visibility = View.GONE
            main_RV_donationList.visibility = View.VISIBLE
            main_LBL_headline.visibility = View.VISIBLE



        } else if(main_RV_notificationList.visibility == View.GONE) {
            bringNotificationsFromFirebaseToNotificationAdapter()
            main_RV_notificationList.visibility = View.VISIBLE
            main_RV_donationList.visibility = View.GONE
            main_LBL_headline.visibility = View.GONE
            NewNotificationCount = 0
            main_BTN_Notification.text = ""


        }
    }

    private fun uploadPhotoToFirebaseStorage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun returnToLoginActivity() {
      val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
        }
    }

    private fun uploadImageToFirebase(photoName : String) {
        if (imageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference.child("restaurants/$restaurantUserName/$photoName.jpg")
            val uploadTask = storageRef.putFile(imageUri!!)

            uploadTask.addOnSuccessListener {
                Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }


    private fun createNewDonation() {
        val donation_title = donation_TIE_title.text.toString()
        val donation_amonut = donation_TIE_amount.text.toString()
        val donation_description = donation_TIE_description.text.toString()
        val fieldsCheck = checkFields(donation_title,donation_amonut,donation_description,imageUri)
        if(!fieldsCheck){
            Toast.makeText(this, "You need to fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        if(imageUri == null){
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            return
        } else{
            uploadImageToFirebase(donation_title)
        }

        val new_donation  = Donation(
            title = donation_title,
            amount = donation_amonut.toInt(),
            description = donation_description,
        )
        db_manager.addDonationToRestaurant(new_donation,restaurantUserName,this)

        // Close the new donation video and clear fields
        donationWindowChanges("close")
        clearFields()
        restaurantRef.child("donationList").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Map each snapshot to a Donation object
                val donations = snapshot.children.mapNotNull { it.getValue(Donation::class.java) }

                // Update the adapter directly
                adapter.updateDonations(donations)
            } else {
                // If no donations exist, clear the adapter
                adapter.updateDonations(emptyList())
            }
        }.addOnFailureListener { error ->
            Log.e("Firebase", "Error fetching donations: ", error)
            adapter.updateDonations(emptyList()) // Handle error by clearing adapter
        }

//        // Update adapter list
//        val updatedDonations = addDonationAdapter.donations.toMutableList()
//        updatedDonations.add(new_donation)
//        addDonationAdapter.donations = updatedDonations
//        getListOfDonationFromFireBase()
//
//        // Notify the adapter to update the RecyclerView
//        addDonationAdapter.notifyDataSetChanged()

    }

    private fun checkFields(title: String, amonut: String, description: String, imageUri: Uri?) : Boolean {
       if(title.replace(" ","").equals("")
           || amonut.replace(" ","").equals("")
           || description.replace(" ","").equals("") ){
           return false
       } else{
           return true
       }
    }


    private fun clearFields(){
        // Clear input fields
        donation_TIE_title.setText("")
        donation_TIE_amount.setText("")
        donation_TIE_description.setText("")
    }
    private fun deleteDonation() {
//        val restaurantRef = initDataBaseToRestaurant(userName)
        restaurantRef.child("donationList").child(donationPicked!!.title).removeValue()
            .addOnSuccessListener {
                removeImageFromStorage(donationPicked!!.title)
                restaurantRef.child("donationList").get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        // Map each snapshot to a Donation object
                        val donations = snapshot.children.mapNotNull { it.getValue(Donation::class.java) }

                        // Update the adapter directly
                        adapter.updateDonations(donations)
                    } else {
                        // If no donations exist, clear the adapter
                        adapter.updateDonations(emptyList())
                    }
                }.addOnFailureListener { error ->
                    Log.e("Firebase", "Error fetching updated donations: ", error)
                    adapter.updateDonations(emptyList()) // Handle error by clearing adapter
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.e("Firebase", "Failed to delete donation: ${exception.message}")
                Toast.makeText(this,"Failed to delete donation: ${exception.message}",Toast.LENGTH_SHORT).show()
            }

    }

    private fun removeImageFromStorage(donationTitle: String) {
        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference

// Specify the path to the file you want to delete
        val fileRef = storageRef.child("restaurants/$restaurantUserName/$donationTitle.jpg")

// Delete the file
        fileRef.delete()
            .addOnSuccessListener {
                // File deleted successfully
                println("File deleted successfully!")
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur
                println("Error deleting file: ${exception.message}")
            }
    }

    private fun updateDonationClicked() {
        setValuesInFireBase()
        // donationPicked!!.title = donation_TIE_title.text.toString()
        // if(donation_TIE_amount.text.toString().isNotEmpty()){
        //    donationPicked!!.amount = Integer.parseInt( donation_TIE_amount.text.toString())
        // }
        // donationPicked!!.description = donation_TIE_description.text.toString()

        restaurantRef = db_manager.initDataBaseToRestaurant(restaurantUserName)
        restaurantRef.child("donationList").addValueEventListener(object :
            ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val donationsList = mutableListOf<Donation>()
                // This method is called every time the data at this location changes.
                for (donationSnapshot in dataSnapshot.children) {
                    val donation = donationSnapshot.getValue<Donation>()
                    if (donation != null) {
                        donationsList.add(donation)
                    }
                }
                // Update the adapter with the latest dataa
                adapter.donations = donationsList
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })

        donation_TIE_UpdateAmount.setText("")
        main_CARD_UpdateAmount.visibility = View.GONE
    }

    private fun setValuesInFireBase() {
//        val restaurantRef = initDataBaseToRestaurant(userName)
        //Update donation title
//        val title = donation_TIE_title.text.toString();
        val amount = donation_TIE_UpdateAmount.text.toString();
//        val description = donation_TIE_title.text.toString();
//
//        if (title.trim().isNotEmpty()) {
//            restaurantRef.child("donationList")
//                .child(donationPicked!!.title)
//                .child("title")
//                .setValue(title)
//        }
        //Update donation amount
        if(amount.toInt() > 0){
            restaurantRef.child("donationList")
                .child(donationPicked!!.title)
                .child("amount")
                .setValue(amount.toInt())
        }
        //Update donation description

//        if(description.trim().isNotEmpty()){
//            restaurantRef.child("donationList")
//                .child(donationPicked!!.title)
//                .child("description")
//                .setValue(description)
//        }

    }

    private fun bringDonationsFromFireBaseToAdapter(){
       getListOfDonationFromFireBase()
        main_RV_donationList.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        main_RV_donationList.layoutManager = linearLayoutManager
        adapter.updateDonationCallback = object : CallBack_UpdateDonation {
            override fun updateDonation(donation: Donation, position: Int) {
//                donation_BTN_add.visibility = View.GONE
//                donation_BTN_update.visibility = View.VISIBLE
//                donation_LBL_headline.setText("Update Donation")
//                main_CARD_newDonation.visibility = View.VISIBLE
                main_CARD_UpdateAmount.visibility = View.VISIBLE
                positionDonation = position
                donationPicked = donation
                //updateDonationClicked()
            }
        }

        adapter.removeDoantionCallback = object : CallBack_removeDonation {
            override fun removeDonation(donation: Donation, position: Int) {
                main_CARD_newDonation.visibility = View.GONE
                donation_BTN_add.visibility = View.VISIBLE
                donation_BTN_update.visibility = View.GONE
                donation_BTN_addPhoto.visibility = View.GONE
                donationPicked = donation
                deleteDonation()
            }
        }

        adapter.openImageCallback = object : CallBack_OpenImage {
            override fun openImage(donation: Donation, position: Int) {
                main_CARD_imageView.visibility = View.VISIBLE
                val storage = FirebaseStorage.getInstance()
                val storageReference =  storage.reference
                    .child("restaurants")
                    .child(restaurantUserName)
                    .child("${donation.title}.jpg")// Example path
                ImageLoader.getInstance().loadFromFirebaseStorage(storageReference,main_IV_imegeView)
            }
        }
    }

    private fun getListOfDonationFromFireBase() {
        restaurantRef = db_manager.initDataBaseToRestaurant(restaurantUserName)
        restaurantRef.child("donationList").addValueEventListener(object :
            ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val donationsList = mutableListOf<Donation>()
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (donationSnapshot in dataSnapshot.children) {
                    val donation = donationSnapshot.getValue<Donation>()
                    if (donation != null) {
                        donationsList.add(donation)
                    }
                }
//                Log.d("VALUE",donationsList.toString())

//                if (donationsList != null) {
                    adapter.donations = donationsList
                   adapter.restaurant_name = restaurantUserName
                    adapter.notifyDataSetChanged()
                    Log.d("LIST OF DONATIONS",adapter.donations.toString())
                main_RV_donationList.adapter = adapter
                val linearLayoutManager = LinearLayoutManager(this@RestaurantHomePage)
                linearLayoutManager.orientation = RecyclerView.VERTICAL
                main_RV_donationList.layoutManager = linearLayoutManager

//                }
//                else
//                    addDonationAdapter = AddDonationAdapter(emptyList())
            }


            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }



    private fun donationWindowChanges(action: String) {
        if(action == "open"){
//            donation_LBL_headline.setText("New Donation")
//            donation_BTN_addPhoto.visibility = View.VISIBLE
//            donation_BTN_add.visibility =View.VISIBLE
            main_CARD_newDonation.visibility = View.VISIBLE
        }
        if(action == "close"){
            main_CARD_newDonation.visibility = View.GONE
        }
    }



    private fun bringNotificationsFromFirebaseToNotificationAdapter() {
        // Set up the RecyclerView with the adapter
        main_RV_notificationList.adapter = notificationAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        main_RV_notificationList.layoutManager = linearLayoutManager

        // Fetch notifications from Firebase
        restaurantRef.child("notificationList").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val notificationsList = mutableListOf<Notification>()
                for (notificationSnapshot in dataSnapshot.children) {
                    val notification = notificationSnapshot.getValue<Notification>()
                    if (notification != null) {
                            notificationsList.add(notification)
                        }
                }

                // Sort the notifications by the 'time' property in descending order (latest first)
                val sortedNotifications = notificationsList.sortedByDescending { it.time }

                // Update adapter with sorted notifications
                notificationAdapter.notifications = sortedNotifications
                notificationAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors, such as permission issues
            }
        })
    }
}


//    private fun notificationWindowChanges(action: String) {
//        if(action == "open"){
//            main_CARD_Notifications.visibility = View.VISIBLE
//        }
//        if(action == "close"){
//            main_CARD_Notifications.visibility = View.GONE
//        }















