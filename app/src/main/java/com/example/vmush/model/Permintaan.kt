package com.example.vmush.model

data class Permintaan(
    val id_stok: String,
    val username: String,
    val jumlah_stok: String,
    val alamat_permintaan: String,
    val status_permintaan: String,
    val tanggal_permintaan: String,
    val dibutuhkan: String,
    val user: String?,
    val gambar: String,
    val nohp: String
)

data class DataPermintaan(
    val DataPermintaan: List<Permintaan> // DataPermintaan is a list of Permintaan
)
