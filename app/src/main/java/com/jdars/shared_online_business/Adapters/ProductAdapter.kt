package com.jdars.shared_online_business.Adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.horizam.skbhub.Utils.Constants
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.ProductListItemBinding
import com.jdars.shared_online_business.models.Product


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

        fun bind(position: Int) {
            val product = productList[position]

            Glide.with(context).load(product.pImage)
                .placeholder(R.drawable.img_product_placeholder)
                .into(binding.ivImage)
            binding.tvName.text = product.pTitle
            binding.tvPrice.text = "RS: ${product.pPrice}/="
            binding.tvAddress.text = product.pBrand
            binding.tvDate.text = product.createdAt
            itemView.setOnClickListener {
                val bundle =  bundleOf(Constants.PRODUCT to product)
                findNavController(itemView).navigate(R.id.product_details_Fragment,bundle)
            }
        }

    }
}