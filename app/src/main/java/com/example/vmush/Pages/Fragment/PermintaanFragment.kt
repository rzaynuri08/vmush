package com.example.vmush.Pages.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vmush.R
import com.example.vmush.adapter.PermintaanAdapter
import com.example.vmush.model.Permintaan

class PermintaanFragment : Fragment(), PermintaanAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var permintaanAdapter: PermintaanAdapter
    private val permintaanList = mutableListOf<Permintaan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_permintaan, container, false)

        recyclerView = view.findViewById(R.id.rvPermintaan)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Tambahkan data dummy
        permintaanList.add(
            Permintaan(
                name = "Sarah Wilson",
                time = "5 menit yang lalu",
                item = "10 kg Jamur Tiram",
                location = "Jl. Merdeka No. 123, Jakarta",
                profileImageRes = R.drawable.j // ganti sesuai drawable kamu
            )
        )
        permintaanList.add(
            Permintaan(
                name = "John Cooper",
                time = "15 menit yang lalu",
                item = "5 kg Jamur Tiram",
                location = "Jl. Sudirman No. 45, Jakarta",
                profileImageRes = R.drawable.i // ganti sesuai drawable kamu
            )
        )

        permintaanAdapter = PermintaanAdapter(permintaanList, this)
        recyclerView.adapter = permintaanAdapter

        return view
    }

    override fun onAcceptClicked(permintaan: Permintaan) {
        // Aksi kalau klik Terima
        // Contoh:
        println("Permintaan diterima: ${permintaan.name}")
    }

    override fun onDeclineClicked(permintaan: Permintaan) {
        // Aksi kalau klik Tolak
        // Contoh:
        println("Permintaan ditolak: ${permintaan.name}")
    }
}
