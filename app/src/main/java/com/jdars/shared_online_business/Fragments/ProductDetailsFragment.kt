package com.jdars.shared_online_business.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Utils.Constants.Companion.PRODUCT
import com.horizam.skbhub.Utils.Constants.Companion.USERS_DATABASE_ROOT
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.Utils.BaseUtils
import com.jdars.shared_online_business.databinding.FragmentProductDetailsBinding
import com.jdars.shared_online_business.models.Product
import com.jdars.shared_online_business.models.User


class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var product: Product
    private lateinit var user: User
    private lateinit var genericHandler: GenericHandler
    private lateinit var userReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var sellerPhone: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(layoutInflater)
        initViews()
        setClickListeners()
        return binding.root
    }

    private fun setClickListeners() {
        binding.backIV.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnChat.setOnClickListener {
            val bundle = bundleOf(
                "id" to user.id,
                "name" to user.userName,
                "photo" to user.profileImage,
            )
            findNavController()
                .navigate(
                    R.id.message_Fragment,
                    bundle,
                    BaseUtils.animationOpenScreen()
                )
        }
        binding.ivCall.setOnClickListener {
            val phone = sellerPhone
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun initViews() {
        product = arguments!!.getSerializable(PRODUCT) as Product
        setUpFireBase()
        setData()

    }
    private fun setUpFireBase() {
        db = Firebase.firestore
        userReference = db.collection(USERS_DATABASE_ROOT)
        auth = FirebaseAuth.getInstance()
    }

    private fun setData() {
        genericHandler.showProgressBar(true)
        binding.apply {
            Glide.with(requireContext()).load(product.pImage)
                .placeholder(R.drawable.img_product_placeholder)
                .into(binding.ivImage)
            tvItemName.text = product.pTitle
            tvDate.text = product.createdAt
            tvPrice.text = "RS: ${product.pPrice}"
            tvAddress.text = product.pLocation

            type.text = product.pCategory
            size.text = product.pSize
            if (product.pCondition){
                condition.text = getString(R.string.str_in_use)
            }else {
                condition.text = getString(R.string.str_new)
            }
            color.text = product.pColour
            description.text = product.pDescription
            getSellerDetails()
        }
    }

    private fun getSellerDetails() {
            userReference.document(product.ownerId)
                .get().addOnCompleteListener {
                    if (it.isSuccessful){
                        val document: DocumentSnapshot = it.result
                        if (document.exists()) {
                            user = document.toObject(User::class.java)!!
                            showSellerInfo()
                        }
                    }else{
                        genericHandler.showProgressBar(false)
                        genericHandler.showMessage(it.exception.toString())
                    }
                }
    }

    private fun showSellerInfo() {
        if(product.ownerId == auth.currentUser!!.uid){
            binding.btnChat.visibility = View.GONE
            binding.ivCall.visibility = View.GONE
        }
        Glide.with(requireContext()).load(user.profileImage)
            .placeholder(R.drawable.ic_profile_placeholder)
            .into(binding.ciSellerProfileImage)
        binding.tvSellerName.text = user.userName
        sellerPhone = user.phone!!
        genericHandler.showProgressBar(false)
    }

}