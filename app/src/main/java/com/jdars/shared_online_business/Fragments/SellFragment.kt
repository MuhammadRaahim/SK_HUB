package com.jdars.shared_online_business.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Adapters.CategoryListAdapter
import com.horizam.skbhub.Adapters.ProductListAdapter
import com.horizam.skbhub.Utils.Constants
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentSellBinding
import com.jdars.shared_online_business.models.Product
import java.lang.Exception


class SellFragment : Fragment() {

    private lateinit var binding: FragmentSellBinding
    private lateinit var productAdapter: ProductListAdapter
    private lateinit var productList: ArrayList<Product>
    private lateinit var genericHandler: GenericHandler
    private lateinit var productReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var currentFirebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellBinding.inflate(layoutInflater)
        initViews()
        setProductRecyclerView()
        setClickListeners()
        getProducts()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun initViews() {
        productList = ArrayList()
        setUpFireBase()
    }

    private fun setClickListeners() {
        binding.apply {
            fabAddProduct.setOnClickListener {
                findNavController().navigate(R.id.add_product_Fragment)
            }
        }
    }

    private fun setUpFireBase() {
        db = Firebase.firestore
        productReference = db.collection(Constants.PRODUCT_DATABASE_ROOT)
        auth = FirebaseAuth.getInstance()
        currentFirebaseUser = auth.currentUser!!
    }

    private fun setProductRecyclerView() {
        binding.rvProduct.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.VERTICAL, false)
        productAdapter = ProductListAdapter(productList,requireContext())
        binding.rvProduct.adapter = productAdapter
    }

    private fun getProducts() {
        productList.clear()
        genericHandler.showProgressBar(true)
        db.collection(Constants.PRODUCT_DATABASE_ROOT).get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val product  = documentSnapshot.toObject(Product::class.java)
                try {
                    productList.add(product)
                } catch (ex: Exception) {
                    genericHandler.showProgressBar(true)
                    genericHandler.showMessage(ex.message.toString())
                }
            }
            if (productList.isNotEmpty()){
                productAdapter.updateList(productList)
                genericHandler.showProgressBar(false)
            }else{
                genericHandler.showProgressBar(false)
            }

        }.addOnFailureListener {
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(it.message!!)
        }

    }


}