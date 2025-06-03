package com.example.vmush.Pages.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vmush.R
import com.squareup.picasso.Picasso

class DetailPermintaanActivity : AppCompatActivity() {

    private lateinit var btnChat: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_permintaan)

        // Ambil data dari Intent
        val idStok = intent.getStringExtra("id_stok")
        val username = intent.getStringExtra("username")
        val tanggalPermintaan = intent.getStringExtra("tanggal_permintaan")
        val jumlahStok = intent.getStringExtra("jumlah_stok")
        val alamatPermintaan = intent.getStringExtra("alamat_permintaan")
        val gambarUrl = intent.getStringExtra("gambar")
        val nohp = intent.getStringExtra("nohp")
        val dibutuhkan = intent.getStringExtra("dibutuhkan")

        // Set data ke view
        val textName = findViewById<TextView>(R.id.textName)
        val textDate = findViewById<TextView>(R.id.textDate)
        val textQuantity = findViewById<TextView>(R.id.textQuantity)
        val textAddress = findViewById<TextView>(R.id.textAddress)
        val profilePicture = findViewById<ImageView>(R.id.profilePicture)

        textName.text = username ?: "-"
        textDate.text = dibutuhkan ?: "-"
        textQuantity.text = jumlahStok ?: "-"
        textAddress.text = alamatPermintaan ?: "-"

        // Load gambar pakai Picasso
        if (!gambarUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(gambarUrl)
                .into(profilePicture)
        }

        btnChat = findViewById(R.id.btnChat)
        btnChat.setOnClickListener {
            val url = "https://wa.me/$nohp"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

    }
}
