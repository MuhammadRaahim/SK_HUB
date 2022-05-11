package com.jdars.shared_online_business.Adapters

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.jdars.shared_online_business.Fragments.MessageFragment
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.Utils.BaseUtils.Companion.changeMiliSecondToTime
import com.jdars.shared_online_business.models.Message
import com.jdars.shared_online_business.models.ProductMessageModel

class MessageAdapter(
    var fragment: MessageFragment,
    var messageList: ArrayList<Message>,
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private lateinit var dialog: Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {
        if (viewType == 1) {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_right_message, parent, false)
            return ViewHolder(view)
        } else {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_left_message, parent, false)
            return ViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messageList[position].senderID == FirebaseAuth.getInstance().currentUser!!.uid) {
            return 1
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (messageList[position].attachmentType == 0) {
            holder.rlTextMessage.visibility = View.VISIBLE
            holder.cvImage.visibility = View.GONE
            holder.tvMessage.text = messageList[position].message
        } else if (messageList[position].attachmentType == 1) {
            holder.rlTextMessage.visibility = View.GONE
            holder.cvImage.visibility = View.VISIBLE
            Glide.with(fragment.requireContext())
                .load(messageList[position].message)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .into(holder.ivMessage)
        }
        holder.tvTime.text = changeMiliSecondToTime(messageList[position].sentAt, "hh:mm a")

        if (messageList[position].productMessageModel == null) {
            holder.cvProduct.visibility = View.GONE
        } else {
            holder.cvProduct.visibility = View.VISIBLE
            var productMessageModel: ProductMessageModel? =
                messageList[position].productMessageModel
            holder.tvProduct.text = productMessageModel!!.productName
            Glide.with(fragment.requireContext())
                .load(productMessageModel.productImage)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .into(holder.ivProduct)

        }

        holder.cvImage.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("url", messageList[position].message)
//            fragment.findNavController()
//                .navigate(R.id.singleImageFragment, bundle, Utils.animationOpenScreen())
            createImageDialog()
            showFilterDialog(messageList[position].message)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    private fun createImageDialog() {
        dialog = Dialog(fragment.requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_full_screen_image)
    }

    private fun showFilterDialog(imageUrl: String) {
        dialog.show()
        val ivImage = dialog.findViewById<Button>(R.id.iv_image) as ImageView
        val ivBack = dialog.findViewById<Button>(R.id.iv_back) as ImageView
        Glide.with(fragment.requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .into(ivImage)
        ivBack.setOnClickListener{
            dialog.dismiss()
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMessage: TextView = itemView.findViewById(R.id.tv_message)
        var tvTime: TextView = itemView.findViewById(R.id.tv_time)
        var cvProduct: CardView = itemView.findViewById(R.id.cv_product)
        var ivProduct: ImageView = itemView.findViewById(R.id.iv_product)
        var tvProduct: TextView = itemView.findViewById(R.id.tv_product)
        var rlTextMessage: RelativeLayout = itemView.findViewById(R.id.rl_text_message)
        var cvImage: CardView = itemView.findViewById(R.id.cv_image)
        var ivMessage: ImageView = itemView.findViewById(R.id.iv_message)
    }
}