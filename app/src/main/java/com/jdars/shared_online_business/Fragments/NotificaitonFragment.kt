package com.jdars.shared_online_business.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdars.shared_online_business.Adapters.NotificationAdapter
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentNotificaitonBinding


class NotificaitonFragment : Fragment() {

    private lateinit var binding: FragmentNotificaitonBinding
    private lateinit var adapter: NotificationAdapter
    private lateinit var notificationList: ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificaitonBinding.inflate(layoutInflater)

        initViews()
        setClickListeners()
        setRecyclerView()



        return binding.root
    }

    private fun setClickListeners() {

    }

    private fun initViews() {
        notificationList = ArrayList()
    }

    private fun setRecyclerView() {
        binding.rvNotification.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.VERTICAL, false)
        adapter = NotificationAdapter(notificationList)
        binding.rvNotification.adapter = adapter
    }

}