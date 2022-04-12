package com.jdars.shared_online_business.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentSellerPanelBinding

class SellerPanelFragment : Fragment() {

    private lateinit var binding: FragmentSellerPanelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellerPanelBinding.inflate(layoutInflater)

        setClickListeners()
        return binding.root
    }

    private fun setClickListeners() {
        binding.apply {
            mcProduct.setOnClickListener {
                findNavController().navigate(R.id.add_product_Fragment)
            }
            mcBrand.setOnClickListener {
                findNavController().navigate(R.id.add_brand_Fragment)
            }
            mcCategory.setOnClickListener {
                findNavController().navigate(R.id.add_category_Fragment)
            }
        }
    }
}