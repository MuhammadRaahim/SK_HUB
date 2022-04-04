package com.jdars.shared_online_business.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Utils.Constants
import com.jdars.shared_online_business.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setSplash()
    }
    private fun setSplash() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
                checkLoginInfo()
        }, Constants.SPLASH_DISPLAY_LENGTH.toLong())
    }

    private fun checkLoginInfo(){
        val user = Firebase.auth.currentUser
        val intent = if (user != null){
            Intent(this@SplashActivity, MainActivity::class.java)
        }else{
            Intent(this@SplashActivity, AuthenticationActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}