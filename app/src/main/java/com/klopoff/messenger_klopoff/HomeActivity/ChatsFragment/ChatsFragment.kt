package com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.klopoff.messenger_klopoff.Utils.MarginItemDecoration
import com.klopoff.messenger_klopoff.databinding.FragmentChatsBinding

class ChatsFragment : Fragment() {

    private lateinit var adapter: ChatAdapter
    private var chats: MutableList<Chat> = mutableListOf()
    private var chatItemClickListener: ChatItemClickListener? = null
    private var newChatButtonClickListener: View.OnClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChatsBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(requireContext())
                .setReverseLayout(layoutManager.reverseLayout)
                .setVerticalMargin(16)
        )
        adapter = ChatAdapter(binding.root, chats)
        adapter.setItemClickListener(chatItemClickListener)
        binding.recyclerView.adapter = adapter

        newChatButtonClickListener?.let { binding.fabChats.setOnClickListener(it) }

        return binding.root
    }

    fun setChatItemClickListener(listener: ChatItemClickListener) {
        chatItemClickListener = listener
    }

    fun setNewChatButtonClickListener(listener: View.OnClickListener) {
        newChatButtonClickListener = listener
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }

    interface ChatItemClickListener {
        fun onClick(chat: Chat)
    }
}