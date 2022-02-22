package com.jdars.shared_online_business.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdars.shared_online_business.Adapters.ProductAdapter
import com.jdars.shared_online_business.Adapters.ProductsPageAdapter
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentProductBinding

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private lateinit var productAdapter: ProductsPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(layoutInflater)
        setProductRecyclerView()
        return binding.root
    }

    private fun setProductRecyclerView() {
        binding.rvProducts.layoutManager =  GridLayoutManager(requireActivity(),2,  RecyclerView.VERTICAL, false)
        productAdapter = ProductsPageAdapter()
        binding.rvProducts.adapter = productAdapter
    }

}