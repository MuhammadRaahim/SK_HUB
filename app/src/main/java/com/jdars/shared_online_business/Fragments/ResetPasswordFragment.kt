package com.jdars.shared_online_business.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.Utils.BaseUtils.Companion.showMessage
import com.jdars.shared_online_business.Utils.Validator.Companion.isValidEmail
import com.jdars.shared_online_business.databinding.FragmentResetPasswordBinding


class ResetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordBinding.inflate(layoutInflater)
        setClickListeners()
        return binding.root
    }

    private fun setClickListeners() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            tvLogin.setOnClickListener {
                findNavController().popBackStack()
            }
            btnResetPassword.setOnClickListener {
                if (!isValidEmail(etEmail, true, requireContext())){
                    return@setOnClickListener
                }else{
                    setForgetPasswordEmail()
                }
            }
        }
    }

    private fun setForgetPasswordEmail() {
        binding.progressLayout.visibility = View.VISIBLE
        FirebaseAuth.getInstance().sendPasswordResetEmail(binding.etEmail.text.toString().trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.progressLayout.visibility = View.GONE
                    showMessage(binding.root,"Email Send Successfully")
                    findNavController().popBackStack()
                }else{
                    binding.progressLayout.visibility = View.GONE
                    showMessage(binding.root,task.exception.toString())
                }
            }
    }

}