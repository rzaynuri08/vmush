package com.example.vmush.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vmush.R
import com.example.vmush.model.StatusHistoryModel

class StatusHistoryAdapter(private val historyList: List<StatusHistoryModel>) :
    RecyclerView.Adapter<StatusHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStatus: TextView = view.findViewById(R.id.tvHistoryStatus)
        val tvTime: TextView = view.findViewById(R.id.tvHistoryTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_status_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = historyList[position]
        holder.tvStatus.text = history.status
        holder.tvTime.text = history.time
    }

    override fun getItemCount(): Int = historyList.size
}
