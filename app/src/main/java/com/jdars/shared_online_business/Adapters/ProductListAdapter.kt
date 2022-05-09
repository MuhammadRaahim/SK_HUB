package com.horizam.skbhub.Adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.horizam.skbhub.Utils.Constants.Companion.PRODUCT
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.SellerProductListItemBinding
import com.jdars.shared_online_business.models.Product


class ProductListAdapter(
    private var productList: ArrayList<Product>,
    private val context: Context
): RecyclerView.Adapter<ProductListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: SellerProductListItemBinding = SellerProductListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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
        binding: SellerProductListItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: SellerProductListItemBinding = binding

        fun bind(position: Int) {
            val product = productList[position]

            Glide.with(context).load(product.pImage)
                .placeholder(R.drawable.img_product_placeholder)
                .into(binding.ivImage)

            binding.tvName.text = product.pTitle
            binding.tvPrice.text = "RS: ${product.pPrice}"
            binding.tvDate.text = product.createdAt
            binding.tvAddress.text = product.pLocation
            binding.tvNoOfStockItem.text  = "RS: ${product.pPrice}"
            if (product.pCondition){
                binding.tvStatus.text = context.getString(R.string.str_in_use)
            }else {
                binding.tvStatus.text = context.getString(R.string.str_new)
            }

            itemView.setOnClickListener {
                val bundle =  bundleOf(PRODUCT to product)
                findNavController(itemView).navigate(R.id.product_details_Fragment,bundle)
            }
        }



    }

}