package com.example.vmush.Pages.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vmush.R
import com.example.vmush.adapter.KumbungAdapter
import com.example.vmush.model.KumbungModel

class HomeFragment : Fragment() {

    private lateinit var rvKumbung: RecyclerView
    private lateinit var kumbungAdapter: KumbungAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvKumbung = view.findViewById(R.id.rvKumbung)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val dummyData = listOf(
            KumbungModel("Kumbung 1", "23°C", "82%", "Normal", true),
            KumbungModel("Kumbung 2", "24°C", "78%", "Good", false),
            KumbungModel("Kumbung 3", "22°C", "85%", "Excellent", true),
            KumbungModel("Kumbung 4", "25°C", "80%", "Normal", true)
        )

        kumbungAdapter = KumbungAdapter(dummyData)

        rvKumbung.layoutManager = LinearLayoutManager(requireContext())
        rvKumbung.adapter = kumbungAdapter
    }
}
