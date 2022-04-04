package com.jdars.shared_online_business.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jdars.shared_online_business.Activities.MainActivity
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.Utils.BaseUtils.Companion.showMessage
import com.jdars.shared_online_business.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        initViews()
        setClickListener()
        return binding.root
    }

    private fun initViews() {
        auth = Firebase.auth
    }

    private fun setClickListener() {
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.register_Fragment)
        }
        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.reset_pass_Fragment)
        }
        binding.btnLogin.setOnClickListener {
            binding.progressLayout.visibility = View.VISIBLE
            loginUser()
        }
    }

    private fun loginUser() {
        auth.signInWithEmailAndPassword(binding.etEmail.text.toString().trim(), binding.etPassword.text.toString().trim())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    binding.progressLayout.visibility = View.GONE
                    var intent = Intent(requireActivity(), MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    binding.progressLayout.visibility = View.GONE
                    showMessage(binding.root,task.exception!!.message.toString())
                }
            }
    }


}