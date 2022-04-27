package com.jdars.shared_online_business.Fragments

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.horizam.skbhub.Utils.Constants.Companion.BRANDS_DATABASE_ROOT
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.Utils.ImageFilePath
import com.jdars.shared_online_business.databinding.DialogFileUploadingBinding
import com.jdars.shared_online_business.databinding.FragmentAddBrandBinding
import com.jdars.shared_online_business.models.Brand
import java.io.File
import java.util.*

class AddBrandFragment : Fragment() {

    private lateinit var binding: FragmentAddBrandBinding
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var brandReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var genericHandler: GenericHandler
    private lateinit var dialogFileUpload: Dialog
    private lateinit var bindingFileUploadDialog: DialogFileUploadingBinding
    private var imagePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBrandBinding.inflate(layoutInflater)

        initView()
        setClickListeners()



        return binding.root
    }

    private fun initView() {
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        brandReference = db.collection(BRANDS_DATABASE_ROOT)
        firebaseStorage = FirebaseStorage.getInstance()

        initFileUploadDialog()

    }

    private fun setClickListeners() {
        binding.apply {
            ivEditImage.setOnClickListener {
                launchGalleryIntent()
            }
            btAddBrand.setOnClickListener {
                when {
                    etTitle.text.toString().trim().isEmpty() -> {
                        etTitle.error = getString(R.string.str_invalid_field)
                    }
                    etOwnerName.text.toString().trim().isEmpty() -> {
                        etOwnerName.error = getString(R.string.str_invalid_field)
                    }
                    etOwnerEmail.text.toString().trim().isEmpty() -> {
                        etOwnerEmail.error = getString(R.string.str_invalid_field)
                    }
                    etDescription.text.toString().trim().isEmpty() -> {
                        etDescription.error = getString(R.string.str_invalid_field)
                    }
                    else -> {
                        getData()
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }



    private fun uploadImageToStorage(imagePath: String){
        val file = File(imagePath)
        val uniqueId = UUID.randomUUID().toString()
        val storagePath = "Brands Pictures/${currentFirebaseUser.uid}/".plus(uniqueId)
        uploadFile(file, storagePath)
    }

    private fun uploadFile(file: File, storagePath: String) {
        val ext: String = file.extension
        if (ext.isEmpty()) {
            genericHandler.showMessage("Something went wrong")
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
                        addBrand(uri.toString())
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
            genericHandler.showMessage(e.message.toString())
            if (dialogFileUpload.isShowing) {
                dialogFileUpload.dismiss()
            }
        }
    }

    private fun getData() {
        if (imagePath!= null){
            uploadImageToStorage(imagePath!!)
        }else{
            addBrand("")
        }
    }

    private fun addBrand(image: String) {
        binding.apply {
            genericHandler.showProgressBar(false)
            val refId  = brandReference.document()
            val title = etTitle.text.toString().trim()
            val ownerName = etOwnerName.text.toString().trim()
            val ownerEmail = etOwnerEmail.text.toString().trim()
            val description = etDescription.text.toString().trim()

            val brand = Brand(
                id = refId.id, brandTitle = title, ownerId = currentFirebaseUser.uid,
                ownerName = ownerName, ownerEmail = ownerEmail, description = description,
                brandImage = image!!
            )
            uploadBrand(brand)
        }
    }

    private fun uploadBrand(brand: Brand) {
        val ref = brandReference.document(brand.id)
        ref.set(brand).addOnSuccessListener {
            genericHandler.showProgressBar(false)
            genericHandler.showMessage("Brand add Successfully")
            findNavController().popBackStack()
        }.addOnFailureListener{
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(it.message.toString())
        }
    }



    private fun initFileUploadDialog() {
        dialogFileUpload = Dialog(requireContext())
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

    private fun launchGalleryIntent() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                getImageFromGallery.launch("image/*")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
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
                Toast.makeText(requireContext(),getString(R.string.permission_required)
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
                action(requireView().findViewById(android.R.id.content))
            }.show()
        } else {
            snackBar.show()
        }
    }

    private fun setPicture(uri: Uri?) {
        imagePath = ImageFilePath().getFilePath(uri!!,requireContext())
        Glide.with(this).load(uri).placeholder(R.drawable.img_product_placeholder)
            .into(binding.ivLogo)
    }

}