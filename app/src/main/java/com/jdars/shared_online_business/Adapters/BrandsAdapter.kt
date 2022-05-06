package com.horizam.skbhub.Adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.BrandsItemBinding
import com.jdars.shared_online_business.models.Brand


class BrandsAdapter(
    private var brandList: ArrayList<Brand>,
    private var context: Context
): RecyclerView.Adapter<BrandsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: BrandsItemBinding = BrandsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
         holder.bind(position)
    }

    override fun getItemCount(): Int {
        return brandList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<Brand>){
        brandList = list
        notifyDataSetChanged()
    }

    inner class Holder(
        binding: BrandsItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: BrandsItemBinding = binding

        fun bind(position: Int) {
            val brand = brandList[position]
            Glide.with(context).load(brand.brandImage)
                .placeholder(R.drawable.img_brand_logo)
                .into(binding.ivImage)
            binding.tvName.text = brand.brandTitle

            itemView.setOnClickListener {
                Navigation.findNavController(itemView).navigate(R.id.product_Fragment)
            }
        }

    }
}