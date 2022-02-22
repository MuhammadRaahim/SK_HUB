package com.jdars.shared_online_business.Adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.ProductListItemBinding


class ProductsPageAdapter(): RecyclerView.Adapter<ProductsPageAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: ProductListItemBinding = ProductListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
          holder.itemView.setOnClickListener {
              Navigation.findNavController(holder.itemView).navigate(R.id.product_Fragment)
          }
    }

    override fun getItemCount(): Int {
        return 7
    }

    inner class Holder(
        binding: ProductListItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: ProductListItemBinding = binding


    }
}