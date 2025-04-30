package com.fueltracker.presentation.carstatus

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.fueltracker.presentation.databinding.FragmentCarStatusBinding
import com.fueltracker.presentation.utils.ConnectionHandler.fetchCurrentUserCarData
import com.fueltracker.presentation.utils.SystemHandler.isAutomotive
import com.fueltracker.presentation.utils.SystemHandler.isMobile
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carStatusViewModel.registerCarData(requireContext())

        binding.apply {
            if (isMobile(requireContext())) {
                fetchCurrentUserCarData(
                    onSuccess = { car ->
                        labelBrand.text = car.brand
                        labelModel.text = car.type
                        labelEngine.text = car.engine.toString()
                        labelHorsePower.text = car.horsePower.toString()

                    },
                    onFailure = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            } else if (isAutomotive(requireContext())) {
                carStatusViewModel.retrieveCarData(requireContext()).apply {
                    labelCarId!!.text = carId
                    labelBrand.text = brand
                    labelModel.text = type
                    labelEngine.text = engine.toString()
                    labelHorsePower.text = horsePower.toString()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
