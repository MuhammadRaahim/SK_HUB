package com.jdars.shared_online_business.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Adapters.BrandNameListAdapter
import com.horizam.skbhub.Adapters.CategoryNameListAdapter
import com.horizam.skbhub.Utils.Constants
import com.horizam.skbhub.Utils.Constants.Companion.CATEGORY_DATABASE_ROOT
import com.jdars.shared_online_business.Utils.BaseUtils
import com.jdars.shared_online_business.databinding.ActivityCategoriesBinding
import com.jdars.shared_online_business.models.Brand
import com.jdars.shared_online_business.models.Category
import java.lang.Exception

class CategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var adapter: CategoryNameListAdapter
    private lateinit var categoryReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var categoryList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setRecyclerView()



    }

    private fun initViews() {
        categoryList = ArrayList()
        setUpFireBase()
        getCategoryList()
    }

    private fun getCategoryList() {
        binding.progressLayout.visibility = View.VISIBLE
        categoryList.clear()
        db.collection(CATEGORY_DATABASE_ROOT).get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val category  = documentSnapshot.toObject(Category::class.java)
                try {
                    categoryList.add(category.categoryTitle)
                } catch (ex: Exception) {
                    binding.progressLayout.visibility = View.GONE
                    BaseUtils.showMessage(binding.root, ex.message.toString())
                }
            }
            if (categoryList.isNotEmpty()){
                adapter.updateCategoryList(categoryList)
                binding.progressLayout.visibility = View.GONE
            }


        }.addOnFailureListener {
            binding.progressLayout.visibility = View.GONE
            BaseUtils.showMessage(binding.root, it.message.toString())
        }


    }

    private fun setUpFireBase() {
        db = Firebase.firestore
        categoryReference = db.collection(CATEGORY_DATABASE_ROOT)
        auth = FirebaseAuth.getInstance()
        currentFirebaseUser = auth.currentUser!!
    }

    private fun setRecyclerView() {
        binding.rvCategoryList.layoutManager =  LinearLayoutManager(this,  RecyclerView.VERTICAL, false)
        adapter = CategoryNameListAdapter(this,categoryList)
        binding.rvCategoryList.adapter = adapter
    }
}