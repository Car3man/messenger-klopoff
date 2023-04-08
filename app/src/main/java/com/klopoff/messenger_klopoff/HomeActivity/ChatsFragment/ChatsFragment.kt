package com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.klopoff.messenger_klopoff.databinding.FragmentChatBinding

class ChatsFragment : Fragment() {
    private var itemClickListener: ChatItemListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChatBinding.inflate(inflater, container, false)

        val chats = getDummyChats()
        val adapter = ChatAdapter(binding.root, chats)
        adapter.setItemClickListener(itemClickListener)
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    private fun getDummyChats(): List<Chat> {
        val messages = mutableListOf<Chat>()
        messages.add(Chat("userId", "TestUser1", null, null))
        messages.add(Chat("userId", "TestUser2", null, null))
        messages.add(Chat("userId", "TestUser3", null, null))
        return messages
    }

    fun setItemClickListener(listener: ChatItemListener) {
        itemClickListener = listener
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }

    interface ChatItemListener {
        fun onClicked(chat: Chat)
    }
}