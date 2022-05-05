package com.horizam.skbhub.Adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.CategoryHomeItemBinding
import com.jdars.shared_online_business.models.Category


class CategoryAdapter(
    private var categoryList: ArrayList<Category>,
    private val context: Context
): RecyclerView.Adapter<CategoryAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: CategoryHomeItemBinding = CategoryHomeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       holder.bind(position)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<Category>){
        categoryList = list
        notifyDataSetChanged()
    }

    inner class Holder(
        binding: CategoryHomeItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: CategoryHomeItemBinding = binding

        fun bind(position: Int) {
            val category = categoryList[position]
            Glide.with(context).load(category.brandImage)
                .into(binding.ivImage)
            binding.tvName.text = category.categoryTitle
            itemView.setOnClickListener {
                Navigation.findNavController(itemView).navigate(R.id.product_Fragment)
            }
        }

    }
}