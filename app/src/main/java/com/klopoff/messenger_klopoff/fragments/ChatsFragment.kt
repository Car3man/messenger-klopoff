package com.klopoff.messenger_klopoff.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.klopoff.messenger_klopoff.models.Chat
import com.klopoff.messenger_klopoff.adapters.ChatAdapter
import com.klopoff.messenger_klopoff.decorations.ItemMarginsDecoration
import com.klopoff.messenger_klopoff.databinding.FragmentChatsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ChatsFragment private constructor(context: Context) : Fragment() {

    private val auth: FirebaseAuth = Firebase.auth
    private val database: FirebaseDatabase = Firebase.database
    private val chats: MutableList<Chat> = mutableListOf()
    private val adapter: ChatAdapter = ChatAdapter(context, chats)

    private var newChatButtonClickListener: View.OnClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChatsBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            ItemMarginsDecoration(requireContext())
                .setReverseLayout(layoutManager.reverseLayout)
                .setVerticalMargin(16)
        )
        binding.recyclerView.adapter = adapter

        binding.fabChats.setOnClickListener(newChatButtonClickListener)

        loadChats()

        return binding.root
    }

    fun setChatItemClickListener(listener: ChatItemClickListener) {
        adapter.setItemClickListener(listener)
    }

    fun setNewChatButtonClickListener(listener: View.OnClickListener) {
        newChatButtonClickListener = listener
    }

    private fun loadChats() {
        lifecycleScope.launch(Dispatchers.IO) {
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
                        .await().value as String
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
        fun newInstance(context: Context) = ChatsFragment(context)
    }

    interface ChatItemClickListener {
        fun onClick(chat: Chat)
    }
}