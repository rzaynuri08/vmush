package com.example.vmush.Pages.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vmush.Pages.activity.DetailKumbungActivity
import com.example.vmush.R
import com.example.vmush.adapter.KumbungAdapter
import com.example.vmush.model.KumbungModel
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class HomeFragment : Fragment() {

    private var linkList: MutableList<KumbungModel> = mutableListOf()
    private val client = OkHttpClient()
    private lateinit var adapter: KumbungAdapter
    private val handler = Handler(Looper.getMainLooper())
    private val pollingInterval = 2000L // 2 detik

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val rvKumbung: RecyclerView = rootView.findViewById(R.id.rvKumbung)
        rvKumbung.layoutManager = LinearLayoutManager(requireContext())

        adapter = KumbungAdapter(linkList) { kumbung ->
            // Handle klik di sini, jalankan intent
            val selectedLink = extractLinkFromName(kumbung.name) // Extract link asli dari name
            val intent = Intent(requireContext(), DetailKumbungActivity::class.java).apply {
                putExtra("name", kumbung.name)
                putExtra("link", selectedLink) // Kirim link yang dipilih saja
            }
            startActivity(intent)
        }
        rvKumbung.adapter = adapter

        getFirebaseLinks("alpant") // Ganti dengan username yang sesuai

        return rootView
    }

    // Fungsi helper untuk extract link dari name
    private fun extractLinkFromName(uniqueName: String): String {
        // Karena uniqueName format: "$link#$i"
        return uniqueName.substringBefore("#")
    }

    private fun getFirebaseLinks(username: String) {
        val url = "https://vmush.site/api/Data/Link-Firebase/tampil/$username"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e("HomeFragment", "Network Error: ${e.message}", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    try {
                        val responseBody = response.body?.string()
                        if (responseBody != null) {
                            val responseJson = JSONObject(responseBody)
                            val dataLinks = responseJson.optJSONArray("DataLinkFirebase")

                            if (dataLinks != null && dataLinks.length() > 0) {
                                linkList.clear()

                                for (i in 0 until dataLinks.length()) {
                                    val link = dataLinks.optJSONObject(i)?.optString("link")
                                    if (!link.isNullOrEmpty()) {
                                        val uniqueName = "$link#$i" // Buat unik supaya index aman
                                        Log.d("HomeFragment", "Adding link: $uniqueName")

                                        linkList.add(
                                            KumbungModel(
                                                name = uniqueName,
                                                temperature = "N/A",
                                                humidity = "N/A"
                                            )
                                        )

                                        // Start polling untuk setiap link
                                        startPollingLinkData(link, i)
                                    }
                                }

                                activity?.runOnUiThread {
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("HomeFragment", "Error parsing the response", e)
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "Error parsing response", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("HomeFragment", "Response failed: ${response.message}")
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun startPollingLinkData(link: String, index: Int) {
        val runnable = object : Runnable {
            override fun run() {
                fetchLinkData(link, index) {
                    handler.postDelayed(this, pollingInterval)
                }
            }
        }
        handler.post(runnable)
    }

    private fun fetchLinkData(link: String, index: Int, onComplete: () -> Unit) {
        val request = Request.Builder().url(link).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("HomeFragment", "Error fetching data for $link: ${e.message}", e)
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Error fetching data for $link", Toast.LENGTH_SHORT).show()
                }
                onComplete()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    try {
                        val responseBody = response.body?.string()
                        if (responseBody != null) {
                            val responseJson = JSONObject(responseBody)

                            val temperature = responseJson.optString("temperature", "N/A")
                            val humidity = responseJson.optString("humidity", "N/A")

                            if (index in linkList.indices) {
                                val currentItem = linkList[index]
                                linkList[index] = KumbungModel(
                                    name = currentItem.name,
                                    temperature = temperature,
                                    humidity = humidity
                                )

                                activity?.runOnUiThread {
                                    adapter.notifyItemChanged(index)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("HomeFragment", "Error parsing data for $link: ${e.message}", e)
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "Error parsing data for $link", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("HomeFragment", "Failed to fetch data for $link: ${response.message}")
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "Failed to fetch data for $link", Toast.LENGTH_SHORT).show()
                    }
                }
                onComplete()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}