package com.horizam.skbhub.Adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.BrandsItemBinding


class BrandsAdapter(): RecyclerView.Adapter<BrandsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: BrandsItemBinding = BrandsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
         holder.bind()
    }

    override fun getItemCount(): Int {
        return 7
    }

    inner class Holder(
        binding: BrandsItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: BrandsItemBinding = binding

        fun bind(){
            itemView.setOnClickListener {
                Navigation.findNavController(itemView).navigate(R.id.product_Fragment)
            }
        }

    }
}