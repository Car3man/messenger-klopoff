package com.klopoff.messenger_klopoff.HomeActivity.NewChatFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.klopoff.messenger_klopoff.Utils.MarginItemDecoration
import com.klopoff.messenger_klopoff.Utils.Utils
import com.klopoff.messenger_klopoff.databinding.FragmentNewChatBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class NewChatFragment : Fragment() {

    private lateinit var adapter: FoundedPersonAdapter
    private var foundedUsers: MutableList<FoundedPerson> = mutableListOf()
    private var foundedPersonClickListener: FoundedPersonItemClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewChatBinding.inflate(inflater, container, false)

        binding.searchInputText.setOnEditorActionListener { _, _, _ ->
            Utils.hideSoftKeyboard(requireActivity())
            binding.searchInputText.clearFocus()
            tryFindPersons(binding.searchInputText.editableText.toString())
            true
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(requireContext())
                .setReverseLayout(layoutManager.reverseLayout)
                .setVerticalMargin(16)
        )
        adapter = FoundedPersonAdapter(foundedUsers)
        adapter.setItemClickListener(foundedPersonClickListener)
        binding.recyclerView.adapter = adapter

        // Auto start editing search input text on start
        lifecycleScope.launch {
            delay(100)
            binding.searchInputText.requestFocus()
            val inputMethodManager: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(binding.searchInputText, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun tryFindPersons(userName: String) {
        foundedUsers.clear()

        if (userName.isBlank()) return

        lifecycleScope.launch(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val persons = database.reference
                .child("persons")
                .orderByChild("userName")
                .startAt(userName)
                .get()
                .await()
                .children
                .filter { (it.child("userName").value as String).contains(userName) }
                .map {
                    val personUserId = it.child("userId").value as String
                    val personUserName = it.child("userName").value as String
                    // TODO: implement loading user avatar
                    FoundedPerson(personUserId, personUserName, null)
                }

            withContext(Dispatchers.Main) {
                foundedUsers.addAll(persons)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun setFoundedPersonClickListener(listener: FoundedPersonItemClickListener?) {
        foundedPersonClickListener = listener
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NewChatFragment()
    }

    interface FoundedPersonItemClickListener {
        fun onClick(foundedPerson: FoundedPerson)
    }
}