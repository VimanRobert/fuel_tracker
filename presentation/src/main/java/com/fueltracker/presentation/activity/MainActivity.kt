package com.fueltracker.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fueltracker.presentation.databinding.ActivityMainBinding
import com.fueltracker.presentation.utils.NotificationsHelper.showConnectDialogIfNeeded
import com.fueltracker.presentation.utils.handleBackButtonNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showConnectDialogIfNeeded(this)

        binding.toolbarBackButton.setOnClickListener {
            handleBackButtonNavigation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
