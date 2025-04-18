package com.fueltracker.presentation.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserHelper(val context: Context) {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestoreData: FirebaseFirestore

    fun login(email: String, password: String, onLoginResult: (Boolean) -> Unit) {
        FirebaseApp.initializeApp(context)
        firebaseAuth = FirebaseAuth.getInstance()
        val signInInputsArray = arrayOf(email, password)
        if (email.isNotEmpty() || password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        Toast.makeText(context, "Successfully connected!", Toast.LENGTH_SHORT)
                            .show()
                        onLoginResult(true)
                    } else {
                        Toast.makeText(
                            context,
                            "Connection failed! Try again",
                            Toast.LENGTH_LONG
                        ).show()
                        onLoginResult(false)
                    }
                }

        } else if (email.isEmpty() || password.isEmpty()) {
            signInInputsArray.forEach { input ->
                if (input.trim().isEmpty()) {
                    Toast.makeText(context, "$input field is required!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(context, "All field are required!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun createAccount(email: String, password: String) {
        FirebaseApp.initializeApp(context)
        firebaseAuth = FirebaseAuth.getInstance()
        firestoreData = FirebaseFirestore.getInstance()
        try {
            email.trim()
            password.trim()
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Account created successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val userId = firebaseAuth.currentUser?.uid
                        Log.i("TAG", "uid: $userId")
  //                      setupUserCollection(firestoreData, userId!!)
                        //sendEmailVerification()
                    } else {
                        Toast.makeText(
                            context,
                            "Sign in failed!\nTry again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } catch (error: Error) {
            Log.e("TAG", "$error - cannot create account!")
        }
    }

    fun confirmPassword(passwordField: String, confirmationField: String): Boolean {
        val createAccountInputsArray = arrayOf(passwordField, confirmationField)
        var identical = false
        if (passwordField.isNotEmpty() &&
            passwordField.trim() == confirmationField.trim()
        ) {
            identical = true
        } else if (passwordField.isNotEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input.trim().isEmpty()) {
                    Toast.makeText(context, "$input field is required!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            Toast.makeText(context, "This field is required", Toast.LENGTH_SHORT).show()
        }
        return identical
    }

    private fun isUserRegistered(email: String) {
        firebaseAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {
                        Log.i("TAG", "no user registered with this email")
                    } else {
                        Log.i("TAG", "an user is already registered with this email")
                    }
                } else {
                    Log.i("TAG", "an error occurred")
                }
            }
    }

//    private fun setupUserCollection(firestore: FirebaseFirestore, userId: String) {
//        val userRef = firestore.collection("users").document(userId)
//        Log.i("TAG", "ref: $userRef")
//
//        val userData = mapOf(
//            "email" to FirebaseAuth.getInstance().currentUser?.email,
//            "created_at" to System.currentTimeMillis(),
//        )
//        userRef.set(userData).addOnSuccessListener {
//            createSubcollections(userRef)
//        }.addOnFailureListener { exception ->
//            Log.e("TAG", "Failed to create user document: ${exception.localizedMessage}")
//        }
//    }

//    private fun createSubcollections(userDocRef: DocumentReference) {
//        val photosData = mapOf("initialized" to true)
//        val documentsData = mapOf("initialized" to true)
//        val notesData = mapOf("initialized" to true)
//
//        userDocRef.collection(Sections.PHOTOS.title).document("metadata")
//            .set(photosData)
//            .addOnSuccessListener {
//                Log.d("TAG", "Photos subcollection created successfully.")
//            }
//            .addOnFailureListener { exception ->
//                Log.e("TAG", "Error creating Photos subcollection: ${exception.localizedMessage}")
//            }
//
//        userDocRef.collection(Sections.DOCUMENTS.title).document("metadata")
//            .set(documentsData)
//            .addOnSuccessListener {
//                Log.d("TAG", "Documents subcollection created successfully.")
//            }
//            .addOnFailureListener { exception ->
//                Log.e(
//                    "TAG",
//                    "Error creating Documents subcollection: ${exception.localizedMessage}"
//                )
//            }
//
//        userDocRef.collection(Sections.NOTES.title).document("metadata")
//            .set(notesData)
//            .addOnSuccessListener {
//                Log.d("TAG", "Notes subcollection created successfully.")
//            }
//            .addOnFailureListener { exception ->
//                Log.e("TAG", "Error creating Notes subcollection: ${exception.localizedMessage}")
//            }
//
//        userDocRef.collection(Sections.OTHERS.title).document("metadata")
//            .set(notesData)
//            .addOnSuccessListener {
//                Log.d("TAG", "Notes subcollection created successfully.")
//            }
//            .addOnFailureListener { exception ->
//                Log.e("TAG", "Error creating Notes subcollection: ${exception.localizedMessage}")
//            }
//    }
}