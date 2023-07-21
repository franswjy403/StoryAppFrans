package com.example.storyappfrans.ui.main

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappfrans.R
import com.example.storyappfrans.ui.auth.LoginActivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        deleteTokenFromSharedPrefs()
        navigateToLoginActivity()
    }

    private fun deleteTokenFromSharedPrefs() {
        val sharedPreferences = getSharedPreferences(
            getString(R.string.my_preferences),
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.remove(getString(R.string.key_token))
        editor.apply()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
