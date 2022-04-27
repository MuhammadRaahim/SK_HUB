package com.jdars.shared_online_business.Fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
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
import com.horizam.skbhub.Utils.Constants
import com.horizam.skbhub.Utils.Constants.Companion.PRODUCT_DATABASE_ROOT
import com.jdars.shared_online_business.Activities.BrandsActivity
import com.jdars.shared_online_business.Activities.CategoriesActivity
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.Utils.ImageFilePath
import com.jdars.shared_online_business.databinding.ActivityBrandsBinding
import com.jdars.shared_online_business.databinding.DialogFileUploadingBinding
import com.jdars.shared_online_business.databinding.FragmentAddProductBinding
import com.jdars.shared_online_business.models.Category
import com.jdars.shared_online_business.models.Product
import java.io.File
import java.text.SimpleDateFormat
import java.util.*



class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var genericHandler: GenericHandler
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var productReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var dialogFileUpload: Dialog
    private lateinit var bindingFileUploadDialog: DialogFileUploadingBinding
    private var imagePath: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)

        initView()
        setClickListeners()

        return binding.root
    }

    private fun initView() {
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        productReference = db.collection(PRODUCT_DATABASE_ROOT)
        firebaseStorage = FirebaseStorage.getInstance()

        initFileUploadDialog()

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun setClickListeners() {
        binding.apply {
            etBrand.setOnClickListener {
                brandsLauncher.launch(Intent(requireContext(), BrandsActivity::class.java))
            }
            spCategories.setOnClickListener {
                categoryLauncher.launch(Intent(requireContext(), CategoriesActivity::class.java))
            }
            ivEditImage.setOnClickListener {
                launchGalleryIntent()
            }
            btnAddProduct.setOnClickListener {
                when {
                    etTitle.text.toString().trim().isEmpty() -> {
                        etTitle.error = getString(R.string.str_invalid_field)
                    }
                    spCategories.text.toString().trim().isEmpty() -> {
                        spCategories.error = getString(R.string.str_invalid_field)
                    }
                    spSubCategories.text.toString().trim().isEmpty() -> {
                        spSubCategories.error = getString(R.string.str_invalid_field)
                    }
                    etPriza.text.toString().trim().isEmpty() -> {
                        etPriza.error = getString(R.string.str_invalid_field)
                    }
                    etBrand.text.toString().trim().isEmpty() -> {
                        etBrand.error = getString(R.string.str_invalid_field)
                    }
                    etSize.text.toString().trim().isEmpty() -> {
                        etSize.error = getString(R.string.str_invalid_field)
                    }
                    etColor.text.toString().trim().isEmpty() -> {
                        etColor.error = getString(R.string.str_invalid_field)
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

    private fun getData() {
        if (imagePath!= null){
            uploadImageToStorage(imagePath!!)
        }else{
            addProduct("")
        }
    }

    private fun uploadImageToStorage(imagePath: String){
        val file = File(imagePath)
        val uniqueId = UUID.randomUUID().toString()
        val storagePath = "Products Pictures/${currentFirebaseUser.uid}/".plus(uniqueId)
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
                        addProduct(uri.toString())
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

    private fun addProduct(image: String) {
        binding.apply {
            genericHandler.showProgressBar(false)
            val id: String = productReference.id
            val ownerId: String = currentFirebaseUser.uid
            val pTitle: String = etTitle.text.toString().trim()
            val pCategory: String = spCategories.text.toString().trim()
            val pSubCategory: String = spSubCategories.text.toString().trim()
            val pPrice: String = etPriza.text.toString().trim()
            val pBrand: String = etBrand.text.toString().trim()
            val pSize: String = etPriza.text.toString().trim()
            val pColour: String = etColor.text.toString().trim()
            val pDescription: String = etDescription.text.toString().trim()
            val createdAt: String =
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val pImage: String? = image


            val product = Product(id = id, ownerId = ownerId, pTitle = pTitle, pCategory = pCategory,
            pSubCategory = pSubCategory, pPrice = pPrice, pBrand = pBrand, pSize = pSize,
            pColour = pColour, pDescription = pDescription, createdAt = createdAt, pImage = pImage)

            uploadProduct(product)

        }

    }

    private fun uploadProduct(product: Product) {
        val ref = productReference.document(product.id)
        ref.set(product).addOnSuccessListener {
            genericHandler.showProgressBar(false)
            genericHandler.showMessage("Product added Successfully")
            findNavController().popBackStack()
        }.addOnFailureListener{
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(it.message.toString())
        }
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
            .into(binding.iv)
    }

    private val brandsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding.etBrand.setText(result.data?.getStringExtra("brand"))
            }
        }

    private val categoryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding.spCategories.setText(result.data?.getStringExtra("category"))
            }
        }

}