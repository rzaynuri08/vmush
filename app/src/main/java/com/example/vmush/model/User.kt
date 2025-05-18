package com.example.vmush.model

data class UserResponse(
    val DataAkun: List<User> // List of users from the API
)

data class User(
    val username: String,
    val nama: String,
    val email: String,
    val password: String,
    val pwasli: String,
    val status: String,
    val gambar: String,
    val alamat: String,
    val nohp: String,
    val tanggal_create: String,
    val status_akun: String
)
