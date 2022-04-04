package com.jdars.shared_online_business.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.horizam.skbhub.Utils.Constants.Companion.USERS_DATABASE_ROOT
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.Utils.BaseUtils.Companion.showMessage
import com.jdars.shared_online_business.Utils.Validator
import com.jdars.shared_online_business.databinding.FragmentRegisterBinding
import com.jdars.shared_online_business.models.User


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var userReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        setUpUI()
        setOnclickListeners()

        return binding.root
    }

    private fun setOnclickListeners() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            tvAlreadyHaveAccount.setOnClickListener {
                findNavController().popBackStack()
            }
            tvSignIn.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSignUp.setOnClickListener {
                if (!Validator.isValidUserName(etUsername, true,requireContext())) {
                    return@setOnClickListener
                }else if (!Validator.isValidEmail(etEmail, true, requireContext())) {
                    return@setOnClickListener
                } else if (!Validator.isValidPhone(etCarrierNumber, true, requireContext())) {
                    return@setOnClickListener
                } else if (!Validator.isValidAddress(etAddress, true, requireContext())) {
                    return@setOnClickListener
                } else if (!Validator.isValidAddress(etAddress, true, requireContext())){
                    return@setOnClickListener
                } else {
                    binding.progressLayout.visibility = View.VISIBLE
                    signUp()
                }
            }
        }
    }

    private fun signUp() {
        auth.createUserWithEmailAndPassword(binding.etEmail.text.toString().trim(), binding.etPassword.text.toString().trim())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    getData(task.result.user!!.uid)
                } else {
                    binding.progressLayout.visibility = View.GONE
                    showMessage(binding.root,task.exception!!.message.toString())
                }
            }
    }

    private fun getData(Uid: String) {
        val userName = binding.etUsername.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val phone = binding.ccp.selectedCountryCodeWithPlus+binding.etCarrierNumber.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val gender = binding.etGender.text.toString().trim()

        val user = User(
            id = Uid, userName = userName, email = email,
            phone = phone, address = address, gender = gender
        )

        createProfile(user)
    }

    private fun createProfile(user: User) {
        val ref = userReference.document(user.id!!)
        ref.set(user).addOnSuccessListener {
            binding.progressLayout.visibility = View.GONE
            showMessage(binding.root,getString(R.string.str_signup_successfully))
            findNavController().popBackStack()
        }.addOnFailureListener{
            binding.progressLayout.visibility = View.GONE
            showMessage(binding.root,it.message.toString())
        }
    }

    private fun setUpUI() {
        auth = Firebase.auth
        db = Firebase.firestore
        userReference = db.collection(USERS_DATABASE_ROOT)
        firebaseStorage = FirebaseStorage.getInstance()
    }

}