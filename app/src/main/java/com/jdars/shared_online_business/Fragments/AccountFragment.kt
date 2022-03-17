package com.jdars.shared_online_business.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jdars.shared_online_business.Activities.AccountSettingActivity
import com.jdars.shared_online_business.Activities.AuthenticationActivity
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentAccountBinding


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater)

        setClickListener()

        return binding.root
    }

    private fun setClickListener() {
        binding.apply {
            llAccountSetting.setOnClickListener {
                startActivity(Intent(requireContext(),AccountSettingActivity::class.java))
            }
            llBecomeSeller.setOnClickListener {
                findNavController().navigate(R.id.add_product_Fragment)
            }
            llSignOut.setOnClickListener {
                startActivity(Intent(requireContext(),AuthenticationActivity::class.java))
            }
        }
    }

}