package com.jdars.shared_online_business.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Adapters.ProductListAdapter
import com.horizam.skbhub.Utils.Constants
import com.jdars.shared_online_business.Adapters.ProductAdapter
import com.jdars.shared_online_business.Adapters.ProductsPageAdapter
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentProductBinding
import com.jdars.shared_online_business.models.Product
import com.jdars.shared_online_business.models.Selection
import java.lang.Exception

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private lateinit var productAdapter: ProductsPageAdapter
    private lateinit var productList: ArrayList<Product>
    private lateinit var genericHandler: GenericHandler
    private lateinit var productReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var selection : Selection

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(layoutInflater)
        initViews()
        setProductRecyclerView()
        getProducts()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun initViews() {
        productList = ArrayList()
        selection = arguments!!.getSerializable("select") as Selection
        setUpFireBase()
    }

    private fun setUpFireBase() {
        db = Firebase.firestore
        productReference = db.collection(Constants.PRODUCT_DATABASE_ROOT)
        auth = FirebaseAuth.getInstance()
        currentFirebaseUser = auth.currentUser!!
    }

    private fun setProductRecyclerView() {
        binding.rvProducts.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.VERTICAL, false)
        productAdapter = ProductsPageAdapter(productList,requireContext())
        binding.rvProducts.adapter = productAdapter
    }

    private fun getProducts() {
        productList.clear()
        genericHandler.showProgressBar(true)
        db.collection(Constants.PRODUCT_DATABASE_ROOT).get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val product  = documentSnapshot.toObject(Product::class.java)
                try {
                    if (selection.key == Constants.CATEGORY){
                        if (product.pCategory == selection.value){
                            productList.add(product)
                        }
                        else{
                            genericHandler.showMessage("No product")
                        }
                    }else{
                        if (product.pBrand == selection.value){
                            productList.add(product)
                        }
                        else{
                            genericHandler.showMessage("No product")
                        }
                    }
                    genericHandler.showProgressBar(false)

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