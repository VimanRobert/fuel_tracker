package com.fueltracker.presentation.fuelinfo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fueltracker.presentation.R
import com.fueltracker.presentation.databinding.FragmentDailyReportsBinding
import com.fueltracker.presentation.databinding.FragmentFuelInfoBinding
import com.fueltracker.presentation.databinding.FragmentInfoBinding
import com.fueltracker.presentation.utils.ChartsHandler.setupFuelChart
import com.fueltracker.presentation.utils.ConnectionHandler.fetchFuelUsageReports
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FuelInfoFragment : Fragment() {

    private var _binding: FragmentFuelInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFuelInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser!!

        fetchFuelUsageReports(currentUser.uid,
            onResult = { entries, labels ->
                setupFuelChart(binding.chartFuelUsedReport, entries, labels)
            },
            onError = {
                Log.e("Firestore", "Failed to fetch fuelUsed reports", it)
            }
        )
    }
}