package com.example.vmush.model

data class DataPenjadwalan(
    val id_penjadwalan: String,
    val username: String,
    val tanggal: String,
    val keterangan: String,
    val sub_keterangan: String
)

data class DataResponse(
    val DataPenjadwalan: List<DataPenjadwalan>
)
