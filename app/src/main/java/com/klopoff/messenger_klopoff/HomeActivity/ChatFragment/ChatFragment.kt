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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

    private val auth: FirebaseAuth = Firebase.auth
    private val database: FirebaseDatabase = Firebase.database
    private val messages: MutableList<ChatMessage> = mutableListOf()
    private val adapter: ChatMessageAdapter = ChatMessageAdapter(messages)

    private lateinit var binding: FragmentChatBinding
    private lateinit var chat: Chat

    private var parentBottomNavigation: BottomNavigationView? = null
    private var chatUpdateListener: ChildEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            chat = it.getParcelable(CHAT_PARAM)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        binding.recyclerView.adapter = adapter

        // Apply padding to input field if soft keyboard is active
        binding.textInputMessage.applyInsetter {
            type(ime = true) {
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

            binding.textInputEditMessage.setText("")

            lifecycleScope.launch(Dispatchers.IO) {
                val createdAt = Calendar.getInstance().timeInMillis
                val messageDTO = object {
                    val message = message
                    val sender = auth.uid!!
                }

                database.reference
                    .child("chats")
                    .child(auth.uid!!)
                    .child(chat.userId)
                    .child(createdAt.toString())
                    .setValue(messageDTO)
                    .await()

                database.reference
                    .child("chats")
                    .child(chat.userId)
                    .child(auth.uid!!)
                    .child(createdAt.toString())
                    .setValue(messageDTO)
                    .await()
            }
        }

        subscribeToChatUpdates()
        loadMessages()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        unsubscribeToChatUpdates()
    }

    private fun loadMessages() {
        lifecycleScope.launch(Dispatchers.IO) {
            val loadedMessages =
                database.reference
                    .child("chats")
                    .child(auth.uid!!)
                    .child(chat.userId)
                    .orderByKey()
                    .get()
                    .await()
                    .children
                    .map {
                        val createdAt = it.key!!.toLong()
                        val sender = it.child("sender").value as String
                        val mine = sender == auth.uid!!
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

    private fun subscribeToChatUpdates() {
        chatUpdateListener = database.reference
            .child("chats")
            .child(auth.uid!!)
            .child(chat.userId)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val createdAt = snapshot.key!!.toLong()
                    val sender = snapshot.child("sender").value as String
                    val mine = sender == auth.uid!!
                    val message = snapshot.child("message").value as String

                    messages.add(0, ChatMessage(mine, message, createdAt))
                    adapter.notifyItemInserted(0)
                    binding.recyclerView.smoothScrollToPosition(0)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val createdAt = snapshot.key!!.toLong()

                    val removeIndex = messages.indexOfFirst { it.createdAt == createdAt }
                    if (removeIndex != -1) {
                        messages.removeAt(removeIndex)
                        adapter.notifyItemRemoved(removeIndex)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun unsubscribeToChatUpdates() {
        database.reference
            .child("chats")
            .child(auth.uid!!)
            .child(chat.userId)
            .removeEventListener(chatUpdateListener!!)
    }

    override fun dispatchTouchEvent(activity: Activity, event: MotionEvent): Boolean {
        if (activity.currentFocus != null) {
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            if (!Utils.isPointInsideView(x, y, binding.textInputEditMessage)) {
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
        fun newInstance(chat: Chat) = ChatFragment().apply {
            arguments = Bundle().apply {
                putParcelable(CHAT_PARAM, chat)
            }
        }
    }
}