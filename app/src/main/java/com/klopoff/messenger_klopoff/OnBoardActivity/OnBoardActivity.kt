package com.klopoff.messenger_klopoff.OnBoardActivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.klopoff.messenger_klopoff.HomeActivity.HomeActivity
import com.klopoff.messenger_klopoff.R
import com.klopoff.messenger_klopoff.SignInActivity
import com.klopoff.messenger_klopoff.databinding.ActivityOnboardBinding

class OnBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        // if we already login then switch to home activity
        if (currentUser != null) {
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
            return
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

        val pages = mutableListOf<OnBoardPage>()
        pages.add(OnBoardPage(R.drawable.onboard_page_thumbnail1, "First","First onBoard page short description"))
        pages.add(OnBoardPage(R.drawable.onboard_page_thumbnail2, "Second","Second onBoard page short description"))
        pages.add(OnBoardPage(R.drawable.onboard_page_thumbnail3, "Third","Third onBoard page short description"))
        pages.add(OnBoardPage(R.drawable.onboard_page_thumbnail4, "Four","Four onBoard page short description"))
        pages.add(OnBoardPage(R.drawable.onboard_page_thumbnail5, "Fifth","Fifth onBoard page short description"))

        binding.vpOnBoardPages.adapter = OnBoardPageAdapter(pages)
        TabLayoutMediator(binding.tlOnBoardPages, binding.vpOnBoardPages) { _,_ -> }.attach()
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