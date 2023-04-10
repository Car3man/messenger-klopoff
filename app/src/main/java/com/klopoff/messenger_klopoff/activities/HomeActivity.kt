package com.klopoff.messenger_klopoff.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import com.klopoff.messenger_klopoff.fragments.ChatFragment
import com.klopoff.messenger_klopoff.models.Chat
import com.klopoff.messenger_klopoff.fragments.ChatsFragment
import com.klopoff.messenger_klopoff.models.Person
import com.klopoff.messenger_klopoff.fragments.ProfileFragment
import com.klopoff.messenger_klopoff.fragments.SettingsFragment
import com.klopoff.messenger_klopoff.fragments.CreateChatFragment
import com.klopoff.messenger_klopoff.R
import com.klopoff.messenger_klopoff.fragments.interfaces.BottomNavigationSupport
import com.klopoff.messenger_klopoff.fragments.interfaces.DispatchTouchEventSupport
import com.klopoff.messenger_klopoff.databinding.ActivityHomeBinding
import dev.chrisbanes.insetter.applyInsetter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.root.applyInsetter {
            type(statusBars = true) {
                margin(vertical = true)
            }
        }
        binding.bottomNavigation.applyInsetter {
            type(navigationBars = true) {
                padding()
            }
            type(ime = true) {}
        }
        setContentView(binding.root)

        supportFragmentManager.commit {
            replace(R.id.fragmentContainerView, ProfileFragment.newInstance())
        }
        binding.bottomNavigation.setOnItemSelectedListener { onNavigationItemClick(it) }
        binding.bottomNavigation.selectedItemId = R.id.itemChats
    }

    private fun onNavigationItemClick(it: MenuItem): Boolean {
        val nextFragment = when (it.itemId) {
            R.id.itemProfile -> ProfileFragment.newInstance()
            R.id.itemChats -> createChatsFragment()
            R.id.itemSettings -> SettingsFragment.newInstance()
            else -> throw Exception("Out of bottom navigation items")
        }

        supportFragmentManager.popBackStack()
        supportFragmentManager.commit {
            replace(R.id.fragmentContainerView, nextFragment)
        }
        return true
    }

    private fun onChatItemClick(chat: Chat) {
        val chatFragment = ChatFragment.newInstance(chat)
        val bottomNavigationSupport = chatFragment as BottomNavigationSupport
        bottomNavigationSupport.onBottomNavigationProvided(binding.bottomNavigation)

        supportFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragmentContainerView, chatFragment)
        }
    }

    private fun createChatsFragment(): ChatsFragment {
        val chatsFragment = ChatsFragment.newInstance(this)

        chatsFragment.setChatItemClickListener(object : ChatsFragment.ChatItemClickListener {
            override fun onClick(chat: Chat) {
                onChatItemClick(chat)
            }
        })

        chatsFragment.setNewChatButtonClickListener {
            val createChatFragment = CreateChatFragment.newInstance()
            createChatFragment.setPersonClickListener(object : CreateChatFragment.PersonItemClickListener {
                override fun onClick(foundedPerson: Person) {
                    createNewChatWithPerson(foundedPerson)
                }
            })

            supportFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.fragmentContainerView, createChatFragment)
            }
        }
        return chatsFragment
    }

    private fun createNewChatWithPerson(foundedPerson: Person) {
        supportFragmentManager.popBackStack().also {
            onChatItemClick(
                Chat(
                foundedPerson.userId,
                foundedPerson.userName,
                foundedPerson.userAvatar,
                null)
            )
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val fragments = supportFragmentManager.fragments
        if (fragments.size > 0 && fragments[fragments.size - 1] is DispatchTouchEventSupport) {
            val fragment = fragments[fragments.size - 1] as DispatchTouchEventSupport
            if (fragment.dispatchTouchEvent(this, ev)) return true
        }
        return super.dispatchTouchEvent(ev)
    }
}