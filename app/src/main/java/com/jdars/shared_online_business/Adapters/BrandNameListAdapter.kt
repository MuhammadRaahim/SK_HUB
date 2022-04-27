package com.horizam.skbhub.Adapters
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdars.shared_online_business.Activities.BrandsActivity
import com.jdars.shared_online_business.databinding.BrandCategoryItemBinding


class BrandNameListAdapter(
    val context: Context,
    var brandList: ArrayList<String>
): RecyclerView.Adapter<BrandNameListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: BrandCategoryItemBinding = BrandCategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
         holder.bind(position)
    }

    override fun getItemCount(): Int {
        return brandList.size
    }

    fun updateBrandList(list: ArrayList<String>){
        this.brandList = list
        notifyDataSetChanged()
    }

    inner class Holder(
        binding: BrandCategoryItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: BrandCategoryItemBinding = binding

        fun bind(position: Int){

            val brandTitle = brandList[position]
            binding.tvText.text = brandTitle

            itemView.setOnClickListener {
                val intent = Intent()
                intent.putExtra("brand", brandTitle)
                (context as BrandsActivity).setResult(Activity.RESULT_OK, intent)
                context.finish()
            }
        }

    }
}