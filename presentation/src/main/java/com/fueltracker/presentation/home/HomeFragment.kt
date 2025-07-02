package com.fueltracker.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fueltracker.domain.model.HomeDestination
import com.fueltracker.presentation.R
import com.fueltracker.presentation.databinding.FragmentHomeBinding
import com.fueltracker.presentation.utils.SystemHandler.isAutomotive
import com.fueltracker.presentation.utils.SystemHandler.isMobile
import com.fueltracker.presentation.utils.homeItemsList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HomeAdapter(homeItemsList) { destination ->
            when (destination) {
                HomeDestination.INFO -> findNavController().navigate(R.id.action_homeFragment_to_infoFragment)
                HomeDestination.CAR_STATUS -> findNavController().navigate(R.id.action_homeFragment_to_carStatusFragment)
                HomeDestination.GENERAL_REPORTS -> findNavController().navigate(R.id.action_homeFragment_to_generalReportsFragment)
                HomeDestination.USER_STATUS -> findNavController().navigate(R.id.action_homeFragment_to_userStatusFragment)
                HomeDestination.FUEL_INFO -> TODO()
                HomeDestination.DAILY_REPORTS -> findNavController().navigate(R.id.action_homeFragment_to_dailyReportsFragment)
            }
        }

        with(binding) {
            if (isMobile(requireContext())) {
                homeRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
            } else if (isAutomotive(requireContext())) {
                homeRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
            }
            homeRecyclerView.adapter = adapter
        }

//        createLinearChart(
//            listOf(
//                Entry(0f, 5.8f),
//                Entry(1f, 6.5f),
//                Entry(2f, 7f),
//                Entry(3f, 6.6f),
//                Entry(4f, 6f),
//                Entry(5f, 6.2f),
//                Entry(6f, 6.8f),
//                Entry(7f, 6.5f),
//            ), binding.lineChart
//        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
