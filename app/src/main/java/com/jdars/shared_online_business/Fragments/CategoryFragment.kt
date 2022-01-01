package com.jdars.shared_online_business.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horizam.skbhub.Adapters.CategoryAdapter
import com.horizam.skbhub.Adapters.CategoryListAdapter
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.CategoryListItemBinding
import com.jdars.shared_online_business.databinding.FragmentCategoryBinding


class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryAdapter: CategoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        setCategoryRecyclerView()
        return binding.root
    }

    private fun setCategoryRecyclerView() {
        binding.rvCategory.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.VERTICAL, false)
        categoryAdapter = CategoryListAdapter()
        binding.rvCategory.adapter = categoryAdapter
    }
}