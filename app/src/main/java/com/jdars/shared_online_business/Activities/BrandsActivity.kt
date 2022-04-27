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
import com.horizam.skbhub.Utils.Constants.Companion.BRANDS_DATABASE_ROOT
import com.jdars.shared_online_business.Utils.BaseUtils.Companion.showMessage
import com.jdars.shared_online_business.databinding.ActivityBrandsBinding
import com.jdars.shared_online_business.models.Brand
import java.lang.Exception

class BrandsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBrandsBinding
    private lateinit var adapter: BrandNameListAdapter
    private lateinit var brandReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var brandsList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrandsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setRecyclerView()
    }

    private fun initViews() {
        brandsList = ArrayList()
        setUpFireBase()
        getBrand()
    }

    private fun getBrand() {
        binding.progressLayout.visibility = View.VISIBLE
        brandsList.clear()
            db.collection(BRANDS_DATABASE_ROOT).get().addOnSuccessListener { documentSnapshots ->
                for (documentSnapshot in documentSnapshots) {
                    val brand  = documentSnapshot.toObject(Brand::class.java)
                    try {
                            brandsList.add(brand.brandTitle)
                    } catch (ex: Exception) {
                        binding.progressLayout.visibility = View.GONE
                        showMessage(binding.root,ex.message.toString())
                    }
                }
                if (brandsList.isNotEmpty()){
                    adapter.updateBrandList(brandsList)
                    binding.progressLayout.visibility = View.GONE
                }


            }.addOnFailureListener {
                binding.progressLayout.visibility = View.GONE
                showMessage(binding.root,it.message.toString())
            }


    }

    private fun setUpFireBase() {
        db = Firebase.firestore
        brandReference = db.collection(BRANDS_DATABASE_ROOT)
        auth = FirebaseAuth.getInstance()
        currentFirebaseUser = auth.currentUser!!
    }

    private fun setRecyclerView() {
        binding.rvBandsList.layoutManager =  LinearLayoutManager(this,  RecyclerView.VERTICAL, false)
        adapter = BrandNameListAdapter(this, brandsList)
        binding.rvBandsList.adapter = adapter
    }
}