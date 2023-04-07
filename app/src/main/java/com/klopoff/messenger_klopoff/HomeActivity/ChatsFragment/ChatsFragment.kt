package com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.klopoff.messenger_klopoff.databinding.FragmentChatsBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.*
import java.util.*

class ChatsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChatsBinding.inflate(inflater, container, false)

        val messages = getDummyMessages()
        val adapter = ChatMessageAdapter(messages)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter

        return binding.root
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
        return messages
    }

    private fun getMessageTime(hours: Int, minutes: Int): Long {
        val calendar = GregorianCalendar(2023, 4, 6, hours, minutes, 0)
        return calendar.timeInMillis
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }
}