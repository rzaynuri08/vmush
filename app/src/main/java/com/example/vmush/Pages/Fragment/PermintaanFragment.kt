package com.example.vmush.Pages.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vmush.Network.RetrofitClient
import com.example.vmush.R
import com.example.vmush.adapter.PermintaanAdapter
import com.example.vmush.model.DataPermintaan
import com.example.vmush.Pages.activity.DetailPermintaanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PermintaanFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_permintaan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvPermintaan)
        recyclerView.layoutManager = LinearLayoutManager(context)

        RetrofitClient.apiService.getDataPermintaan().enqueue(object : Callback<DataPermintaan> {
            override fun onResponse(call: Call<DataPermintaan>, response: Response<DataPermintaan>) {
                if (response.isSuccessful) {
                    val permintaanList = response.body()?.DataPermintaan ?: emptyList()
                    recyclerView.adapter = PermintaanAdapter(permintaanList)
                } else {
                    Log.e("API Error", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DataPermintaan>, t: Throwable) {
                Log.e("API Failure", t.message ?: "Unknown error")
            }
        })
    }
}
