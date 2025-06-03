// ApiService.kt
package com.example.vmush.Network

import com.example.vmush.model.AddCatatanRequest
import com.example.vmush.model.BasicResponse
import com.example.vmush.model.UserResponse
import com.example.vmush.model.DataResponse
import com.example.vmush.model.DataPermintaan
import com.example.vmush.model.LinkFirebaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("akun/user/tampil")
    fun getUsers(): Call<UserResponse>

    @GET("akun/user/tampil/{username}")
    fun getUserByUsername(@Path("username") username: String): Call<UserResponse>

    @GET("Data/Penjadwalan/data-tampil/{username}/{tanggal}")
    fun getDataPenjadwalan(
        @Path("username") username: String,
        @Path("tanggal") tanggal: String
    ): Call<DataResponse>

    @GET("Data/Link-Firebase/tampil/{username}")
    fun getFirebaseLinks(@Path("username") username: String): Call<LinkFirebaseResponse>

    @GET("Data/Permintaan/data-tampil")
    fun getDataPermintaan(): Call<DataPermintaan>

    @POST("Data/Penjadwalan/tambah-Data")
    fun tambahCatatan(@Body request: AddCatatanRequest): Call<BasicResponse>

    @GET("Data/Permintaan/edit-Data/id/{id_stok}")
    fun getDetailPermintaan(
        @Path("id_stok") idStok: String
    ): Call<DataPermintaan>
}
