package com.klopoff.messenger_klopoff.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.klopoff.messenger_klopoff.models.OnBoardPage
import com.klopoff.messenger_klopoff.adapters.OnBoardPageAdapter
import com.klopoff.messenger_klopoff.R
import com.klopoff.messenger_klopoff.databinding.ActivityOnboardBinding
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class OnBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardBinding
    private lateinit var auth: FirebaseAuth
    private val pages: List<OnBoardPage> = listOf(
        OnBoardPage(R.drawable.thumbnail_onboard_page1, "First", "First onBoard page short description"),
        OnBoardPage(R.drawable.thumbnail_onboard_page2, "Second", "Second onBoard page short description"),
        OnBoardPage(R.drawable.thumbnail_onboard_page3, "Third", "Third onBoard page short description"),
        OnBoardPage(R.drawable.thumbnail_onboard_page4, "Four", "Four onBoard page short description"),
        OnBoardPage(R.drawable.thumbnail_onboard_page5, "Fifth", "Fifth onBoard page short description")
    )
    private val adapter: OnBoardPageAdapter = OnBoardPageAdapter(pages)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        // if we already login then switch to home activity
        if (currentUser != null) {
            // check if maybe user deleted/disabled
            // else start home immediately
            var switchToHome = false

            runBlocking {
                try {
                    currentUser.getIdToken(true).await()
                    switchToHome = true
                } catch (exception: Exception) {
                    auth.signOut()
                    Log.e(
                        OnBoardActivity::class.java.name,
                        "Refresh token exception: ${exception.message}"
                    )
                }
            }

            if (switchToHome) {
                Intent(this, HomeActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
                return
            }
        }

        val sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)

        // if this no first time user experience switch to sign in activity
        val ftue = sharedPreferences.getBoolean("FTUE", false)
        if (ftue) {
            Intent(this, SignInActivity::class.java).also {
                startActivity(it)
                finish()
            }
            return
        }

        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vpOnBoardPages.adapter = adapter
        TabLayoutMediator(binding.tlOnBoardPages, binding.vpOnBoardPages) { _, _ -> }.attach()

        binding.btnStartMessaging.setOnClickListener {
            val sharedPreferencesEditor = sharedPreferences.edit()
            sharedPreferencesEditor.putBoolean("FTUE", true)
            sharedPreferencesEditor.apply()

            Intent(this, SignInActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}