package com.fueltracker.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fueltracker.presentation.R
import com.fueltracker.presentation.activity.MainActivity
import com.fueltracker.presentation.databinding.FragmentLoginBinding
import com.fueltracker.presentation.utils.UserHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userHelper: UserHelper
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseAuth = FirebaseAuth.getInstance()
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userHelper = UserHelper(requireContext().applicationContext)
        binding.login.setOnClickListener {
            userHelper.login(
                binding.userEmail.text.toString(),
                binding.userPassword.text.toString()
            ) { isSuccessful ->
                if (isSuccessful) {
                    startActivity(Intent(context, MainActivity::class.java))
                } else {
                    Log.e("TAG", "credentials don't match")
                }
            }
        }
        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}