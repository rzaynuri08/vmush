package com.example.vmush.Pages.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vmush.Network.RetrofitClient
import com.example.vmush.R
import com.example.vmush.model.AddCatatanRequest
import com.example.vmush.model.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCalenderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_calender)

        // Handle edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi tombol
        val btnAccept = findViewById<Button>(R.id.btnAccept)

        btnAccept.setOnClickListener {
            val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val username = sharedPref.getString("username", "") ?: ""

            val tanggal = intent.getStringExtra("selected_date") ?: ""
            val keterangan = findViewById<EditText>(R.id.keteranganEditText).text.toString()
            val subKeterangan = findViewById<EditText>(R.id.subKeteranganEditText).text.toString()

            if (keterangan.isBlank() || subKeterangan.isBlank()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = AddCatatanRequest(
                username = username,
                tanggal = tanggal,
                keterangan = keterangan,
                sub_keterangan = subKeterangan
            )

            RetrofitClient.apiService.tambahCatatan(request).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        Toast.makeText(this@AddCalenderActivity, "Catatan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddCalenderActivity, "Catatan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    Toast.makeText(this@AddCalenderActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
