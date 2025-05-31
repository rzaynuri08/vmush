package com.example.vmush.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vmush.R
import com.example.vmush.adapter.ScheduleAdapter
import com.example.vmush.model.DataPenjadwalan
import com.example.vmush.Network.RetrofitClient
import com.example.vmush.Network.ApiService
import com.example.vmush.model.DataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarFragment : Fragment() {

    private val apiService: ApiService = RetrofitClient.apiService
    private val scheduleList: MutableList<DataPenjadwalan> = mutableListOf()
    private lateinit var adapter: ScheduleAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var calendarView: CalendarView

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        calendarView = view.findViewById(R.id.calendarView)

        // Initialize the RecyclerView and Adapter
        adapter = ScheduleAdapter(scheduleList, object : ScheduleAdapter.OnItemClickListener {
            override fun onItemClicked(schedule: DataPenjadwalan) {
                Toast.makeText(requireContext(), "Item clicked: ${schedule.keterangan}", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Handle calendar date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            val username = sharedPreferences.getString("username", null)

            Log.d("CalendarFragment", "Selected date: $selectedDate")
            Log.d("CalendarFragment", "Username from SharedPreferences: $username")

            if (!username.isNullOrEmpty()) {
                fetchDataForDate(username, selectedDate)
            } else {
                Toast.makeText(requireContext(), "No username found. Please log in again.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun fetchDataForDate(username: String, tanggal: String) {
        // Make the API call using Retrofit
        apiService.getDataPenjadwalan(username, tanggal).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.DataPenjadwalan ?: emptyList()

                    // Update the adapter with new data or show an empty list
                    adapter.updateData(data)

                    // If no data found, show a message
                    if (data.isEmpty()) {
                        Toast.makeText(requireContext(), "No data for selected date", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error fetching data: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e("CalendarFragment", "Error fetching data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("CalendarFragment", "Network error: ${t.message}", t)
            }
        })
    }
}
