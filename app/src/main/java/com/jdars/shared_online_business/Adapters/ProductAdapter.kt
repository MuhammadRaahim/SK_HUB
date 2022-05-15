package com.jdars.shared_online_business.Adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.horizam.skbhub.Utils.Constants
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.ProductListItemBinding
import com.jdars.shared_online_business.models.Product
import com.varunest.sparkbutton.SparkEventListener





class ProductAdapter(
    private var productList: ArrayList<Product>,
    private var context: Context

): RecyclerView.Adapter<ProductAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: ProductListItemBinding = ProductListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
          holder.bind(position)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<Product>){
        productList = list
        notifyDataSetChanged()
    }

    inner class Holder(
        binding: ProductListItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: ProductListItemBinding = binding
        private  var currentFirebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        private  var db: FirebaseFirestore = Firebase.firestore
        private  var wishListReference: CollectionReference = db.collection(Constants.WISHLIST_DATABASE_ROOT)
        private  var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()

        fun bind(position: Int) {
            val product = productList[position]

            Glide.with(context).load(product.pImage)
                .placeholder(R.drawable.img_product_placeholder)
                .into(binding.ivImage)
            binding.tvName.text = product.pTitle
            binding.tvPrice.text = "RS: ${product.pPrice}/="
            binding.tvAddress.text = product.pBrand
            binding.tvDate.text = product.createdAt

            binding.sbWishlist.setEventListener(object : SparkEventListener {
                override fun onEvent(button: ImageView?, buttonState: Boolean) {
                   if (buttonState){
                       addToWishList(product)
                   }
                }
                override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {}
                override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {}

            })
            itemView.setOnClickListener {
                val bundle =  bundleOf(Constants.PRODUCT to product)
                findNavController(itemView).navigate(R.id.product_details_Fragment,bundle)
            }
        }

        private fun addToWishList(product: Product) {
            val ref = wishListReference.document(product.id)
            ref.set(product).addOnSuccessListener {
                Toast.makeText(context,"Added to wishlist",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context,it.message.toString(),Toast.LENGTH_SHORT).show()
            }

        }

    }
}