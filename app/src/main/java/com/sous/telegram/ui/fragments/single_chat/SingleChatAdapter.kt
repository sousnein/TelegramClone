package com.sous.telegram.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sous.telegram.R
import com.sous.telegram.database.CURRENT_UID
import com.sous.telegram.models.CommonModel
import com.sous.telegram.utilits.TYPE_MESSAGE_IMAGE
import com.sous.telegram.utilits.TYPE_MESSAGE_TEXT
import com.sous.telegram.utilits.asTime
import com.sous.telegram.utilits.downloadAndSetImage
import kotlinx.android.synthetic.main.message_item.view.*

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {
    private var mListMessagesCache = mutableListOf<CommonModel>()

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        //text
        val blocUserMessage: ConstraintLayout = view.block_user_message
        val chatUserMessage: TextView = view.chat_user_message
        val chatUserMessageTime: TextView = view.chat_user_message_time
        val blocReceivedMessage: ConstraintLayout = view.block_received_message
        val chatReceivedMessage: TextView = view.chat_received_message
        val chatReceivedMessageTime: TextView = view.chat_received_message_time

        //image
        val blocUserImageMessage: ConstraintLayout = view.block_user_image_message
        val chatUserImageMessageTime: TextView = view.chat_user_image_message_time
        val chatUserImageMessage: ImageView = view.chat_user_image
        val blocReceivedImageMessage: ConstraintLayout = view.block_received_image_message
        val chatReceivedImageMessageTime: TextView = view.chat_received_image_message_time
        val chatReceivedImageMessage: ImageView = view.chat_received_image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCache.size
    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        when (mListMessagesCache[position].type) {
            TYPE_MESSAGE_TEXT -> drawMessageText(holder, position)
            TYPE_MESSAGE_IMAGE -> drawMessageImage(holder, position)
        }

    }

    private fun drawMessageImage(holder: SingleChatHolder, position: Int) {
        holder.blocUserMessage.visibility = View.GONE
        holder.blocReceivedMessage.visibility = View.GONE
        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blocReceivedImageMessage.visibility = View.GONE
            holder.blocUserImageMessage.visibility = View.VISIBLE
            holder.chatUserImageMessage.downloadAndSetImage(mListMessagesCache[position].imageUrl)
            holder.chatUserImageMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()

        } else {
            holder.blocReceivedImageMessage.visibility = View.VISIBLE
            holder.blocUserImageMessage.visibility = View.GONE
            holder.chatReceivedImageMessage.downloadAndSetImage(mListMessagesCache[position].imageUrl)
            holder.chatReceivedImageMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        }
    }

    private fun drawMessageText(holder: SingleChatHolder, position: Int) {
        holder.blocReceivedImageMessage.visibility = View.GONE
        holder.blocUserImageMessage.visibility = View.GONE
        if (mListMessagesCache[position].from == CURRENT_UID) {

            holder.blocUserMessage.visibility = View.VISIBLE
            holder.blocReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.chatUserMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        } else {
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mListMessagesCache[position].text
            holder.chatReceivedMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        }
    }

    fun addItemToBottom(
        item: CommonModel,
        onSuccess: () -> Unit
    ) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            notifyItemInserted(mListMessagesCache.size)
        }
        onSuccess()
    }

    fun addItemToTop(
        item: CommonModel,
        onSuccess: () -> Unit
    ) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timeStamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }
}