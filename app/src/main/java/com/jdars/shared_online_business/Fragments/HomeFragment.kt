package com.jdars.shared_online_business.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.horizam.skbhub.Adapters.BrandsAdapter
import com.horizam.skbhub.Adapters.CategoryAdapter
import com.jdars.shared_online_business.Adapters.ProductAdapter
import com.jdars.shared_online_business.CallBacks.DrawerHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var brandAdapter: BrandsAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var drawerHandlerCallback: DrawerHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setImageSlider()
        setCLickListener()
        setCategoryRecyclerView()
        setBrandRecyclerView()
        setProductRecyclerView()

        return binding.root
    }

    private fun setCLickListener() {
        binding.appHeaderLayout.ivOpenDrawer.setOnClickListener {
            drawerHandlerCallback.openDrawer()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        drawerHandlerCallback = context as DrawerHandler
    }

    private fun setImageSlider() {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.img_accessories, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img_shoes, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img_clothes, ScaleTypes.FIT))
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
    }

    private fun setCategoryRecyclerView() {
        binding.rvAllCategory.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter()
        binding.rvAllCategory.adapter = categoryAdapter
    }

    private fun setBrandRecyclerView() {
        binding.rvAllBrands.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.HORIZONTAL, false)
        brandAdapter = BrandsAdapter()
        binding.rvAllBrands.adapter = brandAdapter
    }

    private fun setProductRecyclerView() {
        binding.rvAllProduct.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.HORIZONTAL, false)
        productAdapter = ProductAdapter()
        binding.rvAllProduct.adapter = productAdapter
    }

}