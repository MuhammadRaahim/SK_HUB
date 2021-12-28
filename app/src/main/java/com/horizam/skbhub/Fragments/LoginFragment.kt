package com.horizam.skbhub.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.horizam.skbhub.Activities.MainActivity
import com.horizam.skbhub.R
import com.horizam.skbhub.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setClickListener()
        return binding.root
    }

    private fun setClickListener() {
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.register_Fragment)
        }
        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.reset_pass_Fragment)
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(requireActivity(),MainActivity::class.java))
        }
    }




}