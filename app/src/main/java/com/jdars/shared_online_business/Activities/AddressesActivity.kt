package com.jdars.shared_online_business.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jdars.shared_online_business.Utils.BaseUtils.Companion.showMessage
import com.jdars.shared_online_business.databinding.ActivityAddressesBinding

class AddressesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clMap.setOnClickListener {
            showMessage(findViewById(android.R.id.content),"More Functionality coming soon")
        }


    }
}