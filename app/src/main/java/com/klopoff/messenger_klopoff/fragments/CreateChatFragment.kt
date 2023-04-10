package com.klopoff.messenger_klopoff.fragments

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
import com.klopoff.messenger_klopoff.models.Person
import com.klopoff.messenger_klopoff.adapters.PersonAdapter
import com.klopoff.messenger_klopoff.decorations.ItemMarginsDecoration
import com.klopoff.messenger_klopoff.databinding.FragmentNewChatBinding
import com.klopoff.messenger_klopoff.utils.InputUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class CreateChatFragment : Fragment() {

    private val persons: MutableList<Person> = mutableListOf()
    private val adapter: PersonAdapter = PersonAdapter(persons)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewChatBinding.inflate(inflater, container, false)

        binding.searchInputText.setOnEditorActionListener { _, _, _ ->
            InputUtils.hideSoftKeyboard(requireActivity())
            binding.searchInputText.clearFocus()
            findPersons(binding.searchInputText.editableText.toString())
            true
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            ItemMarginsDecoration(requireContext())
                .setReverseLayout(layoutManager.reverseLayout)
                .setVerticalMargin(16)
        )
        binding.recyclerView.adapter = adapter

        // Auto start editing search input text on start
        lifecycleScope.launch {
            delay(100)
            binding.searchInputText.requestFocus()
            val inputMethodManager: InputMethodManager = requireActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                binding.searchInputText, InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        return binding.root
    }

    private fun findPersons(userName: String) {
        if (userName.isBlank()) {
            persons.clear()
            return
        }

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
                    Person(personUserId, personUserName, null)
                }

            withContext(Dispatchers.Main) {
                this@CreateChatFragment.persons.clear()
                this@CreateChatFragment.persons.addAll(persons)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun setPersonClickListener(listener: PersonItemClickListener?) {
        adapter.setItemClickListener(listener)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CreateChatFragment()
    }

    interface PersonItemClickListener {
        fun onClick(foundedPerson: Person)
    }
}