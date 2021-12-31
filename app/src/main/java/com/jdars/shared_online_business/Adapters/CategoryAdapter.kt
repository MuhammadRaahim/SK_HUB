package com.horizam.skbhub.Adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdars.shared_online_business.databinding.CategoryHomeItemBinding


class CategoryAdapter(): RecyclerView.Adapter<CategoryAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: CategoryHomeItemBinding = CategoryHomeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 7
    }

    inner class Holder(
        binding: CategoryHomeItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: CategoryHomeItemBinding = binding

    }
}