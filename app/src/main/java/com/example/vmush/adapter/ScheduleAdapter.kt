package com.example.vmush.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vmush.R
import com.example.vmush.model.DataPenjadwalan

class ScheduleAdapter(
    private val scheduleList: MutableList<DataPenjadwalan>,  // Changed to MutableList
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(schedule: DataPenjadwalan)
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
        val tvSubKeterangan: TextView = itemView.findViewById(R.id.tvSubKeterangan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = scheduleList[position]
        holder.tvKeterangan.text = schedule.keterangan
        holder.tvSubKeterangan.text = schedule.sub_keterangan

        holder.itemView.setOnClickListener {
            listener.onItemClicked(schedule)
        }
    }

    override fun getItemCount(): Int = scheduleList.size

    // Method to update the schedule list with new data (useful for clearing and adding new data)
    fun updateData(newScheduleList: List<DataPenjadwalan>) {
        scheduleList.clear() // Clear the current data
        scheduleList.addAll(newScheduleList) // Add new data
        notifyDataSetChanged() // Notify adapter that data has changed
    }
}
