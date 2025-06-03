package com.example.vmush.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vmush.R
import com.example.vmush.model.Permintaan
import com.squareup.picasso.Picasso
import com.example.vmush.Pages.activity.DetailPermintaanActivity

class PermintaanAdapter(
    private val listPermintaan: List<Permintaan>
) : RecyclerView.Adapter<PermintaanAdapter.PermintaanViewHolder>() {

    inner class PermintaanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProfile: ImageView = itemView.findViewById(R.id.imageProfile)
        val tvNamaUser: TextView = itemView.findViewById(R.id.tvNamaUser)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvItem: TextView = itemView.findViewById(R.id.tvItem)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val btnAccept: Button = itemView.findViewById(R.id.btnAccept)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermintaanViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_permintaan, parent, false)
        return PermintaanViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PermintaanViewHolder, position: Int) {
        val currentPermintaan = listPermintaan[position]

        holder.tvNamaUser.text = currentPermintaan.username
        holder.tvTime.text = currentPermintaan.tanggal_permintaan
        holder.tvItem.text = "${currentPermintaan.jumlah_stok} stok jamur tiram"
        holder.tvLocation.text = currentPermintaan.alamat_permintaan

        Picasso.get()
            .load(currentPermintaan.gambar)
            .into(holder.imageProfile)

        // Intent langsung kirim semua data
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailPermintaanActivity::class.java)
            intent.putExtra("id_stok", currentPermintaan.id_stok)
            intent.putExtra("username", currentPermintaan.username)
            intent.putExtra("dibutuhkan", currentPermintaan.dibutuhkan)
            intent.putExtra("nohp", currentPermintaan.nohp)
            intent.putExtra("tanggal_permintaan", currentPermintaan.tanggal_permintaan)
            intent.putExtra("jumlah_stok", currentPermintaan.jumlah_stok)
            intent.putExtra("alamat_permintaan", currentPermintaan.alamat_permintaan)
            intent.putExtra("gambar", currentPermintaan.gambar)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listPermintaan.size
    }
}
