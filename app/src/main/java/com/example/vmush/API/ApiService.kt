// ApiService.kt
package com.example.vmush.Network

import com.example.vmush.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("akun/user/tampil")
    fun getUsers(): Call<UserResponse> // This will return a UserResponse object, not a List<User>
}
