package com.fueltracker.presentation.userstatus

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fueltracker.presentation.databinding.FragmentUserStatusBinding
import com.fueltracker.presentation.utils.UserHelper

class UserStatusFragment : Fragment() {

    private var _binding: FragmentUserStatusBinding? = null
    private val binding get() = _binding!!

    private lateinit var user: UserHelper
//    private val viewModel: UserStatusViewModel by viewModels()
    private lateinit var viewModel: UserStatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        user = UserHelper(requireContext())
        _binding = FragmentUserStatusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = UserStatusViewModel(user)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.GET_ACCOUNTS)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.GET_ACCOUNTS),
                1001
            )
        } else {
            user.getGoogleEmail()
        }

        user.retrieveCarUserProfileInfo(binding.labelUsername, binding.labelEmail)
//        viewModel.userEmail.observe(viewLifecycleOwner) { email ->
//            binding.labelEmail?.text = email ?: "Email not found"
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                user.getGoogleEmail()
            } else {
                // Handle the case when permission is denied
                Log.e("UserStatusFragment", "Permission denied for GET_ACCOUNTS")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}