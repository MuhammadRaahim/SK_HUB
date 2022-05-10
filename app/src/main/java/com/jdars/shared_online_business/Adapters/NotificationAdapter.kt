package com.jdars.shared_online_business.Adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jdars.shared_online_business.databinding.NotificationItemBinding


class NotificationAdapter(

    private var notificationList: ArrayList<String>

): RecyclerView.Adapter<NotificationAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: NotificationItemBinding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)

    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    fun updateNotifications(list: ArrayList<String>){
        this.notificationList = list
        notifyDataSetChanged()
    }

    inner class Holder(
       private val binding: NotificationItemBinding
    ):RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int) {
            val notification = notificationList[position]

            binding.tvNotification.text = notification

        }


    }
}