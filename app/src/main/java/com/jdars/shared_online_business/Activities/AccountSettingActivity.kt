package com.jdars.shared_online_business.Activities

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.horizam.skbhub.Utils.Constants
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.Utils.BaseUtils.Companion.showMessage
import com.jdars.shared_online_business.Utils.ImageFilePath
import com.jdars.shared_online_business.Utils.Validator
import com.jdars.shared_online_business.databinding.ActivityAccountSettingBinding
import com.jdars.shared_online_business.databinding.DialogFileUploadingBinding
import com.jdars.shared_online_business.models.User
import java.io.File
import java.util.*

class AccountSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingBinding
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var userReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var dialogFileUpload: Dialog
    private lateinit var bindingFileUploadDialog: DialogFileUploadingBinding
    private var imagePath: String? = null
    private var image: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setClickListeners()
        getUserFirebaseData()
    }

    private fun getUserFirebaseData(){
        binding.progressLayout.visibility = View.VISIBLE
        currentFirebaseUser = auth.currentUser!!
        userReference.document(currentFirebaseUser!!.uid)
            .get().addOnCompleteListener {
                if (it.isSuccessful){
                    val document: DocumentSnapshot = it.result
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        setData(user)
                    }
                }else{
                    showMessage(findViewById(android.R.id.content),it.exception.toString())
                }

            }
    }

    private fun setData(user: User?) {
        if (user!!.profileImage != null){
            image = user!!.profileImage!!
            Glide.with(this@AccountSettingActivity).load(user.profileImage).placeholder(R.drawable.img_product_placeholder)
                .into(binding.civProfile)
        }
        binding.apply {
            etFullName.setText(user!!.userName)
            etEmail.setText(user.email)
            etPhone.setText(user.phone)
            etGender.setText(user.gender)
            etAddress.setText(user.address)

        }
        binding.progressLayout.visibility = View.GONE
    }


    private fun setClickListeners() {
        binding.apply {
            ivChoseImage.setOnClickListener {
                launchGalleryIntent()
            }
            ivBack.setOnClickListener {
                onBackPressed()
            }
            btnUpdateProfile.setOnClickListener {
                if (!Validator.isValidUserName(etFullName, true,this@AccountSettingActivity)) {
                    return@setOnClickListener
                }else if (!Validator.isValidEmail(etEmail, true, this@AccountSettingActivity)) {
                    return@setOnClickListener
                } else if (!Validator.isValidPhone(etPhone, true, this@AccountSettingActivity)) {
                    return@setOnClickListener
                } else if (!Validator.isValidAddress(etAddress, true, this@AccountSettingActivity)) {
                    return@setOnClickListener
                } else if (!Validator.isValidAddress(etAddress, true, this@AccountSettingActivity)){
                    return@setOnClickListener
                } else {
                    binding.progressLayout.visibility = View.VISIBLE
                    getData()
                }

            }
        }
    }

    private fun getData() {
        if (imagePath != null) {
            uploadImageToStorage(imagePath!!)
        } else {
            createProfile(image!!)
        }
    }

    private fun uploadImageToStorage(imagePath: String){
        val file = File(imagePath)
        val uniqueId = UUID.randomUUID().toString()
        val storagePath = "Profile Pictures/${currentFirebaseUser.uid}/".plus(uniqueId)
        uploadFile(file, storagePath)
    }

    private fun uploadFile(file: File, storagePath: String) {
        val ext: String = file.extension
        if (ext.isEmpty()) {
            showMessage(findViewById(android.R.id.content),"Something went wrong")
            return
        }
        val storageReference = firebaseStorage.reference.child("$storagePath.$ext")
        val uriFile = Uri.fromFile(file)
        val uploadTask: UploadTask = storageReference.putFile(uriFile)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            if (taskSnapshot.metadata != null && taskSnapshot.metadata!!.reference != null) {
                val result = taskSnapshot.storage.downloadUrl
                result.addOnSuccessListener { uri ->
                    if (uri != null) {
                        if (dialogFileUpload.isShowing) {
                            dialogFileUpload.dismiss()
                        }
                        createProfile(uri.toString())
                    }
                }
            }
        }.addOnProgressListener { taskSnapshot ->
            val progress: Double =
                100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
            if (!dialogFileUpload.isShowing) {
                dialogFileUpload.show()
            }
            bindingFileUploadDialog.progressBar.progress = progress.toInt()
            bindingFileUploadDialog.tvFileProgress.text = progress.toInt().toString().plus("%")
        }.addOnFailureListener { e ->
            showMessage(findViewById(android.R.id.content),e.message.toString())
            if (dialogFileUpload.isShowing) {
                dialogFileUpload.dismiss()
            }
        }
    }

    private fun createProfile(image:String){
        binding.progressLayout.visibility = View.VISIBLE
        val userId = currentFirebaseUser.uid
        val userName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val gender = binding.etGender.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        val user = User(
            id = userId, userName = userName, email = email,
            gender = gender, phone = phone, address = address, profileImage = image
        )
        updateProfile(user)
    }

    private fun updateProfile(user: User) {
        val ref = userReference.document(currentFirebaseUser.uid)
        ref.set(user).addOnSuccessListener {
            binding.progressLayout.visibility = View.GONE
            showMessage(findViewById(android.R.id.content),"Save successfully")
        }.addOnFailureListener{
            binding.progressLayout.visibility = View.GONE
            showMessage(findViewById(android.R.id.content),it.message.toString())
        }
    }

    private fun launchGalleryIntent() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                getImageFromGallery.launch("image/*")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showSnackBar(
                    binding.root,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    "Ok"
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
                getImageFromGallery.launch("image/*")
            } else {
                Log.i("Permission: ", "Denied")
                Toast.makeText(this,getString(R.string.permission_required)
                    .plus(". Please enable it settings"), Toast.LENGTH_SHORT).show()
            }
        }

    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        setPicture(uri)
    }

    private fun showSnackBar(
        view: View, msg: String, length: Int, actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackBar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackBar.setAction(actionMessage) {
                action(findViewById(android.R.id.content))
            }.show()
        } else {
            snackBar.show()
        }
    }

    private fun setPicture(uri: Uri?) {
        imagePath = ImageFilePath().getFilePath(uri!!,this)
        Glide.with(this).load(uri).placeholder(R.drawable.img_product_placeholder)
            .into(binding.civProfile)
    }

    private fun initView() {
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        userReference = db.collection(Constants.USERS_DATABASE_ROOT)
        firebaseStorage = FirebaseStorage.getInstance()
        initFileUploadDialog()
    }

    private fun initFileUploadDialog() {
        dialogFileUpload = Dialog(this)
        bindingFileUploadDialog = DialogFileUploadingBinding.inflate(layoutInflater)
        dialogFileUpload.setContentView(bindingFileUploadDialog.root)
        setDialogWidth()
    }

    private fun setDialogWidth() {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialogFileUpload.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialogFileUpload.window!!.attributes = layoutParams
    }
}