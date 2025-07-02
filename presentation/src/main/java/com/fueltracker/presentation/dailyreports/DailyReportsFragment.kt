package com.fueltracker.presentation.dailyreports

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fueltracker.presentation.databinding.FragmentDailyReportsBinding
import com.fueltracker.presentation.utils.ChartsHandler.setupChart
import com.fueltracker.presentation.utils.ConnectionHandler.fetchUserCarDailyReports
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyReportsFragment : Fragment() {

    private var _binding: FragmentDailyReportsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyReportsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser

        fetchUserCarDailyReports(userId = currentUser!!.uid,
            onResult = { entries, timeLabels ->
                setupChart(binding.chartDailyReport, entries, timeLabels)
            },
            onError = {
                Log.e("Firestore", "Error loading reports", it)
            }
        )
    }
}