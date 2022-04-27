package com.horizam.skbhub.Adapters
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.CategoryHomeItemBinding
import com.jdars.shared_online_business.models.Brand
import com.jdars.shared_online_business.models.Category


class CategoryAdapter(
    private var categoryList: ArrayList<Category>
): RecyclerView.Adapter<CategoryAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: CategoryHomeItemBinding = CategoryHomeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       holder.bind()
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

        fun bind(){
            itemView.setOnClickListener {
                Navigation.findNavController(itemView).navigate(R.id.product_Fragment)
            }
        }

    }
}