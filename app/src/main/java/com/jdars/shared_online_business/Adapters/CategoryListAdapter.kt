package com.horizam.skbhub.Adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdars.shared_online_business.databinding.CategoryHomeItemBinding
import com.jdars.shared_online_business.databinding.CategoryListItemBinding


class CategoryListAdapter(): RecyclerView.Adapter<CategoryListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: CategoryListItemBinding = CategoryListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 7
    }

    inner class Holder(
        binding: CategoryListItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: CategoryListItemBinding = binding

    }
}