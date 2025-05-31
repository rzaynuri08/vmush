package com.example.vmush.Pages.Fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.vmush.Pages.activity.LoginActivity
import com.example.vmush.R
import com.example.vmush.Network.RetrofitClient
import com.squareup.picasso.Picasso
import com.example.vmush.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile_Fragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnLogout: Button
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvFullNameLabel: TextView
    private lateinit var tvPhoneLabel: TextView
    private lateinit var tvLocationLabel: TextView
    private lateinit var ivProfilePic: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences to handle login state
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_, container, false)

        // Initialize the UI elements
        btnLogout = view.findViewById(R.id.btnLogout)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvFullNameLabel = view.findViewById(R.id.tvFullNameValue)
        tvPhoneLabel = view.findViewById(R.id.tvPhoneValue)
        tvLocationLabel = view.findViewById(R.id.tvLocationValue)
        ivProfilePic = view.findViewById(R.id.ivProfilePic)

        // Fetch the saved username from SharedPreferences
        val username = sharedPreferences.getString("username", "")

        if (!username.isNullOrEmpty()) {
            // Call API to fetch user data if the username is available
            fetchUserData(username)
        }

        // Handle Logout Button click
        btnLogout.setOnClickListener {
            // Clear login state and username from SharedPreferences
            with(sharedPreferences.edit()) {
                putBoolean("isLoggedIn", false)  // Set login state to false
                remove("username")  // Remove the saved username
                apply()  // Apply changes
            }

            // Navigate back to LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Close Profile_Fragment so the user can't go back to it using the back button
        }

        return view
    }

    private fun fetchUserData(username: String) {
        // Make the API call to fetch the user data based on username
        val call = RetrofitClient.apiService.getUserByUsername(username)  // Pass username to the API endpoint

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()?.DataAkun?.firstOrNull() // Assuming you only have one user in the response
                    if (user != null) {
                        // Update the UI with user information
                        tvName.text = user.nama
                        tvEmail.text = user.email
                        tvFullNameLabel.text = user.nama
                        tvPhoneLabel.text = user.nohp
                        tvLocationLabel.text = user.alamat

                        Picasso.get()
                            .load(user.gambar) // The URL of the image
                            .into(ivProfilePic)

                    }
                } else {
                    // Handle error case (e.g., server error)
                    showErrorToast("Failed to fetch user data.")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // Handle failure (e.g., network error)
                showErrorToast("Error: ${t.message}")
            }
        })
    }

    private fun showErrorToast(message: String) {
        // Show a toast message for errors
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
