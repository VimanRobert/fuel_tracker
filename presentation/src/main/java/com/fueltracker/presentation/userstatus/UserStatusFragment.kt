package com.fueltracker.presentation.userstatus

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fueltracker.presentation.databinding.FragmentUserStatusBinding
import com.fueltracker.presentation.login.LoginActivity
import com.fueltracker.presentation.utils.ConnectionHandler.connectCarAppToUser
import com.fueltracker.presentation.utils.UserHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserStatusFragment : Fragment() {

    private var _binding: FragmentUserStatusBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userHelper: UserHelper
    private val viewModel: UserStatusViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userHelper = UserHelper(requireContext())
        firebaseAuth = FirebaseAuth.getInstance()
        _binding = FragmentUserStatusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("VIMAN", "Current user: ${firebaseAuth.currentUser?.email}")

        with(binding) {
            if (firebaseAuth.currentUser == null) {
                buttonDisconnect?.isEnabled = false
                buttonDisconnect?.isVisible = false
                buttonConnectNewUser?.apply {
                    isEnabled = true
                    isVisible = true
                    setOnClickListener {
                        userHelper.showPairingCodeDialog(requireContext(), { code ->
                            val currentCarConfig = viewModel.callCarData(requireContext())
                            connectCarAppToUser(
                                requireContext(),
                                car = currentCarConfig,
                                pairingCode = code,
                                onSuccess = { userDoc ->
                                    val username = userDoc.getString("username") ?: "Unknown"
                                    Toast.makeText(
                                        context,
                                        "Connected to $username",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.i("VIMAN", "Current user: ${FirebaseAuth.getInstance().currentUser?.email}")
                                    lifecycleScope.launch {
                                        labelEmail.text = viewModel.fetchCurrentUserData()?.userEmail
                                    }
                                },
                                onFailure = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    Log.e("VIMAN", "Dailed to write database")
                                }
                            )
                        })
                    }
                }
            } else {
                buttonConnectNewUser?.isEnabled = false
                buttonConnectNewUser?.isVisible = false
                buttonDisconnect?.apply {
                    isEnabled = true
                    isVisible = true
                    setOnClickListener {
                        userHelper.disconnectUser(requireContext()) {
                            startActivity(Intent(context, LoginActivity::class.java))
                        }
                    }
                }

                lifecycleScope.launch {
                    labelEmail.text = viewModel.fetchCurrentUserData()?.userEmail
                    labelUsername.text = viewModel.fetchCurrentUserData()?.userName
                    labelPaircode!!.text = viewModel.fetchCurrentUserData()?.pairingCode
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}