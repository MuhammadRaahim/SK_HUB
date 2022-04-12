package com.jdars.shared_online_business.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentAddBrandBinding

class AddBrandFragment : Fragment() {

    private lateinit var binding: FragmentAddBrandBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBrandBinding.inflate(layoutInflater)



        return binding.root
    }

}