package com.jdars.shared_online_business.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
                startActivity(Intent(this, AuthenticationActivity::class.java))
                finish()
        }, Constants.SPLASH_DISPLAY_LENGTH.toLong())
    }
}