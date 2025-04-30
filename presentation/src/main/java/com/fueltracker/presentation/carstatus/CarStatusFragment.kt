package com.fueltracker.presentation.carstatus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.fueltracker.presentation.databinding.FragmentCarStatusBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CarStatusFragment : Fragment() {

    private var _binding: FragmentCarStatusBinding? = null
    private val binding get() = _binding!!

    private val carStatusViewModel: CarStatusViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarStatusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            labelCarId?.text = carStatusViewModel.retrieveCarId(requireContext())
            textBrand.text = carStatusViewModel.retrieveCarData(requireContext()).brand
        }









    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}