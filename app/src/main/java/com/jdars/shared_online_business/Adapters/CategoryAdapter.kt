package com.horizam.skbhub.Adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.horizam.skbhub.Utils.Constants
import com.horizam.skbhub.Utils.Constants.Companion.CATEGORY
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.CategoryHomeItemBinding
import com.jdars.shared_online_business.models.Category
import com.jdars.shared_online_business.models.Selection


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
                .placeholder(R.drawable.img_category_logo)
                .into(binding.ivImage)
            binding.tvName.text = category.categoryTitle
            itemView.setOnClickListener {
                val selection = Selection(CATEGORY,category.categoryTitle)
                val bundle =  bundleOf("select" to selection)
                Navigation.findNavController(itemView).navigate(R.id.product_Fragment,bundle)
            }
        }

    }
}