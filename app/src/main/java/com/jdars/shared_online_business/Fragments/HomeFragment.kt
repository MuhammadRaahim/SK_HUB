package com.jdars.shared_online_business.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Adapters.BrandsAdapter
import com.horizam.skbhub.Adapters.CategoryAdapter
import com.horizam.skbhub.Utils.Constants
import com.horizam.skbhub.Utils.Constants.Companion.BRANDS_DATABASE_ROOT
import com.horizam.skbhub.Utils.Constants.Companion.CATEGORY_DATABASE_ROOT
import com.horizam.skbhub.Utils.Constants.Companion.PRODUCT_DATABASE_ROOT
import com.horizam.skbhub.Utils.Constants.Companion.USERS_DATABASE_ROOT
import com.jdars.shared_online_business.Adapters.ProductAdapter
import com.jdars.shared_online_business.CallBacks.DrawerHandler
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.Utils.BaseUtils
import com.jdars.shared_online_business.databinding.FragmentHomeBinding
import com.jdars.shared_online_business.models.Brand
import com.jdars.shared_online_business.models.Category
import com.jdars.shared_online_business.models.Product
import com.jdars.shared_online_business.models.User
import java.lang.Exception


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var drawerHandlerCallback: DrawerHandler
    private lateinit var genericHandler: GenericHandler

    private lateinit var brandAdapter: BrandsAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter


    private lateinit var categoryList: ArrayList<Category>
    private lateinit var brandList: ArrayList<Brand>
    private lateinit var productList: ArrayList<Product>

    private lateinit var brandReference: CollectionReference
    private lateinit var categoryReference: CollectionReference
    private lateinit var productReference: CollectionReference
    private lateinit var userReference: CollectionReference

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var currentFirebaseUser: FirebaseUser



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        initViews()
        setImageSlider()
        setCLickListener()
        setCategoryRecyclerView()
        setBrandRecyclerView()
        setProductRecyclerView()

        return binding.root
    }

    private fun getUserFirebaseData(){
        currentFirebaseUser = auth.currentUser!!
        userReference.document(currentFirebaseUser!!.uid)
            .get().addOnCompleteListener {
                if (it.isSuccessful){
                    val document: DocumentSnapshot = it.result
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        Constants.USER_NAME = user!!.userName!!
                        if(user.profileImage != null) {
                            Constants.USER_IMAGE = user.profileImage!!
                        }
                            genericHandler.showProgressBar(false)

                    }
                }else{
                    genericHandler.showProgressBar(false)
                    genericHandler.showMessage( it.exception.toString())
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        drawerHandlerCallback = context as DrawerHandler
        genericHandler = context as GenericHandler
    }

    private fun initViews() {
        categoryList = ArrayList()
        productList = ArrayList()
        brandList = ArrayList()
        setUpFireBase()
        getData()
    }

    private fun setUpFireBase() {
        db = Firebase.firestore
        brandReference = db.collection(BRANDS_DATABASE_ROOT)
        categoryReference = db.collection(CATEGORY_DATABASE_ROOT)
        productReference = db.collection(PRODUCT_DATABASE_ROOT)
        userReference = db.collection(USERS_DATABASE_ROOT)
        auth = FirebaseAuth.getInstance()
        currentFirebaseUser = auth.currentUser!!
    }

    private fun getData() {
        genericHandler.showProgressBar(true)
        brandList.clear()
        db.collection(BRANDS_DATABASE_ROOT).get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val brand  = documentSnapshot.toObject(Brand::class.java)
                try {
                    brandList.add(brand)
                } catch (ex: Exception) {
                    genericHandler.showProgressBar(true)
                    genericHandler.showMessage(ex.message.toString())
                }
            }
            if (brandList.isNotEmpty()){
                brandAdapter.updateList(brandList)
                getCategories()
            }else{
                genericHandler.showProgressBar(false)
            }

        }.addOnFailureListener {
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(it.message!!)
        }

    }

    private fun getCategories() {
        categoryList.clear()
        db.collection(CATEGORY_DATABASE_ROOT).get().addOnSuccessListener { documentSnapshots ->
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
                getProducts()
            }else{
                genericHandler.showProgressBar(false)
            }

        }.addOnFailureListener {
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(it.message!!)
        }

    }

    private fun getProducts() {
        productList.clear()
        db.collection(PRODUCT_DATABASE_ROOT).get().addOnSuccessListener { documentSnapshots ->
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
                getUserFirebaseData()
            }else{
                genericHandler.showProgressBar(false)
            }

        }.addOnFailureListener {
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(it.message!!)
        }

    }

    private fun setCLickListener() {
        binding.appHeaderLayout.ivOpenDrawer.setOnClickListener {
            drawerHandlerCallback.openDrawer()
        }
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
        categoryAdapter = CategoryAdapter(categoryList,requireContext())
        binding.rvAllCategory.adapter = categoryAdapter
    }

    private fun setBrandRecyclerView() {
        binding.rvAllBrands.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.HORIZONTAL, false)
        brandAdapter = BrandsAdapter(brandList,requireContext())
        binding.rvAllBrands.adapter = brandAdapter
    }

    private fun setProductRecyclerView() {
        binding.rvAllProduct.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.HORIZONTAL, false)
        productAdapter = ProductAdapter(productList,requireContext())
        binding.rvAllProduct.adapter = productAdapter
    }

}