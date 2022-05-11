package com.jdars.shared_online_business.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.jdars.shared_online_business.Fragments.ChatFragment
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.Utils.BaseUtils.Companion.animationOpenScreen
import com.jdars.shared_online_business.Utils.BaseUtils.Companion.changeMiliSecondToTime
import com.jdars.shared_online_business.models.Inbox
import com.jdars.shared_online_business.models.User


class ChatAdapter(var fragment: ChatFragment, var inboxList: ArrayList<Inbox>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        for (pos in 0 until inboxList[position].membersInfo.size) {
            if (inboxList[position].membersInfo[pos].id != FirebaseAuth.getInstance().currentUser!!.uid) {
                holder.tvUserName.text = inboxList[position].membersInfo[pos].name
                Glide.with(fragment.requireContext())
                    .load(inboxList[position].membersInfo[pos].photo)
                    .placeholder(R.drawable.img_product_placeholder)
                    .into(holder.ivImage)
            }
        }
        holder.tvLastMessage.text = inboxList[position].lastMessage
        holder.tvDate.text =
            changeMiliSecondToTime(inboxList[position].sentAt, "(yyyy-MM-dd) hh:mm a")

        setListener(holder, position)
    }

    private fun setListener(holder: ViewHolder, position: Int) {
        var user = User()
        for (pos in 0 until inboxList[position].membersInfo.size) {
            if (inboxList[position].membersInfo[pos].id != FirebaseAuth.getInstance().currentUser!!.uid) {
                user.id = inboxList[position].membersInfo[pos].id
                user.userName = inboxList[position].membersInfo[pos].name
                user.profileImage = inboxList[position].membersInfo[pos].photo
            }
        }

        holder.itemView.setOnClickListener {
            val bundle = bundleOf(
                "id" to user.id,
                "name" to user.userName,
                "photo" to user.profileImage,
            )
            fragment.findNavController()
                .navigate(
                    R.id.message_Fragment,
                    bundle,
                    animationOpenScreen()
                )
        }
    }

    override fun getItemCount(): Int {
        return inboxList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: TextView = view.findViewById(R.id.tv_name)
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val tvLastMessage: TextView = view.findViewById(R.id.tv_last_message)
        val ivImage: ImageView = view.findViewById(R.id.iv_image)
    }
}