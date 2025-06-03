package com.example.vmush.Pages.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vmush.R
import com.example.vmush.model.KumbungModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class DetailKumbungActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val handler = Handler(Looper.getMainLooper())
    private val pollingInterval = 2000L // 2 detik
    private var isPolling = false
    private var pollingRunnable: Runnable? = null

    // Declare variables that were missing
    private var link: String? = null
    private var name: String? = null
    private var kumbungData: KumbungModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_kumbung)

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        link = intent.getStringExtra("link")
        name = intent.getStringExtra("name")

        // Setup back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Start polling if link is not null
        link?.let { linkUrl ->
            startPollingLinkData(linkUrl)
        }
    }

    private fun startPollingLinkData(link: String) {
        if (isPolling) return // Prevent multiple polling instances

        isPolling = true
        pollingRunnable = object : Runnable {
            override fun run() {
                if (isPolling) {
                    fetchLinkData(link) {
                        if (isPolling) {
                            handler.postDelayed(this, pollingInterval)
                        }
                    }
                }
            }
        }
        pollingRunnable?.let { handler.post(it) }
    }

    private fun stopPolling() {
        isPolling = false
        pollingRunnable?.let { handler.removeCallbacks(it) }
    }

    private fun fetchLinkData(link: String, onComplete: () -> Unit) {
        val request = Request.Builder().url(link).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("DetailKumbungActivity", "Error fetching data for $link: ${e.message}", e)
                runOnUiThread {
                    Toast.makeText(this@DetailKumbungActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
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
                            val name = responseJson.optString("name", "Kumbung") // Get name from API or set default

                            // Update single kumbung data
                            kumbungData = KumbungModel(
                                name = name,
                                temperature = temperature,
                                humidity = humidity
                            )

                            runOnUiThread {
                                // Update UI elements here with the new data
                                updateUI(kumbungData!!)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("DetailKumbungActivity", "Error parsing data for $link: ${e.message}", e)
                        runOnUiThread {
                            Toast.makeText(this@DetailKumbungActivity, "Error parsing data", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("DetailKumbungActivity", "Failed to fetch data for $link: ${response.message}")
                    runOnUiThread {
                        Toast.makeText(this@DetailKumbungActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }
                onComplete()
            }
        })
    }

    private fun updateUI(data: KumbungModel) {
        // Update UI elements with the data
        findViewById<TextView>(R.id.tvTitle).text = name
        findViewById<TextView>(R.id.tvTemp).text = "${data.temperature}°"
        findViewById<TextView>(R.id.tvHumidity).text = "${data.humidity}"

        // Update status based on temperature and humidity values
        updateGrowthStatus(data.temperature, data.humidity)
    }

    private fun updateGrowthStatus(temperature: String, humidity: String) {
        val growthTextView = findViewById<TextView>(R.id.tvGrowth)
        val statusTextView = findViewById<TextView>(R.id.tvStatus)

        try {
            val temp = temperature.toDoubleOrNull()
            val hum = humidity.toDoubleOrNull()

            // Define optimal ranges for mushroom growth
            val optimalTempRange = 20.0..25.0
            val optimalHumRange = 75.0..85.0

            when {
                temp != null && hum != null &&
                        temp in optimalTempRange && hum in optimalHumRange -> {
                    growthTextView.text = "Optimal Growth"
                    growthTextView.setTextColor(getColor(android.R.color.holo_green_dark))
                    statusTextView.text = "Active"
                    statusTextView.setTextColor(getColor(android.R.color.holo_green_dark))
                }
                temp != null && hum != null -> {
                    growthTextView.text = "Normal Growth"
                    growthTextView.setTextColor(getColor(android.R.color.holo_orange_dark))
                    statusTextView.text = "Active"
                    statusTextView.setTextColor(getColor(android.R.color.holo_orange_dark))
                }
                else -> {
                    growthTextView.text = "No Data"
                    growthTextView.setTextColor(getColor(android.R.color.holo_red_dark))
                    statusTextView.text = "Error"
                    statusTextView.setTextColor(getColor(android.R.color.holo_red_dark))
                }
            }
        } catch (e: Exception) {
            growthTextView.text = "Unknown Status"
            growthTextView.setTextColor(getColor(android.R.color.darker_gray))
            statusTextView.text = "Unknown"
            statusTextView.setTextColor(getColor(android.R.color.darker_gray))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPolling() // Stop polling when activity is destroyed
    }

    override fun onPause() {
        super.onPause()
        stopPolling() // Stop polling when activity is paused
    }

    override fun onResume() {
        super.onResume()
        // Restart polling when activity resumes
        link?.let { linkUrl ->
            startPollingLinkData(linkUrl)
        }
    }
}