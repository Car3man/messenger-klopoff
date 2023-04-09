package com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.klopoff.messenger_klopoff.HomeActivity.NewChatFragment.FoundedPerson
import com.klopoff.messenger_klopoff.Utils.MarginItemDecoration
import com.klopoff.messenger_klopoff.databinding.FragmentChatsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

        loadChats()

        return binding.root
    }

    fun setChatItemClickListener(listener: ChatItemClickListener) {
        chatItemClickListener = listener
    }

    fun setNewChatButtonClickListener(listener: View.OnClickListener) {
        newChatButtonClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadChats() {
        lifecycleScope.launch(Dispatchers.IO) {
            val auth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance()

            val loadedChats = database.reference
                .child("chats")
                .child(auth.uid!!)
                .get()
                .await()
                .children
                .map {
                    val userId = it.key
                    // TODO: implement loading user avatar
                    // TODO: implement loading last message
                    val userName = database.reference
                        .child("persons")
                        .child(userId!!)
                        .child("userName")
                        .get()
                        .await()
                        .value as String
                    Chat(userId, userName, null, null)
                }

            chats.clear()
            chats.addAll(loadedChats)

            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }

    interface ChatItemClickListener {
        fun onClick(chat: Chat)
    }
}