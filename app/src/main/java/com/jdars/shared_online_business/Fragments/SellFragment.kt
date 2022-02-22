package com.jdars.shared_online_business.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horizam.skbhub.Adapters.CategoryListAdapter
import com.horizam.skbhub.Adapters.ProductListAdapter
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentSellBinding


class SellFragment : Fragment() {

    private lateinit var binding: FragmentSellBinding
    private lateinit var productAdapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellBinding.inflate(layoutInflater)
        setProductRecyclerView()
        setClickListeners()
        return binding.root
    }

    private fun setClickListeners() {
        binding.apply {
            fabAddProduct.setOnClickListener {
                findNavController().navigate(R.id.add_product_Fragment)
            }
        }
    }

    private fun setProductRecyclerView() {
        binding.rvProduct.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.VERTICAL, false)
        productAdapter = ProductListAdapter()
        binding.rvProduct.adapter = productAdapter
    }


}