package com.example.finalproject_mealmatchapp

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.finalproject_mealmatchapp.Login_And_Registration.LoginActivity
import com.example.finalproject_mealmatchapp.Utilities.SharedPreferencesManagerV3
import com.example.finalproject_mealmatchapp.Utilities.UserManager
import com.example.finalproject_mealmatchapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var isLoggedIn: Boolean = false
    private var name: String = ""
    private var email: String = ""
    private var password: String = ""
    private var type: String = ""
    private val userManager = UserManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        SharedPreferencesManagerV3.init(this)
        isLoggedIn = SharedPreferencesManagerV3.getInstance()
            .getBooleanOrLog("isLoggedIn", false)
        if(isLoggedIn){
            name = SharedPreferencesManagerV3.getInstance().getString("name", "")
            email = SharedPreferencesManagerV3.getInstance().getString("email", "")
            password = SharedPreferencesManagerV3.getInstance().getString("password", "")
            type = SharedPreferencesManagerV3.getInstance().getString("type", "")
        }
        startAnimation(binding.splashLOTTIELottie)
    }

    private fun startAnimation(lottieAnimationView: LottieAnimationView) {
        lottieAnimationView.resumeAnimation()
        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // Fetch data from SharedPreferences
            }

            override fun onAnimationEnd(animation: Animator) {
                if (isLoggedIn) {
                    // User is logged in; perform login
                    userManager.login(name, email, password, this@SplashActivity, type)
                } else {
                    // User is not logged in; navigate to login activity
                    transactToLoginActivity()
                }
            }

            override fun onAnimationCancel(animation: Animator) {
                // Handle animation cancel if needed
            }

            override fun onAnimationRepeat(animation: Animator) {
                // Optionally handle repeat; cancel if data is already fetched
            }
        })
    }

    private fun transactToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
