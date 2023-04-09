package com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.klopoff.messenger_klopoff.R
import com.klopoff.messenger_klopoff.databinding.ItemChatBinding

class ChatAdapter(
    private val parent: ViewGroup,
    private val chats: List<Chat>
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private var itemClickListener: ChatsFragment.ChatItemClickListener? = null

    inner class ViewHolder(
        private var itemBinding: ItemChatBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(chat: Chat) {
            with(chat) {
                itemBinding.tvUsername.text = userName

                if (userAvatar != null) {
                    itemBinding.ivAvatar.setImageBitmap(userAvatar)
                } else {
                    itemBinding.ivAvatar.setImageResource(R.drawable.ic_avatar_placeholder)
                }

                if (lastMessage != null) {
                    itemBinding.tvLastMessage.text = lastMessage!!.message
                } else {
                    itemBinding.tvLastMessage.text = parent.context.getString(R.string.no_messages)
                }

                itemBinding.root.setOnClickListener {
                    itemClickListener?.onClick(this)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        holder.bind(chat)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun setItemClickListener(listener: ChatsFragment.ChatItemClickListener?) {
        itemClickListener = listener
    }
}