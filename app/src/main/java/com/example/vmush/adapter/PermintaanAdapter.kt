package com.example.vmush.adapter

import com.example.vmush.model.Permintaan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.vmush.R
import androidx.recyclerview.widget.RecyclerView

class PermintaanAdapter(
    private val list: List<Permintaan>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PermintaanAdapter.PermintaanViewHolder>() {

    interface OnItemClickListener {
        fun onAcceptClicked(permintaan: Permintaan)
        fun onDeclineClicked(permintaan: Permintaan)
    }

    inner class PermintaanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProfile: ImageView = itemView.findViewById(R.id.imageProfile)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvItem: TextView = itemView.findViewById(R.id.tvItem)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val btnAccept: Button = itemView.findViewById(R.id.btnAccept)
        val btnDecline: Button = itemView.findViewById(R.id.btnDecline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermintaanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_permintaan, parent, false)
        return PermintaanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PermintaanViewHolder, position: Int) {
        val permintaan = list[position]
        holder.tvName.text = permintaan.name
        holder.tvTime.text = permintaan.time
        holder.tvItem.text = permintaan.item
        holder.tvLocation.text = permintaan.location

        // Placeholder untuk gambar profile
        holder.imageProfile.setImageResource(permintaan.profileImageRes)

        holder.btnAccept.setOnClickListener {
            listener.onAcceptClicked(permintaan)
        }

        holder.btnDecline.setOnClickListener {
            listener.onDeclineClicked(permintaan)
        }
    }

    override fun getItemCount(): Int = list.size
}

