package com.klopoff.messenger_klopoff.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.klopoff.messenger_klopoff.models.ChatMessage
import com.klopoff.messenger_klopoff.databinding.ItemChatMessageBinding
import com.klopoff.messenger_klopoff.databinding.ItemChatMessageOtherBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatMessageAdapter(
    private val messages: List<ChatMessage>
) : RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View,
        private var itemBinding: ItemChatMessageBinding?,
        private var itemOtherBinding: ItemChatMessageOtherBinding?
    ) : RecyclerView.ViewHolder(itemView) {
        constructor(itemBinding: ItemChatMessageBinding) : this(itemBinding.root, itemBinding, null)
        constructor(itemOtherBinding: ItemChatMessageOtherBinding) : this(itemOtherBinding.root, null, itemOtherBinding)

        fun bind(chatMessage: ChatMessage) {
            val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            with(chatMessage) {
                if (mine) {
                    itemBinding!!.tvMessage.text = message
                    itemBinding!!.tvTime.text = dateFormat.format(Date(createdAt)).toString()
                } else {
                    itemOtherBinding!!.tvMessage.text = message
                    itemOtherBinding!!.tvTime.text = dateFormat.format(Date(createdAt)).toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return if (viewType == 0) {
            val view = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(view)
        } else {
            val view = ItemChatMessageOtherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].mine) 0 else 1
    }
}