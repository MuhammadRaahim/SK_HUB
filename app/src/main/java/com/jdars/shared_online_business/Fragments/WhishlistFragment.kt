package com.jdars.shared_online_business.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Utils.Constants
import com.jdars.shared_online_business.Adapters.ProductsPageAdapter
import com.jdars.shared_online_business.Adapters.WishListAdapter
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentWhishlistBinding
import com.jdars.shared_online_business.models.Product
import com.jdars.shared_online_business.models.Selection
import java.lang.Exception


class WhishlistFragment : Fragment() {

    private lateinit var binding: FragmentWhishlistBinding
    private lateinit var wishListAdapter: WishListAdapter
    private lateinit var productList: ArrayList<Product>
    private lateinit var genericHandler: GenericHandler
    private lateinit var wishListReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var currentFirebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWhishlistBinding.inflate(layoutInflater)

        initViews()
        setProductRecyclerView()
        getWishList()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun setProductRecyclerView() {
        binding.rvWishlist.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.VERTICAL, false)
        wishListAdapter = WishListAdapter(productList,requireContext())
        binding.rvWishlist.adapter = wishListAdapter
    }

    private fun initViews() {
        productList = ArrayList()
        setUpFireBase()
    }
    private fun setUpFireBase() {
        db = Firebase.firestore
        wishListReference = db.collection(Constants.WISHLIST_DATABASE_ROOT)
        auth = FirebaseAuth.getInstance()
        currentFirebaseUser = auth.currentUser!!
    }

    private fun getWishList() {
        productList.clear()
        genericHandler.showProgressBar(true)

        val query: Query = wishListReference.whereEqualTo("ownerId", currentFirebaseUser.uid)

        query.get().addOnSuccessListener { queryDocumentSnapshots ->
            for (documentSnapshot in queryDocumentSnapshots) {
                val product = documentSnapshot.toObject(Product::class.java)
                try {
                   productList.add(product)
                } catch (ex: Exception) {
                    genericHandler.showMessage(ex.message.toString())
                }
            }
            if (productList.isNotEmpty()){
                wishListAdapter.updateList(productList)
                binding.rvWishlist.visibility = View.VISIBLE
                binding.ivNoInventory.visibility = View.GONE
                genericHandler.showProgressBar(false)
            }else{
                binding.rvWishlist.visibility = View.GONE
                binding.ivNoInventory.visibility = View.VISIBLE
                genericHandler.showProgressBar(false)
            }

            }

    }

}