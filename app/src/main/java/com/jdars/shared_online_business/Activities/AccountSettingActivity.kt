package com.jdars.shared_online_business.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.ActivityAccountSettingBinding

class AccountSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)
    }
}