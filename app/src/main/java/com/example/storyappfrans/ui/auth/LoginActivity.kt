package com.example.storyappfrans.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyappfrans.R
import com.example.storyappfrans.data.repository.Result
import com.example.storyappfrans.ui.main.ApiViewModel
import com.example.storyappfrans.ui.main.StoryListActivity
import com.example.storyappfrans.utils.ViewModelFactory
import com.example.storyappfrans.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var apiViewModel: ApiViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEditText = binding.edLoginEmail
        passwordEditText = binding.edLoginPassword
        progressBar = binding.progressBarLoading

        val viewModelFactory = ViewModelFactory.getInstance()
        apiViewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        val loginButton: Button = binding.buttonLogin
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            performLogin(email, password)
        }

        val tvSignup: TextView = findViewById(R.id.tv_signup)
        tvSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin(email: String, password: String) {
        apiViewModel.login(email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    val token = result.data.loginResult?.token

                    saveToken(this, token)

                    val intent = Intent(this, StoryListActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    val errorMessage = result.error
                    Toast.makeText(
                        this,
                        "Terjadi kesalahan $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun saveToken(context: Context, token: String?) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            getString(R.string.my_preferences),
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(getString(R.string.key_token), token)
        editor.apply()
    }
}
