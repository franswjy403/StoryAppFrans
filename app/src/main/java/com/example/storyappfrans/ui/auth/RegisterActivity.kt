package com.example.storyappfrans.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyappfrans.R
import com.example.storyappfrans.ui.main.ApiViewModel
import com.example.storyappfrans.utils.ViewModelFactory
import com.example.storyappfrans.data.repository.Result

class RegisterActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var apiViewModel: ApiViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameEditText = findViewById(R.id.ed_register_name)
        emailEditText = findViewById(R.id.ed_register_email)
        passwordEditText = findViewById(R.id.ed_register_password)
        progressBar = findViewById(R.id.progress_bar_loading)

        val viewModelFactory = ViewModelFactory.getInstance()
        apiViewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        val registerButton: Button = findViewById(R.id.button_register)
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            performRegistration(name, email, password)
        }
    }

    private fun performRegistration(name: String, email: String, password: String) {
        apiViewModel.register(name, email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    val response = result.data
                    if (response.error == false) {
                        Toast.makeText(
                            this,
                            "Registration Success",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            response.message ?: "Registration failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    val errorMessage = result.error
                    Toast.makeText(
                        this,
                        "An error occurred: $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}