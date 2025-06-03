package com.example.vmush.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vmush.R
import com.example.vmush.model.KumbungModel

class KumbungAdapter(
    private val listKumbung: List<KumbungModel>,
    private val onItemClick: (KumbungModel) -> Unit
) : RecyclerView.Adapter<KumbungAdapter.KumbungViewHolder>() {

    inner class KumbungViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvKumbungName: TextView = itemView.findViewById(R.id.tvKumbungName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvTemperature: TextView = itemView.findViewById(R.id.tvTemperature)
        val tvHumidity: TextView = itemView.findViewById(R.id.tvHumidity)
        val tvGrowth: TextView = itemView.findViewById(R.id.tvGrowth)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KumbungViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kumbung, parent, false)
        return KumbungViewHolder(view)
    }

    override fun onBindViewHolder(holder: KumbungViewHolder, position: Int) {
        val kumbung = listKumbung[position]

        holder.tvKumbungName.text = kumbung.name
        holder.tvTemperature.text = kumbung.temperature
        holder.tvHumidity.text = kumbung.humidity
        holder.tvGrowth.text = kumbung.growth
        holder.tvStatus.text = if (kumbung.isActive) "Active" else "Inactive"

        holder.itemView.setOnClickListener {
            onItemClick(kumbung)
        }
    }

    override fun getItemCount(): Int = listKumbung.size
}
