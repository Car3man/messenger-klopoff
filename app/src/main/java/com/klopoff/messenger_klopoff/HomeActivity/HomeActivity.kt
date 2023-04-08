package com.klopoff.messenger_klopoff.HomeActivity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import com.klopoff.messenger_klopoff.HomeActivity.ChatFragment.ChatFragment
import com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment.Chat
import com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment.ChatsFragment
import com.klopoff.messenger_klopoff.R
import com.klopoff.messenger_klopoff.databinding.ActivityHomeBinding
import dev.chrisbanes.insetter.applyInsetter

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.root.applyInsetter {
            type (statusBars = true) {
                margin(vertical = true)
            }
        }
        setContentView(binding.root)

        supportFragmentManager.commit {
            replace(R.id.fragmentContainerView, ProfileFragment.newInstance())
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            onNavigationItemClick(it)
        }
        binding.bottomNavigation.selectedItemId = R.id.itemChats
    }

    private fun onNavigationItemClick(it: MenuItem): Boolean {
        val nextFragment = when (it.itemId) {
            R.id.itemProfile -> ProfileFragment.newInstance()
            R.id.itemChats -> {
                val chatsFragment = ChatsFragment.newInstance()
                chatsFragment.setItemClickListener(object: ChatsFragment.ChatItemListener {
                    override fun onClicked(chat: Chat) {
                        onChatItemClick(chat)
                    }
                })
                chatsFragment
            }
            R.id.itemSettings -> SettingsFragment.newInstance()
            else -> throw Exception("Out of bottom navigation items")
        }
        supportFragmentManager.commit {
            replace(R.id.fragmentContainerView, nextFragment)
        }
        return true
    }

    private fun onChatItemClick(it: Chat) {
        supportFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragmentContainerView, ChatFragment.newInstance(it))
        }
    }
}