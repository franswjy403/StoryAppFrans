package com.example.storyappfrans.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyappfrans.R
import com.example.storyappfrans.ui.auth.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isLoggedIn()) {
            navigateToStoryListActivity()
        } else {
            navigateToLoginActivity()
        }
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = this.getSharedPreferences(
            getString(R.string.my_preferences),
            Context.MODE_PRIVATE
        )
        val token = sharedPreferences.getString(getString(R.string.key_token), null)
        return !token.isNullOrEmpty()
    }

    private fun navigateToStoryListActivity() {
        startActivity(Intent(this, StoryListActivity::class.java))
        finish()
    }

    private fun navigateToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}