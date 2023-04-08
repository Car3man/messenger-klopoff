package com.klopoff.messenger_klopoff.HomeActivity.ChatFragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment.Chat
import com.klopoff.messenger_klopoff.Utils.BottomNavigationSupport
import com.klopoff.messenger_klopoff.Utils.DispatchableTouchEventFragment
import com.klopoff.messenger_klopoff.Utils.MarginItemDecoration
import com.klopoff.messenger_klopoff.Utils.SoftKeyboardUtils
import com.klopoff.messenger_klopoff.databinding.FragmentChatBinding
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

private const val CHAT_PARAM = "CHAT"

class ChatFragment : Fragment(), DispatchableTouchEventFragment, BottomNavigationSupport {

    private lateinit var binding: FragmentChatBinding
    private var parentBottomNavigation: BottomNavigationView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        val messages = getDummyMessages()
        val adapter = ChatMessageAdapter(messages)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.reverseLayout = true
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(requireContext())
                .setReverseLayout(layoutManager.reverseLayout)
                .setVerticalMargin(8)
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            val lastVisibleItemPositions = IntArray(1)

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy >= 0) return
                layoutManager.findLastVisibleItemPositions(lastVisibleItemPositions)
                if (lastVisibleItemPositions[0] == RecyclerView.NO_POSITION) return
                // TODO: load more items
            }
        })

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
            // TODO: Send message
        }

        return binding.root
    }

    override fun dispatchTouchEvent(activity: Activity, event: MotionEvent) : Boolean {
        if (activity.currentFocus != null) {
            binding.textInputEditMessage.clearFocus()
            SoftKeyboardUtils.hideSoftKeyboard(activity)
        }
        return false
    }

    override fun onBottomNavigationProvided(bottomNavigation: BottomNavigationView) {
        parentBottomNavigation = bottomNavigation
    }

    private fun getDummyMessages(): List<ChatMessage> {
        val messages = mutableListOf<ChatMessage>()
        messages.add(ChatMessage(false, "That's good idea sir. I'd like to improve their skill.", getMessageTime(16,30)))
        messages.add(ChatMessage(true, "Where are your ? I have something new for you.", getMessageTime(16,37)))
        messages.add(ChatMessage(false, "I still finishing my project. What happen ?", getMessageTime(16,45)))
        messages.add(ChatMessage(true, "I bring a new bag for you! I buy this one because 2 days ago i went to Paris.", getMessageTime(17,3)))
        messages.add(ChatMessage(false, "What ?? Thank you very much. I'll be at home at 7 pm.", getMessageTime(17,25)))
        messages.add(ChatMessage(true, "That's great. Have a nice day!", getMessageTime(17,47)))
        messages.add(ChatMessage(false, "That's good idea sir. I'd like to improve their skill.", getMessageTime(16,30)))
        messages.add(ChatMessage(true, "Where are your ? I have something new for you.", getMessageTime(16,37)))
        messages.add(ChatMessage(false, "I still finishing my project. What happen ?", getMessageTime(16,45)))
        messages.add(ChatMessage(true, "I bring a new bag for you! I buy this one because 2 days ago i went to Paris.", getMessageTime(17,3)))
        messages.add(ChatMessage(false, "What ?? Thank you very much. I'll be at home at 7 pm.", getMessageTime(17,25)))
        messages.add(ChatMessage(true, "That's great. Have a nice day!", getMessageTime(17,47)))
        messages.reverse()
        return messages
    }

    private fun getMessageTime(hours: Int, minutes: Int): Long {
        val calendar = GregorianCalendar(2023, 4, 6, hours, minutes, 0)
        return calendar.timeInMillis
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