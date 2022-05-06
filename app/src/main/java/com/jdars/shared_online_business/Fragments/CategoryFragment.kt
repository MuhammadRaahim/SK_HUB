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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Adapters.CategoryAdapter
import com.horizam.skbhub.Adapters.CategoryListAdapter
import com.horizam.skbhub.Utils.Constants
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.CategoryListItemBinding
import com.jdars.shared_online_business.databinding.FragmentCategoryBinding
import com.jdars.shared_online_business.models.Category
import java.lang.Exception


class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryAdapter: CategoryListAdapter
    private lateinit var categoryList: ArrayList<Category>
    private lateinit var genericHandler: GenericHandler
    private lateinit var categoryReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var currentFirebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        initViews()
        setCategoryRecyclerView()
        getCategories()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun initViews() {
        categoryList = ArrayList()
        setUpFireBase()
    }

    private fun setUpFireBase() {
        db = Firebase.firestore
        categoryReference = db.collection(Constants.CATEGORY_DATABASE_ROOT)
        auth = FirebaseAuth.getInstance()
        currentFirebaseUser = auth.currentUser!!
    }

    private fun setCategoryRecyclerView() {
        binding.rvCategory.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.VERTICAL, false)
        categoryAdapter = CategoryListAdapter(categoryList,requireContext())
        binding.rvCategory.adapter = categoryAdapter
    }

    private fun getCategories() {
        categoryList.clear()
        genericHandler.showProgressBar(true)
        db.collection(Constants.CATEGORY_DATABASE_ROOT).get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val category  = documentSnapshot.toObject(Category::class.java)
                try {
                    categoryList.add(category)
                } catch (ex: Exception) {
                    genericHandler.showProgressBar(true)
                    genericHandler.showMessage(ex.message.toString())
                }
            }
            if (categoryList.isNotEmpty()){
                categoryAdapter.updateList(categoryList)
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