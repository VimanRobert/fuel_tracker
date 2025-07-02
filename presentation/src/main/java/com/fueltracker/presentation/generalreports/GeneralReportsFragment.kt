package com.fueltracker.presentation.generalreports

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fueltracker.presentation.databinding.FragmentGeneralReportsBinding
import com.fueltracker.presentation.utils.ChartsHandler.setupChart
import com.fueltracker.presentation.utils.ChartsHandler.setupGeneralCharts
import com.fueltracker.presentation.utils.ConnectionHandler.fetchAllUserReports
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneralReportsFragment : Fragment() {

    private var _binding: FragmentGeneralReportsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralReportsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser

        fetchAllUserReports(currentUser!!.uid,
            onResult = { entries, labels ->
                setupGeneralCharts(binding.lineChart, entries, labels)
            },
            onError = {
                Log.e("Firestore", "Error loading general reports", it)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
