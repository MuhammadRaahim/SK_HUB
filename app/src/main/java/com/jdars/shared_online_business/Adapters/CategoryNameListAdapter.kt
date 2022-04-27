package com.horizam.skbhub.Adapters
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdars.shared_online_business.Activities.BrandsActivity
import com.jdars.shared_online_business.Activities.CategoriesActivity
import com.jdars.shared_online_business.databinding.BrandCategoryItemBinding


class CategoryNameListAdapter(
    val context: Context,
    var categoryList: ArrayList<String>
): RecyclerView.Adapter<CategoryNameListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: BrandCategoryItemBinding = BrandCategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
         holder.bind(position)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun updateCategoryList(list: ArrayList<String>){
        this.categoryList = list
        notifyDataSetChanged()
    }

    inner class Holder(
        binding: BrandCategoryItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: BrandCategoryItemBinding = binding

        fun bind(position: Int){

            val categoryTitle = categoryList[position]
            binding.tvText.text = categoryTitle

            itemView.setOnClickListener {
                val intent = Intent()
                intent.putExtra("category", categoryTitle)
                (context as CategoriesActivity).setResult(Activity.RESULT_OK, intent)
                context.finish()
            }
        }

    }
}