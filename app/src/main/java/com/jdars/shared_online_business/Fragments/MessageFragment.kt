package com.jdars.shared_online_business.Fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.horizam.skbhub.Utils.Constants
import com.jdars.shared_online_business.Adapters.MessageAdapter
import com.jdars.shared_online_business.CallBacks.HideBottomNavigation
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.FragmentMessageBinding
import com.jdars.shared_online_business.models.*
import java.lang.Exception


class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding
    private lateinit var db: FirebaseFirestore;
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var userData: User
    private lateinit var mineData: User
    private lateinit var adapter: MessageAdapter
    private var inbox: Inbox? = null
    private lateinit var messageList: ArrayList<Message>
    private lateinit var lastVisible: DocumentSnapshot
    private lateinit var layoutManager: LinearLayoutManager
    private var loading: Boolean = false
    private lateinit var hideBottomNavigation: HideBottomNavigation

    // if chat come from product screen then these variable updates and stores in messages
    private var productMessageModel: ProductMessageModel? = null

    override fun onDestroy() {
        super.onDestroy()
        hideBottomNavigation.showNavigation()

    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation.hideNavigation()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideBottomNavigation = context as HideBottomNavigation
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(inflater, container, false)

        initComponent()
        getUserDataFromBundle()
        setListener()

        return binding.root
    }

    private fun setRecyclerviewListener() {

        binding.rvMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition =
                    layoutManager.findFirstCompletelyVisibleItemPosition()
                if (firstVisibleItemPosition == 0 && loading == false) {
                    loading = true
                    binding.pbMessages.visibility = View.VISIBLE
                    getMoreItemFromFirebase()
                }
            }
        })
    }

    private fun getMoreItemFromFirebase() {
        var check = 0
        var messages: ArrayList<Message> = ArrayList()
        val query = db.collection(Constants.CONVERSATIONS_DATABASE_ROOT).document(inbox!!.id)
            .collection(Constants.MESSAGE)
            .orderBy("sentAt", Query.Direction.ASCENDING).endBefore(lastVisible)
            .limitToLast(10)
        query.get().addOnSuccessListener { result ->
            loading = false
            binding.pbMessages.visibility = View.GONE
            for (dc in result.documents) {
                val message = dc.toObject(Message::class.java)
                messages.add(message!!)
                if (check == 0) {
                    check++
                    lastVisible = dc
                }
            }
            messageList.addAll(0, messages)
            adapter.notifyItemRangeInserted(0, messages.size)
        }.addOnFailureListener { exception ->

        }
    }

    private fun getAllMessagesFromFirebaseFirestore() {
        binding.pbMessages.visibility = View.VISIBLE
        var check = 0
        val query = db.collection(Constants.CONVERSATIONS_DATABASE_ROOT).document(inbox!!.id)
            .collection(Constants.MESSAGE)
            .orderBy("sentAt", Query.Direction.ASCENDING).limitToLast(10)
        query.addSnapshotListener { snapshots, e ->
            if (e != null) {
                binding.pbMessages.visibility = View.GONE
            } else {
                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val chatMessage = dc.document.toObject(Message::class.java)
                            for (pos in 0 until messageList.size) {
                                if (messageList[pos].id == chatMessage.id) {
                                    messageList.removeAt(pos)
                                }
                            }
                            messageList.add(chatMessage)
                            if (check == 0) {
                                lastVisible = dc.document
                                check++
                            }
                        }
                    }
                }
                binding.mainProgress.visibility = View.GONE
                binding.pbMessages.visibility = View.GONE
                adapter.notifyDataSetChanged()
                if (messageList.size > 0) {
                    binding.rvMessages.scrollToPosition(messageList.size - 1)
                }
            }
            setRecyclerviewListener()
        }
    }


    private fun loadUserData() {
        binding.tvName.text = userData.userName
        Glide.with(requireContext())
            .load(userData.profileImage)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .into(binding.ivImage)
    }

    private fun setListener() {
        binding.ivSend.setOnClickListener {
            if (binding.etMessage.text.toString().isNotEmpty()) {
                checkInboxNullOrNot(null)
            }
        }
        binding.ivBack.setOnClickListener {
            this.findNavController().popBackStack()
        }

        binding.ivSelectImage.setOnClickListener {
            if (!hasCameraPermission()) {
                requestSinglePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                getCameraImage.launch(intent)
            }
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private val requestSinglePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(activity, "Permission Granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Permission Denied.", Toast.LENGTH_SHORT).show()
            }
        }

    private val getCameraImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    handlePickerResult(result.data!!)
                } else {
                    Toast.makeText(activity, "error while picture uploading", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    private fun handlePickerResult(data: Intent) {
        if (data.data != null) {
            val imageUri: Uri = data.data!!
            findAbsolutePath(imageUri)
        }
    }

    private fun findAbsolutePath(imageUri: Uri) {
        binding.rlLoading.visibility = View.VISIBLE
        var storage =
            storageReference.child("images/" + FirebaseAuth.getInstance().currentUser!!.uid + System.currentTimeMillis())
        storage.putFile(imageUri).addOnSuccessListener {
            storage.downloadUrl.addOnSuccessListener {
                binding.rlLoading.visibility = View.GONE
                checkInboxNullOrNot(it)
            }
        }.addOnFailureListener {
            binding.rlLoading.visibility = View.GONE
            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
        }.addOnProgressListener {
            val progress: Int =
                (100.0 * it.bytesTransferred / it.totalByteCount).toInt()
            binding.piLoading.setProgressCompat(progress, true)
            binding.tvLoadingPercentage.text = progress.toString() + "/100"
        }
    }

    private fun saveImageMessageInFirebaseFirestore(uploadSessionUri: Uri?) {
        binding.etMessage.setText("")
        val document = db.collection(Constants.CONVERSATIONS_DATABASE_ROOT).document(inbox!!.id)
            .collection(Constants.MESSAGE)
            .document()
        var message = Message(
            attachmentType = 1,
            sentAt = System.currentTimeMillis(),
            senderID = mineData.id!!,
            message = uploadSessionUri.toString(),
            id = document.id,
            productMessageModel = productMessageModel
        )
        db.collection(Constants.CONVERSATIONS_DATABASE_ROOT).document(inbox!!.id)
            .collection(Constants.MESSAGE).document(message.id).set(message)
            .addOnSuccessListener {
                updateIntoInboxFirebaseFirestore(message)
            }.addOnFailureListener {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
            }
        productMessageModel = null
    }

    private fun checkInboxNullOrNot(uri: Uri?) {
        if (inbox == null) {
            createInbox(uri)
        } else {
            if (uri == null) {
                saveMessageInFirebaseFirestore(binding.etMessage.text.toString())
            } else {
                saveImageMessageInFirebaseFirestore(uri)
            }
        }
    }

    private fun createInbox(uri: Uri?) {
        var membersInfo: ArrayList<MembersInfo> = ArrayList()
        if (mineData.profileImage == null){
            mineData.profileImage = ""
        }
        if (userData.profileImage == null){
            userData.profileImage = ""
        }
        membersInfo.add(MembersInfo(id = mineData.id!!, photo = mineData.profileImage!!, name = mineData.userName!!))
        membersInfo.add(MembersInfo(id = userData.id!!, photo = userData.profileImage!!, name = userData.userName!!))
        var members: ArrayList<String> = ArrayList()
        members.add(mineData.id!!)
        members.add(userData.id!!)
        var createdBy: String = mineData.id!!
        var lastMessage: String = ""
        if (uri == null) {
            lastMessage = binding.etMessage.text.toString()
        } else {
            lastMessage = "image"
        }
        var senderID: String = mineData.id!!
        var sentAt: Long = System.currentTimeMillis()
        var senderName: String = mineData.userName!!
        var createdAt: Long = System.currentTimeMillis()

        val document: DocumentReference = db.collection(Constants.CONVERSATIONS_DATABASE_ROOT).document()
        inbox = Inbox(
            membersInfo = membersInfo,
            members = members,
            createdBy = createdBy,
            lastMessage = lastMessage,
            senderId = senderID,
            sentAt = sentAt,
            senderName = senderName,
            createdAt = createdAt,
            id = document.id
        )
        insertInboxDataIntoDatabase(inbox!!, uri)
    }

    private fun insertInboxDataIntoDatabase(inbox: Inbox, uri: Uri?) {
        db.collection(Constants.CONVERSATIONS_DATABASE_ROOT).document(inbox.id).set(inbox)
            .addOnSuccessListener {
                getAllMessagesFromFirebaseFirestore()
                if (uri == null) {
                    saveMessageInFirebaseFirestore(inbox.lastMessage)
                } else {
                    saveImageMessageInFirebaseFirestore(uri)
                }
            }.addOnFailureListener {
                Log.wtf("mytag", it.toString())
            }
    }

    private fun saveMessageInFirebaseFirestore(message1: String) {
        binding.etMessage.setText("")
        val document = db.collection(Constants.CONVERSATIONS_DATABASE_ROOT).document(inbox!!.id)
            .collection(Constants.MESSAGE)
            .document()
        var message = Message(
            sentAt = System.currentTimeMillis(),
            senderID = mineData.id!!,
            message = message1,
            id = document.id,
            productMessageModel = productMessageModel
        )
        db.collection(Constants.CONVERSATIONS_DATABASE_ROOT).document(inbox!!.id)
            .collection(Constants.MESSAGE).document(message.id).set(message)
            .addOnSuccessListener {
                updateIntoInboxFirebaseFirestore(message)
            }.addOnFailureListener {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
            }
        productMessageModel = null
    }

    private fun updateIntoInboxFirebaseFirestore(message: Message) {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap.put("senderId", message.senderID)
        hashMap.put("sentAt", message.sentAt)
        if (message.attachmentType == 0) {
            hashMap.put("lastMessage", message.message)
        } else {
            hashMap.put("lastMessage", "image")
        }
        db.collection(Constants.CONVERSATIONS_DATABASE_ROOT).document(inbox!!.id)
            .update(hashMap).addOnSuccessListener {
            }.addOnFailureListener {
                Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun getUserDataFromBundle() {
        binding.mainProgress.visibility = View.VISIBLE
        if (requireArguments().containsKey("id")) {
            userData = User(
                requireArguments().getString("id").toString(),
                requireArguments().getString("name").toString(),
                requireArguments().getString("photo").toString()
            )
            if (requireArguments().getString("product_id") != null) {
                productMessageModel = ProductMessageModel(
                    productID = requireArguments().getString("product_id").toString(),
                    productName = requireArguments().getString("product_name").toString(),
                    productImage = requireArguments().getString("product_image").toString()
                )
            }
            mineData = User(FirebaseAuth.getInstance().currentUser!!.uid, Constants.USER_NAME, Constants.USER_IMAGE)
            loadUserData()
            checkChatAlreadyCreateOrNot()
        }
    }

    private fun initComponent() {
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        db = FirebaseFirestore.getInstance()
        messageList = ArrayList()
        setMessageAdapter()
    }

    private fun setMessageAdapter() {
        adapter = MessageAdapter(this, messageList)
        layoutManager = LinearLayoutManager(activity)
        layoutManager.stackFromEnd = true
        binding.rvMessages.layoutManager = layoutManager
        binding.rvMessages.setHasFixedSize(true)
        binding.rvMessages.adapter = adapter
    }

    private fun checkChatAlreadyCreateOrNot() {
        db.collection(Constants.CONVERSATIONS_DATABASE_ROOT).whereArrayContains("members", mineData.id!!)
            .get().addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val inbox1 = documentSnapshot.toObject(Inbox::class.java)
                    try {
                        if (inbox1!!.membersInfo[0].id == userData.id && inbox1!!.membersInfo[1].id == mineData.id) {
                            inbox = inbox1
                            userData.userName = inbox1!!.membersInfo[0].name
                            userData.profileImage = inbox1!!.membersInfo[0].photo
                        }
                        if (inbox1!!.membersInfo[0].id == mineData.id && inbox1!!.membersInfo[1].id == userData.id) {
                            inbox = inbox1
                            userData.userName = inbox1!!.membersInfo[1].name
                            userData.profileImage = inbox1!!.membersInfo[1].photo
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(activity, ex.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                if (inbox != null) {
                    getAllMessagesFromFirebaseFirestore()
                    loadUserData()
                } else {
                    binding.mainProgress.visibility = View.GONE
                }
            }.addOnFailureListener {
                Log.wtf("mytag", it.toString())
            }
    }
}