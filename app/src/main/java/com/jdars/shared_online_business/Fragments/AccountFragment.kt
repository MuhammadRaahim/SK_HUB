package com.jdars.shared_online_business.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Utils.Constants
import com.jdars.shared_online_business.Activities.AccountSettingActivity
import com.jdars.shared_online_business.Activities.AddressesActivity
import com.jdars.shared_online_business.Activities.AuthenticationActivity
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.Utils.BaseUtils
import com.jdars.shared_online_business.Utils.BaseUtils.Companion.showMessage
import com.jdars.shared_online_business.databinding.FragmentAccountBinding
import com.jdars.shared_online_business.models.User


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var genericHandler: GenericHandler
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var userReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        initView()
        setClickListener()
        getUserFirebaseData()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun initView() {
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        userReference = db.collection(Constants.USERS_DATABASE_ROOT)
    }

    private fun getUserFirebaseData(){
        genericHandler.showProgressBar(true)
        currentFirebaseUser = auth.currentUser!!
        userReference.document(currentFirebaseUser!!.uid)
            .get().addOnCompleteListener {
                if (it.isSuccessful){
                    val document: DocumentSnapshot = it.result
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        if (user!!.profileImage != null){
                            Glide.with(requireContext()).load(user.profileImage).placeholder(R.drawable.img_product_placeholder)
                                .into(binding.cvProfileImage)
                        }
                        binding.tvName.text = user.userName
                        binding.tvEmail.text = user.email
                        genericHandler.showProgressBar(false)
                    }
                }else{
                    genericHandler.showProgressBar(false)
                    genericHandler.showMessage( it.exception.toString())
                }
            }
    }

    private fun setClickListener() {
        binding.apply {
            llAccountSetting.setOnClickListener {
                startActivity(Intent(requireContext(),AccountSettingActivity::class.java))
            }
            llSellerPanel.setOnClickListener {
                findNavController().navigate(R.id.seller_panel_Fragment)
            }
            llNotification.setOnClickListener {
                findNavController().navigate(R.id.notification_Fragment)
            }
            llSignOut.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(requireContext(),AuthenticationActivity::class.java))
            }
            llMyAddressses.setOnClickListener {
                startActivity(Intent(requireContext(),AddressesActivity::class.java))
            }
            llContactUs.setOnClickListener {
                val phone = "+9234004748"
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                startActivity(intent)
            }
            llAboutUs.setOnClickListener {
                showMessage(binding.root,"WE ARE THE FUTURE")
            }
            llPrivacy.setOnClickListener {
                showMessage(binding.root,"All the data is encrypted end to end")
            }
            llRatingTheApp.setOnClickListener {
                showMessage(binding.root,"IN DEVELOPMENT")
            }
        }
    }

}