package com.jdars.shared_online_business.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.horizam.skbhub.Utils.Constants.Companion.CONVERSATIONS_DATABASE_ROOT
import com.jdars.shared_online_business.Adapters.ChatAdapter
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentChatBinding
import com.jdars.shared_online_business.models.Inbox


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var adapter: ChatAdapter
    private lateinit var genericHandler: GenericHandler
    private lateinit var inboxList: ArrayList<Inbox>
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)
        initComponent()
        setAdapter()
        getInboxDataFromFirebaseFirestore()
        setListener()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun setListener() {
        binding.backIV.setOnClickListener {
            this.findNavController().popBackStack()
        }
    }

    private fun getInboxDataFromFirebaseFirestore() {
        genericHandler.showProgressBar(true)
        db.collection(CONVERSATIONS_DATABASE_ROOT).orderBy("sentAt", Query.Direction.DESCENDING)
            .whereArrayContains("members", FirebaseAuth.getInstance().currentUser!!.uid)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.wtf("", "listen:error", e)
                    genericHandler.showProgressBar(false)
                    return@addSnapshotListener
                } else {
                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                val inbox = dc.document.toObject(Inbox::class.java)
                                inboxList.add(inbox)
                            }
                            DocumentChange.Type.MODIFIED -> {
                                val inbox = dc.document.toObject(Inbox::class.java)
                                for (pos in 0 until inboxList.size - 1) {
                                    if (inbox.id == inboxList[pos].id) {
                                        inboxList.removeAt(pos)
                                    }
                                }
                                inboxList.add(0, inbox)
                            }
                        }
                    }
                }
                genericHandler.showProgressBar(false)
                checkDataAvailableOrNot()
                adapter.notifyDataSetChanged()
                binding.rvChat.scrollToPosition(inboxList.size - 1);
            }
    }

    fun checkDataAvailableOrNot() {
        if (inboxList.isEmpty()) {
            binding.tvNoDataAvailable.visibility = View.VISIBLE
        } else {
            binding.tvNoDataAvailable.visibility = View.GONE
        }
    }

    private fun setAdapter() {
        adapter = ChatAdapter(this, inboxList)
        val layoutManager = LinearLayoutManager(activity)
        binding.rvChat.layoutManager = layoutManager
        binding.rvChat.adapter = adapter
    }

    private fun initComponent() {
        inboxList = ArrayList()
        db = FirebaseFirestore.getInstance()
    }


}