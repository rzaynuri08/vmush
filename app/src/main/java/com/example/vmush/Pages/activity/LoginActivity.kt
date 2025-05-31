package com.example.vmush.Pages.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vmush.MainActivity
import com.example.vmush.model.UserResponse
import com.example.vmush.Network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.vmush.R

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Check if user is already logged in
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val username = sharedPreferences.getString("username", "")
        if (isLoggedIn && !username.isNullOrEmpty()) {
            // If the user is already logged in, skip login and go directly to MainActivity
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the LoginActivity so the user can't go back to it using the back button
        }

        val edtUsername = findViewById<EditText>(R.id.etUsername)
        val edtPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                showErrorToast("Username dan Password tidak boleh kosong!")
                return@setOnClickListener
            }

            val call = RetrofitClient.apiService.getUsers()

            call.enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        val users = response.body()?.DataAkun // Access the users list from the response
                        val user = users?.find { it.username == username && it.pwasli == password }
                        if (user != null) {
                            // Login successful, save user info and move to MainActivity
                            Log.d("LoginActivity", "Saving username: ${user.username}") // Debugging line
                            with(sharedPreferences.edit()) {
                                putBoolean("isLoggedIn", true) // Save login status
                                putString("username", user.username) // Save username
                                apply() // You can replace with commit() for synchronous saving
                            }

                            // Navigate to MainActivity
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Close the login activity so the user can't go back
                            showSuccessToast("Login berhasil, selamat datang ${user.nama}!")
                        } else {
                            showErrorToast("Login gagal. Periksa username dan password Anda.")
                        }
                    } else {
                        showErrorToast("Gagal menghubungi server.")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    showErrorToast("Gagal terhubung ke server: ${t.message}")
                }
            })
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccessToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
