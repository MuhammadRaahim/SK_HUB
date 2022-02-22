package com.jdars.shared_online_business.Adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.ProductListItemBinding


class ProductAdapter(): RecyclerView.Adapter<ProductAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: ProductListItemBinding = ProductListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
          holder.bind()

    }

    override fun getItemCount(): Int {
        return 7
    }

    inner class Holder(
        binding: ProductListItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: ProductListItemBinding = binding

        fun bind(){
            itemView.setOnClickListener {
                findNavController(itemView).navigate(R.id.product_details_Fragment)
            }
        }

    }
}