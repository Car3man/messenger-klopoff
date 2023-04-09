package com.klopoff.messenger_klopoff.HomeActivity.ChatFragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment.Chat
import com.klopoff.messenger_klopoff.Utils.BottomNavigationSupport
import com.klopoff.messenger_klopoff.Utils.DispatchableTouchEventFragment
import com.klopoff.messenger_klopoff.Utils.MarginItemDecoration
import com.klopoff.messenger_klopoff.Utils.Utils
import com.klopoff.messenger_klopoff.databinding.FragmentChatBinding
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

private const val CHAT_PARAM = "CHAT"

class ChatFragment : Fragment(), DispatchableTouchEventFragment, BottomNavigationSupport {

    private lateinit var adapter: ChatMessageAdapter
    private lateinit var binding: FragmentChatBinding
    private lateinit var chat: Chat
    private var messages: MutableList<ChatMessage> = mutableListOf()
    private var parentBottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chat = it.getParcelable(CHAT_PARAM)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.reverseLayout = true
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(requireContext())
                .setReverseLayout(layoutManager.reverseLayout)
                .setVerticalMargin(8)
        )
        adapter = ChatMessageAdapter(messages)
        binding.recyclerView.adapter = adapter

        // Apply padding to input field if soft keyboard is active
        binding.textInputMessage.applyInsetter {
            type (ime = true) {
                padding()
            }
        }

        // Hide parent bottom navigation bar if soft keyboard is active
        // Else show it but in small delay to prevent ui render glitches
        binding.textInputEditMessage.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) parentBottomNavigation!!.isGone = true
            else {
                lifecycleScope.launch {
                    delay(100)
                    if (!binding.textInputEditMessage.hasFocus()) {
                        parentBottomNavigation!!.isGone = false
                    }
                }
            }
        }

        binding.textInputMessage.setEndIconOnClickListener {
            val message = binding.textInputEditMessage.text.toString()

            Utils.hideSoftKeyboard(requireActivity())
            binding.textInputMessage.clearFocus()
            binding.textInputEditMessage.setText("")

            lifecycleScope.launch(Dispatchers.IO) {
                val auth = FirebaseAuth.getInstance()
                val database = FirebaseDatabase.getInstance()
                val currUserId = auth.uid!!
                val createdAt = Calendar.getInstance().timeInMillis
                val messageDTO = object {
                    val message = message
                    val sender = currUserId
                }

                database.reference
                    .child("chats")
                    .child(currUserId)
                    .child(chat.userId)
                    .child(createdAt.toString())
                    .setValue(messageDTO)
                    .await()

                database.reference
                    .child("chats")
                    .child(chat.userId)
                    .child(currUserId)
                    .child(createdAt.toString())
                    .setValue(messageDTO)
                    .await()
            }
        }

        loadMessages()

        return binding.root
    }

    private fun loadMessages() {
        lifecycleScope.launch(Dispatchers.IO) {
            val auth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance()
            val currUserId = auth.uid!!

            val loadedMessages = database.reference
                .child("chats")
                .child(currUserId)
                .child(chat.userId)
                .orderByKey()
                .get()
                .await()
                .children
                .map {
                    val createdAt = it.key!!.toLong()
                    val sender = it.child("sender").value as String
                    val mine = sender == currUserId
                    val message = it.child("message").value as String
                    ChatMessage(mine, message, createdAt)
                }

            messages.clear()
            messages.addAll(loadedMessages.asReversed())

            withContext(Dispatchers.Main) {
                binding.tvNothingFound.isVisible = messages.isEmpty()
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun dispatchTouchEvent(activity: Activity, event: MotionEvent) : Boolean {
        if (activity.currentFocus != null) {
            if (!Utils.isPointInsideView(event.rawX.toInt(), event.rawY.toInt(), binding.textInputEditMessage)) {
                binding.textInputEditMessage.clearFocus()
                Utils.hideSoftKeyboard(activity)
            }
        }
        return false
    }

    override fun onBottomNavigationProvided(bottomNavigation: BottomNavigationView) {
        parentBottomNavigation = bottomNavigation
    }

    companion object {
        @JvmStatic
        fun newInstance(chat: Chat) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CHAT_PARAM, chat)
                }
            }
    }
}