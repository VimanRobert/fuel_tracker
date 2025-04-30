package com.fueltracker.presentation.utils

import android.accounts.AccountManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.fueltracker.presentation.R
import com.fueltracker.presentation.utils.ConnectionHandler.connectCarAppToUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class UserHelper(val context: Context) {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestoreData: FirebaseFirestore

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    fun handleGoogleSignInResult(
        data: Intent?,
        onSuccess: (FirebaseUser) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        auth.currentUser?.let(onSuccess)
                    } else {
                        onError(result.exception ?: Exception("Unknown error"))
                    }
                }
        } catch (e: ApiException) {
            onError(e)
        }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun generateSessionToken() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result?.token
                    if (idToken != null) {
                        saveUserToken(context, idToken)
                    }
                } else {
                    Log.e("TokenError", "Failed to get token", task.exception)
                }
            }
    }

    private fun saveUserToken(context: Context, token: String) {
        val sharedPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("firebase_token", token).apply()
    }

    fun getUserToken(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPrefs.getString("firebase_token", null)
    }

    private fun clearStoredUserData(context: Context) {
        val sharedPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().clear().apply()
    }

    fun disconnectUser(context: Context, onComplete: () -> Unit) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        googleSignInClient.signOut().addOnCompleteListener {
            clearStoredUserData(context)
            onComplete()
        }
    }

    fun retrieveCarUserProfileInfo(email: TextView?, userName: TextView?) {
        val accounts = AccountManager.get(context).getAccountsByType("com.google")
        //val account = accounts.firstOrNull()
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            email?.text = account.email
            userName?.text = account.displayName
        } else {
            email?.text = "No Google account found"
            userName?.text = "Unknown user"
        }
    }

    fun getGoogleEmail(): String? {
        val accounts = AccountManager.get(context).getAccountsByType("com.google")
        return accounts.firstOrNull()?.name
    }

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
                        //setupUserCollection(firestoreData, userId!!)
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

    fun createOrUpdateUserInFirestore(
        user: FirebaseUser,
        userName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (userName.length < 3) {
            onFailure("Username too short.")
            return
        }

        val userDoc = FirebaseFirestore.getInstance().collection("users").document(user.uid)

        userDoc.get().addOnSuccessListener { document ->
            if (document.exists()) {
                userDoc.update("username", userName).addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener { e ->
                    onFailure("Failed to update username: ${e.localizedMessage}")
                }
            } else {
                val pairingCode = user.uid.takeLast(8)
                val newUser = hashMapOf(
                    "userId" to user.uid,
                    "userEmail" to user.email,
                    "userName" to userName,
                    "pairingCode" to pairingCode
                )

                userDoc.set(newUser).addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener { e ->
                    onFailure("Failed to create user: ${e.localizedMessage}")
                }
            }
        }.addOnFailureListener { e ->
            onFailure("Error checking username: ${e.localizedMessage}")
        }
    }

    fun showUsernameDialog(
        context: Context,
        onUsernameConfirmed: (String) -> Unit
    ) {
        val input = EditText(context).apply {
            hint = "Enter username"
            inputType = InputType.TYPE_CLASS_TEXT
            setPadding(32, 16, 32, 16)
        }

        AlertDialog.Builder(context)
            .setTitle("Choose a username")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("Confirm") { dialog, _ ->
                val username = input.text.toString().trim()
                if (username.length in 3..20) {
                    onUsernameConfirmed(username)
                    dialog.dismiss()
                } else {
                    input.error = "Username must be 3-20 characters"
                }
            }
            .show()
    }


//    fun connectCarAppToUser(
//        pairingCode: String,
//        onSuccess: (DocumentSnapshot) -> Unit,
//        onFailure: (String) -> Unit
//    ) {
//        Firebase.firestore.collection("users")
//            .whereEqualTo("pairingCode", pairingCode)
//            .limit(1)
//            .get()
//            .addOnSuccessListener { result ->
//                if (!result.isEmpty) {
//                    val userDoc = result.documents.first()
//                    onSuccess(userDoc)
//                } else {
//                    onFailure("No user found with that code.")
//                }
//            }
//            .addOnFailureListener { exception ->
//                onFailure("Error: ${exception.localizedMessage}")
//            }
//    }

    fun showPairingCodeDialog(context: Context, onCodeEntered: (String) -> Unit) {
        val inputField = EditText(context).apply {
            hint = "Enter pairing code"
            inputType = InputType.TYPE_CLASS_TEXT
            setHintTextColor(Color.GRAY)
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle("Pairing Code")
            .setView(inputField)
            .setPositiveButton("Confirm", null)
            .setNegativeButton("Cancel") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            val confirmButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            confirmButton.setOnClickListener {
                val code = inputField.text.toString()

                if (code.length != 8) {
                    inputField.setText("")
                    inputField.hint = "Invalid code"
                    inputField.setHintTextColor(Color.RED)
                } else {
                    onCodeEntered(code)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }
}
