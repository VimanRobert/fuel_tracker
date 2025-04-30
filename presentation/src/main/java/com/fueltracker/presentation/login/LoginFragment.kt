package com.fueltracker.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fueltracker.presentation.R
import com.fueltracker.presentation.activity.MainActivity
import com.fueltracker.presentation.databinding.FragmentLoginBinding
import com.fueltracker.presentation.utils.UserHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userHelper: UserHelper

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

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

        with(binding) {
            if (!loginViewModel.isUserConnected(requireContext(), userHelper)) {
                Log.e("TAG", "No user connected")
                login.setOnClickListener {
                    userHelper.login(
                        binding.userEmail.text.toString(),
                        binding.userPassword.text.toString()
                    ) { isSuccessful ->
                        if (isSuccessful) {
                            userHelper.showUsernameDialog(requireContext()) { username ->
                                val user = FirebaseAuth.getInstance().currentUser
                                if (user != null) {
                                    userHelper.createOrUpdateUserInFirestore(
                                        user,
                                        username,
                                        onSuccess = {
                                            startActivity(
                                                Intent(
                                                    requireContext(),
                                                    MainActivity::class.java
                                                )
                                            )
                                            requireActivity().finish()
                                        },
                                        onFailure = { error ->
                                            Toast.makeText(
                                                requireContext(),
                                                error,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    )
                                }
                            }

                            userHelper.generateSessionToken()
                            startActivity(Intent(context, MainActivity::class.java))
                        } else {
                            Log.e("TAG", "credentials don't match")
                        }
                    }
                }
                loginGoogle.setOnClickListener {
                    startSignIn()
                }
                register.setOnClickListener {
                    findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                }
            } else {
                Log.e("TAG", "User connected: ${userHelper.getCurrentUser()?.email.toString()}")
                startActivity(Intent(context, MainActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            userHelper.handleGoogleSignInResult(result.data,
                onSuccess = { user ->
                    Log.d("SignIn", "User: ${user.email}")
                    FirebaseFirestore.getInstance().collection("users")
                        .document(user.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                startActivity(Intent(requireContext(), MainActivity::class.java))
                                requireActivity().finish()
                            } else {
                                userHelper.showUsernameDialog(requireContext()) { username ->
                                    val currentUser = FirebaseAuth.getInstance().currentUser
                                    if (currentUser != null) {
                                        userHelper.createOrUpdateUserInFirestore(
                                            currentUser,
                                            username,
                                            onSuccess = {
                                                startActivity(
                                                    Intent(
                                                        requireContext(),
                                                        MainActivity::class.java
                                                    )
                                                )
                                                requireActivity().finish()
                                            },
                                            onFailure = { error ->
                                                Toast.makeText(
                                                    requireContext(),
                                                    error,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        )
                                    }
                                }
                            }
                        }

                    userHelper.generateSessionToken()
                },
                onError = { error ->
                    Log.e("SignIn", "Error: ${error.message}")
                }
            )
        }

    private fun startSignIn() {
        val signInIntent = userHelper.getGoogleSignInClient().signInIntent
        signInLauncher.launch(signInIntent)
    }

}